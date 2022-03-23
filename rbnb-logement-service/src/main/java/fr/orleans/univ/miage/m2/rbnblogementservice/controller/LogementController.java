package fr.orleans.univ.miage.m2.rbnblogementservice.controller;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.CreationLogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.dto.LogementDto;
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

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<Logement> createOrUpdateLogement(
            Principal principal,
            @RequestBody CreationLogementDto logementDto
    )
    {
        String idProprietaire = principal.getName();
        List<Equipement> equipements = logementDto
                .idEquipements()
                .stream()
                .map(
                        id -> logementService.getEquipementById(id)
                ).collect(Collectors.toList());
        List<Categorie> categories = logementDto
                .idCategories()
                .stream()
                .map(
                        id -> logementService.getCategorieById(id)
                ).toList();


        Logement logement = new Logement();
        logement.setIdProprietaire(idProprietaire);
        logement.setLibelle(logementDto.libelle());
        logement.setAddress(logementDto.address());
        logement.setEquipements(equipements);
        logement.setCategories(categories);

        Logement updated = logementService.createOrUpdateLogement(logement);
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

}
