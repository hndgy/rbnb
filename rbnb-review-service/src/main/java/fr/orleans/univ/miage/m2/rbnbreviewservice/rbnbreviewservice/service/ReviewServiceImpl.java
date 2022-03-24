package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.LogementDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.NotationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.UtilisateurDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Prestation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.PrestationNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.NotationRepo;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.PrestationRepo;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.ReviewRepo;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepo reviewRepo;

    private final NotationRepo notationRepo;

    private final PrestationRepo prestationRepo;

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepo reviewRepo, NotationRepo notationRepo, PrestationRepo prestationRepo, RestTemplate restTemplate, DiscoveryClient discoveryClient, ModelMapper modelMapper) {
        this.reviewRepo = reviewRepo;
        this.notationRepo = notationRepo;
        this.prestationRepo = prestationRepo;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        reviewRepo.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public NotationDto createNotation(NotationDto notationDto) {
        Notation notation = modelMapper.map(notationDto, Notation.class);
        notationRepo.save(notation);
        return modelMapper.map(notation, NotationDto.class);
    }

    @Override
    public Prestation createPrestation(Prestation prestation) {
        return prestationRepo.save(prestation);
    }

    @Override
    public List<ReviewDto> getAllByLogement(Long idLogement) {
        return reviewRepo.findAllByIdLogement(idLogement)
                .stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getAllByUtilisateur(String idUtilisateur){
        return reviewRepo.findAllByIdUtilisateur(idUtilisateur)
                .stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public ReviewDto getReviewById(Long id) throws ReviewNotFoundException {
       Optional<Review> review = reviewRepo.findById(id);
       if (review.isPresent()){
           Review returnReview = review.get();
           ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
           String urlUtilisateur = "http://rbnb-utilisateur-service/Utilisateur/"+ returnReview.getIdUtilisateur();
           String urlLogement = "http://rbnb-logement-service/logement/"+ returnReview.getIdLogement();
           UtilisateurDto utilisateurDto = restTemplate.getForObject(urlUtilisateur, UtilisateurDto.class);
           LogementDto logementDto = restTemplate.getForObject(urlLogement, LogementDto.class);
           reviewDto.setUtilisateur(utilisateurDto);
           reviewDto.setLogement(logementDto);
           return reviewDto;
       }else {
           throw new ReviewNotFoundException(id);
       }
    }

    @Override
    public List<Notation> getAllByIdReview(Long idReview) {
        List<Notation> notations = notationRepo.findAllByReviewId(idReview);
        if (notations.size() > 0){
            return notations;
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Notation> getAllByIdPrestation(Long idPrestation) {
        List<Notation> notations = notationRepo.findAllByPrestationId(idPrestation);
        if (notations.size() > 0){
            return notations;
        }else {
            return new ArrayList<>();
        }
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
    public void deletePrestationById(Long id) throws PrestationNotFoundException {
        Optional<Prestation> prestation = prestationRepo.findById(id);
        if (prestation.isPresent()){
            prestationRepo.deleteById(id);
        }else {
            throw new PrestationNotFoundException(id);
        }
    }
}
