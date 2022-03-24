package fr.orleans.univ.miage.m2.rbnblogementservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EquipementNotFoundException extends Throwable {
    public EquipementNotFoundException(String s) {
    }
}
