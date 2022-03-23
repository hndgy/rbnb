package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private long id;
    private String contenu;
    private Date date;
    private LogementDto logement;
    private UtilisateurDto utilisateur;
}
