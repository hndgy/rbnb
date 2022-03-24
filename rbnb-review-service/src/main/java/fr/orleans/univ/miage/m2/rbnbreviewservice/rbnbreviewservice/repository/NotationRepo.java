package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotationRepo extends JpaRepository<Notation, Long> {

    List<Notation> findAllByReviewId(Long id);
    List<Notation> findAllByPrestationId(Long id);
}
