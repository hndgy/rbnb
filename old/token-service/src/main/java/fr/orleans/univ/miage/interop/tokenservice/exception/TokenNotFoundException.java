package fr.orleans.univ.miage.interop.tokenservice.exception;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException(String id){
        super("Token "+ id + " not found");
    }
}
