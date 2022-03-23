package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.LogementDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.UtilisateurDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.ReviewRepo;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepo reviewRepo;

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepo reviewRepo, RestTemplate restTemplate, DiscoveryClient discoveryClient, ModelMapper modelMapper) {
        this.reviewRepo = reviewRepo;
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
    public void deleteById(Long id) throws ReviewNotFoundException {
        Optional<Review> review = reviewRepo.findById(id);
        if (review.isPresent()){
            reviewRepo.deleteById(id);
        }else {
            throw new ReviewNotFoundException(id);
        }
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
}
