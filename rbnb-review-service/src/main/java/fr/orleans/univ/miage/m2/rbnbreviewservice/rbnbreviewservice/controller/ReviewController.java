package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.controller;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto.ReviewDto;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Review;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception.ReviewNotFoundException;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    public ModelMapper modelMapper;

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable(name = "id")Long id) throws ReviewNotFoundException {
        Review review = reviewService.getReviewById(id);
        ReviewDto returnReview = modelMapper.map(review, ReviewDto.class);
        return ResponseEntity.ok().body(returnReview);
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto){
        Review reviewPost = modelMapper.map(reviewDto, Review.class);

        Review review = reviewService.createReview(reviewPost);

        ReviewDto reviewDtoResponse = modelMapper.map(review, ReviewDto.class);

        return new ResponseEntity<>(reviewDtoResponse,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable(name = "id") Long id) throws ReviewNotFoundException {
        reviewService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
