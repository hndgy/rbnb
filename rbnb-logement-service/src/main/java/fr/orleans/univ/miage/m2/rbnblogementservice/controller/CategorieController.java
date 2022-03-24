package fr.orleans.univ.miage.m2.rbnblogementservice.controller;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.CategorieDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.service.CategorieService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/categorie")
public class CategorieController {

    CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<Categorie> createCategorie(@RequestBody CategorieDto categorieDto){
        Categorie categorie = new Categorie();
        categorie.setNom(categorieDto.nom());
        Categorie nouvelle = categorieService.createCategorie(categorie);
        return new ResponseEntity<>(nouvelle, new HttpHeaders(), HttpStatus.CREATED);
    }

    @RolesAllowed({"ADMIN","HOTE","USER"})
    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories(){
        List<Categorie> categories = null;
        categories = categorieService.getAllCategories();
        return new ResponseEntity<>(categories, new HttpHeaders(), HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{idCategorie}")
    public ResponseEntity<Object> deleteCategorie(
            @PathVariable Long idCategorie
    ) {
        categorieService.deleteCategorie(idCategorie);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
