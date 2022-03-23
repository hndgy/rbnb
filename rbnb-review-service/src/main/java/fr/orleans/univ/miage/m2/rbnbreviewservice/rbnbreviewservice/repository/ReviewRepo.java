package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findAllByIdReservation(String id);
    List<Review> findAllByUtilisateur(Long idUtilisateur);
    List<Review> findAllByLogement(Long idLogement);
}
