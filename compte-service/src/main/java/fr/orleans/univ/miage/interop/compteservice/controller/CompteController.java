package fr.orleans.univ.miage.interop.compteservice.controller;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;
import fr.orleans.univ.miage.interop.compteservice.service.FacadeCompte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path="/account", produces="application/json")
public class CompteController {

    @Autowired
    private FacadeCompte facadeCompte;

    @PostMapping(value = "/compte")
    public ResponseEntity<Object> createCompte(@RequestParam String idOwner, @RequestParam String libelle, @RequestParam String type) throws CompteIntrouvableException {
        try {
            Compte compte = facadeCompte.saveCompte(new Compte(idOwner, libelle, type));  //TODO : generer un idCompte ?
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(compte.getIdCompte())
                    .toUri();
            return ResponseEntity.created(location).header("idCompte",compte.getIdCompte().toString()).header("idOwner",compte.getIdOwner()).build();
        }
        catch (Exception e) {
            System.out.println(facadeCompte.findComptesByIdOwner(idOwner).stream().toList().toString());
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

    @GetMapping(value = "/{idOwner}/compte") // TODO : revoir le mapping
    public ResponseEntity<Object> getComptes (@PathVariable String idOwner) {
        try {
            Collection<Compte> comptes = facadeCompte.findComptesByIdOwner(idOwner);
            return ResponseEntity.ok(comptes);
        }
        catch (CompteIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/compte/{idCompte}")
    public ResponseEntity<Object> udpadteCompte(@PathVariable Long idCompte, @RequestParam String libelle, @RequestParam String type) {
        try {
            Compte compte = facadeCompte.findCompteByIdCompte(idCompte);
            compte.setTypeCompte(type);
            compte.setLibelleCompte(libelle);  // TODO : modifier la méthode update ?
            Compte compteUpdate = facadeCompte.updateCompte(compte);
            return ResponseEntity.ok(compteUpdate);
        }
        catch (CompteIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/compte/{idCompte}")
    public ResponseEntity<Object> deleteCompte (@PathVariable Long idCompte) {
        try {
            facadeCompte.deleteCompteByIdCompte(idCompte);
            return ResponseEntity.ok().build();
        }
        catch (CompteIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{idUser}/compte")
    public ResponseEntity<Object> deleteComptes (@PathVariable String idUser) {
        try {
            facadeCompte.deleteComptesByIdOwner(idUser);
            return ResponseEntity.ok().build();
        }
        catch (CompteIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
