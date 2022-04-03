package fr.orleans.univ.miage.m2.rbnbreservationservice.controller;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.ReservationDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.ReservationService;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.CapaciteLogementDepasseException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.ReservationIntrouvableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ReservationController {

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RolesAllowed("USER")
    @GetMapping("hello")
    public String hello(Principal principal){
        return "connected as user with id =" +principal.getName();
    }

    @GetMapping("norole")
    public String noauth(Principal principal){
        return "connected with no role with id =" + principal.toString();
    }

    @RolesAllowed("ADMIN")
    @GetMapping("admin")
    public String admin(Principal principal){
        return "connected as admin with id =" + principal.getName();
    }



    @GetMapping("/{id}/reservation")
    public ResponseEntity<Object> getReservationsByVoyageur(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(reservationService.getReservationsByVoyageur(id));
    }

    @RolesAllowed("USER")
    @PostMapping("/reservation")
    public ResponseEntity<Object> createReservation(@RequestBody ReservationDTO reservation, Principal principal) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservation,principal));
        } catch (LogementsIndisponibleException | CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/reservation/{idReservation}/voyageur")
    public ResponseEntity<Object> updateNbVoyageursReservation(@PathVariable String idReservation, @RequestBody int nbVoyageurs){
        try {
            reservationService.updateNbVoyageursReservation(idReservation,nbVoyageurs);
            return ResponseEntity.ok().body(reservationService.getReservationsByIdReservation(idReservation));
        } catch (NbVoyagageurIncorrecteException | CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/reservation/{idReservation}/date")
    public ResponseEntity<Object> updateDateReservation(@PathVariable String idReservation, @RequestBody Date dateDebut, @RequestBody Date dateFin, Principal principal) {
        try {
            reservationService.updateDateReservation(idReservation, dateDebut, dateFin, principal);
            return ResponseEntity.ok().body(reservationService.getReservationsByIdReservation(idReservation));
        } catch (LogementsIndisponibleException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        } catch (CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/reservation/{idReservation}")
    public ResponseEntity<Object> annulerReservation(@PathVariable String idReservation) {
        try {
            reservationService.annulerReservation(idReservation);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
