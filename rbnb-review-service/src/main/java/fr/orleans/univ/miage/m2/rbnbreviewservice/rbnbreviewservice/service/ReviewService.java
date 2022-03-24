package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.NotationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Prestation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.NotationNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.PrestationNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import java.util.List;

public interface ReviewService {

    //create
    ReviewDto createReview(ReviewDto reviewDto);

    NotationDto createNotation(NotationDto notationDto);

    Prestation createPrestation(Prestation prestation);

    //getters
    List<ReviewDto> getAllByLogement(Long idLogement);

    List<ReviewDto> getAllByUtilisateur(String idUtilisateur);

    ReviewDto getReviewById(Long id) throws ReviewNotFoundException;

    List<Notation> getAllByIdReview(Long idReview);

    List<Notation> getAllByIdPrestation(Long idPrestation);

    //delete
    void deleteById(Long id) throws ReviewNotFoundException;

    void deletePrestationById(Long id) throws PrestationNotFoundException;
}
