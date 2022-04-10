package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.mapper;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.*;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.PrestationRepo;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.ReviewRepo;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private PrestationRepo prestationRepo;

    public Mapper(PrestationRepo prestationRepo) {
        this.prestationRepo = prestationRepo;
    }

    public Review reviewCreationToReview(ReviewCreationDto reviewCreationDto){
        Review review = new Review();
        var notations = reviewCreationDto.getNotations().stream().map(
                n -> {
                    Notation notation = new Notation();
                    var prestation = prestationRepo.findById(n.getIdPrestation());
                    notation.setPrestation(prestation.get());
                    notation.setEtoile(n.getEtoile());
                    notation.setReview(review);
                    return notation;
                }
        ).toList();
        review.setDate(reviewCreationDto.getDate());
        review.setContenu(reviewCreationDto.getContenu());
        review.setIdLogement(reviewCreationDto.getIdLogement());
        review.setIdUtilisateur(reviewCreationDto.getIdUtilisateur());
        review.setNotations(notations);
        return review;
    }

    public ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setDate(review.getDate());
        reviewDto.setContenu(review.getContenu());
        var notationsDto = review.getNotations().stream().map(
                notation -> {
                    var notationDto = new NotationDto();
                    notationDto = notationToNotationDto(notation);
                    notationDto.setId(notation.getId());
                    return notationDto;
                }
        ).toList();
        reviewDto.setNotations(notationsDto);
        return reviewDto;
    }

    public NotationDto notationToNotationDto(Notation notation){
        NotationDto notationDto = new NotationDto();
        notationDto.setEtoile(notation.getEtoile());
        notationDto.setPrestation(notation.getPrestation().getLibelle());
        return notationDto;
    }
}
