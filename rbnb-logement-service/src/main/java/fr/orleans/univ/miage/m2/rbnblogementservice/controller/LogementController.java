package fr.orleans.univ.miage.m2.rbnblogementservice.controller;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.CreationLogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import fr.orleans.univ.miage.m2.rbnblogementservice.exception.LogementNotFoundException;
import fr.orleans.univ.miage.m2.rbnblogementservice.service.LogementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logement")
public class LogementController {

    @Autowired
    LogementService logementService;

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<List<Logement>> getAllLogement(){
        List<Logement> logementList = null;
        logementList = logementService.getAllLogements();
        return new ResponseEntity<>(logementList, new HttpHeaders(), HttpStatus.OK);

    }

    @RolesAllowed({"HOTE","USER"})
    @GetMapping("/{idLogement}")
    public ResponseEntity<Optional<Logement>> getLogement(@PathVariable Long idLogement){
        Optional<Logement> logement = null;
        try {
            logement = logementService.getLogementById(idLogement);
            return new ResponseEntity<>(logement, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("HOTE")
    @DeleteMapping("/{idLogement}")
    public ResponseEntity<Object> deleteLogement(Principal principal, @PathVariable Long idLogement) throws LogementNotFoundException {
        Optional<Logement> logement = logementService.getLogementById(idLogement);
        String idProprietaire = logement.get().getIdProprietaire();
        if (Objects.equals(principal.getName(), idProprietaire))
        {
            try {
                logementService.deleteLogement(idLogement);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (LogementNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @RolesAllowed("HOTE")
    @PutMapping("/{idLogement}")
    public ResponseEntity<Object> updateLogement(
            Principal principal,
            @PathVariable Long idLogement,
            @RequestBody CreationLogementDto creationLogementDto
    ) throws LogementNotFoundException
    {
        Optional<Logement> logement = logementService.getLogementById(idLogement);
        String idProprietaire = logement.get().getIdProprietaire();
        if (Objects.equals(principal.getName(), idProprietaire))
        {
            List<Equipement> equipements = creationLogementDto
                    .idEquipements()
                    .stream()
                    .map(
                            id -> logementService.getEquipementById(id)
                    ).collect(Collectors.toList());
            List<Categorie> categories = creationLogementDto
                    .idCategories()
                    .stream()
                    .map(
                            id -> logementService.getCategorieById(id)
                    ).collect(Collectors.toList());

            Logement updateLogement = new Logement();
            updateLogement.setId(idLogement);
            updateLogement.setLibelle(creationLogementDto.libelle());
            updateLogement.setAddress(creationLogementDto.address());
            updateLogement.setIdProprietaire(idProprietaire);
            updateLogement.setEquipements(equipements);
            updateLogement.setCategories(categories);
            logementService.createOrUpdateLogement(updateLogement);
            return new ResponseEntity<>(updateLogement, new HttpHeaders(), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RolesAllowed({"ADMIN","HOTE"})
    @GetMapping("/proprietaire")
    public ResponseEntity<List<Logement>> getLogementsByProprietaire(@RequestParam String idProprietaire){
        List<Logement> logements = null;
        try {
            logements = logementService.getAllLogementsByIdProprietaire(idProprietaire);
            return new ResponseEntity<>(logements, new HttpHeaders(), HttpStatus.OK);
        } catch (LogementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RolesAllowed("HOTE")
    @PostMapping
    public ResponseEntity<Logement> createLogement(
            Principal principal,
            @RequestBody CreationLogementDto creationLogementDto
    )
    {
        String idProprietaire = principal.getName();
        List<Equipement> equipements = creationLogementDto
                .idEquipements()
                .stream()
                .map(
                        id -> logementService.getEquipementById(id)
                ).collect(Collectors.toList());
        List<Categorie> categories = creationLogementDto
                .idCategories()
                .stream()
                .map(
                        id -> logementService.getCategorieById(id)
                ).collect(Collectors.toList());


        Logement logement = new Logement();
        logement.setIdProprietaire(idProprietaire);
        logement.setLibelle(creationLogementDto.libelle());
        logement.setAddress(creationLogementDto.address());
        logement.setEquipements(equipements);
        logement.setCategories(categories);

        Logement nouveauLogement = logementService.createOrUpdateLogement(logement);
        return new ResponseEntity<>(nouveauLogement, new HttpHeaders(), HttpStatus.CREATED);
    }


    //@TODO find par idProprietaire


}
