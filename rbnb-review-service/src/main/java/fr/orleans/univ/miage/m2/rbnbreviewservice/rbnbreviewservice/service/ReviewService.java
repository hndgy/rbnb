package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewDto reviewDto);

    List<ReviewDto> getAllByLogement(Long idLogement);

    List<ReviewDto> getAllByUtilisateur(String idUtilisateur);

    void deleteById(Long id) throws ReviewNotFoundException;

    ReviewDto getReviewById(Long id) throws ReviewNotFoundException;
}
