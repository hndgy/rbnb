package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception;

public class ReviewNotFoundException extends Exception {
    public ReviewNotFoundException(Long id){
        super("Review "+ id + " not found");
    }
}
