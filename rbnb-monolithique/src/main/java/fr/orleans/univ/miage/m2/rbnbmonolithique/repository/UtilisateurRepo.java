package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Long> {

    Utilisateur getUtilisateurById(Long id);

}
