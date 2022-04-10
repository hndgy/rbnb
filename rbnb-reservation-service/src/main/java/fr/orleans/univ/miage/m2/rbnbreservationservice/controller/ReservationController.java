package fr.orleans.univ.miage.m2.rbnbreservationservice.controller;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.DisponibiliteDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.ReservationDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.UpdateDateDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.ReservationService;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservation")  //TODO : REFAIRE LES URL !!!
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


    @RolesAllowed("USER")
    @GetMapping("/{id}/reservation")
    public ResponseEntity<Object> getReservationsByVoyageur(@PathVariable(name = "id") String id) {
        try {
            return ResponseEntity.ok().body(reservationService.getReservationsByVoyageur(id));
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("USER")
    @GetMapping("/{id}/reservations")
    public ResponseEntity<Object> getReservationsByHote(@PathVariable(name = "id") String id, @RequestHeader(name = "Authorization")String token) {
        try {
            return ResponseEntity.ok().body(reservationService.getReservationsByHote(id, token));
        } catch (ReservationIntrouvableException | LogementIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @RolesAllowed("USER")
    @PostMapping("/reservation")
    public ResponseEntity<Object> createReservation(@RequestBody ReservationDTO reservation, Principal principal, @RequestHeader(name = "Authorization")String token) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservation,principal, token));
        } catch (LogementsIndisponibleException | CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (LogementIntrouvableException | UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("USER")
    @GetMapping("/reservation/{id}")
    public ResponseEntity<Object> getReservationById(@PathVariable(name = "id") String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationsByIdReservation(id));
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("USER")
    @PutMapping("/reservation/{idReservation}/voyageur")
    public ResponseEntity<Object> updateNbVoyageursReservation(@PathVariable String idReservation, @RequestBody int nbVoyageurs, @RequestHeader(name = "Authorization")String token){
        try {
            reservationService.updateNbVoyageursReservation(idReservation,nbVoyageurs,token);
            return ResponseEntity.ok().body(reservationService.getReservationsByIdReservation(idReservation));
        } catch (NbVoyagageurIncorrecteException | CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ReservationIntrouvableException | LogementIntrouvableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("USER")
    @PutMapping("/reservation/{idReservation}/date")
    public ResponseEntity<Object> updateDateReservation(@PathVariable String idReservation, @RequestBody UpdateDateDTO dates, Principal principal, @RequestHeader(name = "Authorization")String token) {
        try {
            reservationService.updateDateReservation(idReservation, dates.getDateDebut(), dates.getDateFin(), principal,token);
            return ResponseEntity.ok().body(reservationService.getReservationsByIdReservation(idReservation));
        } catch (LogementsIndisponibleException | CapaciteLogementDepasseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ReservationIntrouvableException | LogementIntrouvableException | UtilisateurInexistantException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO : URL + générer les idDisponibilite ???
    @RolesAllowed("HOTE")
    @PostMapping("/hote/disponibilite/{idLogement}")
    public ResponseEntity<Object> setDisponibilite(@PathVariable Long idLogement, @RequestBody List<DisponibiliteDTO> disponibilitesDTO , @RequestHeader(name = "Authorization")String token, Principal principal) {
        try {
            return ResponseEntity.ok().body(reservationService.setDisponibilite(idLogement,disponibilitesDTO,token, principal));
        } catch (LogementIntrouvableException | UtilisateurInexistantException e) {
            return ResponseEntity.notFound().build();
        } catch (LogementsDejaDisponibleException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RolesAllowed("USER")
    @DeleteMapping("/user/reservation/{idReservation}")
    public ResponseEntity<Object> deleteReservationByIdReservation(@PathVariable String idReservation) {
        try {
            reservationService.annulerReservation(idReservation);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

/*
    @RolesAllowed("HOTE")
    @DeleteMapping("/hote/reservation/{idHote}")
    public ResponseEntity<Object> deleteReservationEtUpdateDispoWhenHostDeleted(@PathVariable Long idHote, @RequestHeader(name = "Authorization")String token) {
        try {
            reservationService.deleteDispoEtReservationWhenHostDeleted(idHote, token);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException | LogementIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RolesAllowed("USER")
    @DeleteMapping("/user/reservation/{idClient}")
    public ResponseEntity<Object> deleteReservationEtUpdateDispoWhenClientDeleted(@PathVariable Long idClient) {
        try {
            reservationService.deleteDispoEtReservationWhenClientDeleted(idClient);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RolesAllowed("USER")
    @DeleteMapping("/user/reservation/{idClient}")
    public ResponseEntity<Object> deleteReservationClientByClient(@PathVariable Long idClient) {
        try {
            reservationService.deleteReservationClientByClient(idClient);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RolesAllowed("HOTE")
    @DeleteMapping("/hote/reservation/{idReservation}")
    public ResponseEntity<Object> deleteReservationClientByHote(@PathVariable String idReservation) {
        try {
            reservationService.deleteReservationClientByHote(idReservation);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RolesAllowed("HOTE")
    @DeleteMapping("/hote/reservation/{idLogement}")
    public ResponseEntity<Object> deleteReservationClientWhenLogementDeleted(@PathVariable Long idLogement) {
        try {
            reservationService.deleteReservationClientWhenLogementDeleted(idLogement);
            return ResponseEntity.ok().build();
        } catch (ReservationIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

     */

}
