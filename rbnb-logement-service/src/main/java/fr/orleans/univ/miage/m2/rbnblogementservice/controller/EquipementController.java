package fr.orleans.univ.miage.m2.rbnblogementservice.controller;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.CategorieDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.dto.EquipementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.service.EquipementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/equipement")
public class EquipementController {

    private EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<Equipement> createEquipement(@RequestBody EquipementDto equipementDto){
        Equipement equipement = new Equipement();
        equipement.setNom(equipementDto.nom());
        Equipement nouveau = equipementService.createEquipement(equipement);
        return new ResponseEntity<>(nouveau, new HttpHeaders(), HttpStatus.CREATED);
    }

    @RolesAllowed({"ADMIN","HOTE","USER"})
    @GetMapping
    public ResponseEntity<List<Equipement>> getAllEquipements(){
        List<Equipement> equipements = null;
        equipements = equipementService.getAllEquipements();
        return new ResponseEntity<>(equipements, new HttpHeaders(), HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{idEquipement}")
    public ResponseEntity<Object> deleteEquipement(
            @PathVariable Long idEquipement
    ) {
        equipementService.deleteEquipement(idEquipement);
        return ResponseEntity.status(HttpStatus.OK).build();
    }




}
