package fr.orleans.univ.miage.interop.compteservice.controller;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;
import fr.orleans.univ.miage.interop.compteservice.service.FacadeCompte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping(path="/account", produces="application/json")
public class CompteController {
    /*
    idCompte;
= "id_user", n
g idUser;
= "libelle")
g libelleCompt
numType.STRING
= "type")
g typeCompte;
     */
    @Autowired
    private FacadeCompte facadeCompte;

    @PostMapping(value = "/compte")
    public ResponseEntity<Object> createCompte(@RequestParam String idOwner, @RequestParam String libelle, @RequestParam String type) {
        try {
            Compte compte = new Compte(idOwner, libelle, type);  //generer un idCompte ?

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(compte.getIdCompte())
                    .toUri();
            return ResponseEntity.created(location).header("idCompte",compte.getIdCompte().toString()).build();
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/compte/{idCompte}")
    public ResponseEntity<Object> getCompte (@PathVariable Long idCompte) {
        try {
            Compte compte = facadeCompte.findCompteByIdCompte(idCompte);
            return ResponseEntity.ok(compte);
        }
        catch (CompteIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
