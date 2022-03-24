package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.exception;

public class PrestationNotFoundException extends Exception{
    public PrestationNotFoundException(Long id){
        super("Prestation "+ id + " not found");
    }
}
