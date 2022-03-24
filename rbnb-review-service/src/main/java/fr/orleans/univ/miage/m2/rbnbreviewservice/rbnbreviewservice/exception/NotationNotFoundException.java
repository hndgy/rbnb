package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception;

public class NotationNotFoundException extends Exception{
    public NotationNotFoundException(Long id){
        super("Notation "+ id + " not found");
    }
}
