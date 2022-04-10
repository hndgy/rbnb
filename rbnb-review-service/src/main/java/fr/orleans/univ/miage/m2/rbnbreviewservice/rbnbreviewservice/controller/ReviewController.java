package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.controller;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.NotationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewCreationDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Prestation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.PrestationNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/review")
public class ReviewController {

    private ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RolesAllowed("USER")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable(name = "id")Long id, @RequestHeader(name = "Authorization")String token) throws ReviewNotFoundException {
        ReviewDto reviewDto = reviewService.getReviewById(id, token);
        return ResponseEntity.ok().body(reviewDto);
    }

    @RolesAllowed({"USER"})
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewCreationDto reviewCreationDto){
        ReviewDto reviewDto = reviewService.createReview(reviewCreationDto);
        return new ResponseEntity<>(reviewDto,HttpStatus.CREATED);
    }
    @RolesAllowed("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable(name = "id") Long id) throws ReviewNotFoundException {
        reviewService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/prestation/{id}")
    public ResponseEntity<HttpStatus> deletePrestation(@PathVariable(name = "id") Long id) throws PrestationNotFoundException {
        reviewService.deletePrestationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({"USER"})
    @GetMapping("/utilisateur")
    public ResponseEntity<List<ReviewDto>> getAllReviewByUtilisateur(Principal principal){
        return ResponseEntity.ok().body(reviewService.getAllByUtilisateur(principal.getName()));
    }

    @RolesAllowed({"HOTE"})
    @GetMapping("/logement/{id}")
    public ResponseEntity<List<ReviewDto>> getAllReviewByLogement(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().body(reviewService.getAllByLogement(id));
    }



    @RolesAllowed({"USER"})
    @GetMapping("/prestations")
    public ResponseEntity<List<Prestation>> getAllPrestations(){
        List<Prestation> prestations = reviewService.getAllPrestation();
        return new ResponseEntity<>(prestations, HttpStatus.OK);
    }


}
