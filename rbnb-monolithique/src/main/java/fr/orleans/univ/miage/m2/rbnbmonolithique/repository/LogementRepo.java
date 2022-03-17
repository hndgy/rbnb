package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogementRepo extends JpaRepository<Logement, Long> {
}
