package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepo reviewRepo;

    @Override
    public Review createReview(Review review) {
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getAllByLogement(Long idLogement) {
        return reviewRepo.findAllByLogement(idLogement);
    }

    public List<Review> getAllByUtilisateur(Long idUtilisateur){
        return reviewRepo.findAllByUtilisateur(idUtilisateur);
    }

    @Override
    public List<Review> getAllByReservation(String idReservation) {
        return reviewRepo.findAllByIdReservation(idReservation);
    }

    @Override
    public void deleteById(Long id) throws ReviewNotFoundException {
        Optional<Review> review = reviewRepo.findById(id);
        if (review.isPresent()){
            reviewRepo.deleteById(id);
        }else {
            throw new ReviewNotFoundException(id);
        }
    }

    @Override
    public Review getReviewById(Long id) throws ReviewNotFoundException {
       Optional<Review> review = reviewRepo.findById(id);
       if (review.isPresent()){
           return review.get();
       }else {
           throw new ReviewNotFoundException(id);
       }
    }
}
