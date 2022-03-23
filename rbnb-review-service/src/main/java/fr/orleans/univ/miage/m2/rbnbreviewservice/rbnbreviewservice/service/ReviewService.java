package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    Review createReview(Review review);

    List<Review> getAllByLogement(Long idLogement);

    List<Review> getAllByUtilisateur(Long idUtilisateur);

    List<Review> getAllByReservation(String idReservation);

    void deleteById(Long id) throws ReviewNotFoundException;

    Review getReviewById(Long id) throws ReviewNotFoundException;
}
