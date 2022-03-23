package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.controller;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable(name = "id")Long id) throws ReviewNotFoundException {
        ReviewDto reviewDto = reviewService.getReviewById(id);
        return ResponseEntity.ok().body(reviewDto);
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto){
        ReviewDto review = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(review,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable(name = "id") Long id) throws ReviewNotFoundException {
        reviewService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed("USER")
    @GetMapping("/utilisateur")
    public ResponseEntity<List<ReviewDto>> getAllReviewByUtilisateur(Principal principal){
        return ResponseEntity.ok().body(reviewService.getAllByUtilisateur(principal.getName()));
    }

    @GetMapping("/logement/{id}")
    public ResponseEntity<List<ReviewDto>> getAllReviewByLogement(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().body(reviewService.getAllByLogement(id));
    }



}
