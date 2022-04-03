package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.mapper;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.NotationCreationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewCreationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.UtilisateurDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;

public class Mapper {

  /*  public static Notation notationCreationDtoToNotation(NotationCreationDto notationCreationDto){
        Notation notation = new Notation();
        notation.setEtoile(notationCreationDto.getEtoile());
        notation.setPrestation(notationCreationDto.getIdPrestation());
    }*/
    public static Review reviewCreationToReview(ReviewCreationDto reviewCreationDto){
        Review review = new Review();
        review.setDate(reviewCreationDto.getDate());
        review.setContenu(reviewCreationDto.getContenu());
        review.setIdLogement(reviewCreationDto.getIdLogement());
        review.setIdUtilisateur(reviewCreationDto.getIdUtilisateur());
        return review;
    }

    public static ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setDate(review.getDate());
        reviewDto.setContenu(review.getContenu());
        return reviewDto;
    }

}
