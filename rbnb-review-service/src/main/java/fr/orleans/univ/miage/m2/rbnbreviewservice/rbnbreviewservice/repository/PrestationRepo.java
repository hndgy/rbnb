package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestationRepo extends JpaRepository<Prestation, Long> {
}
