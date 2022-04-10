package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import lombok.Data;

@Data
public class UtilisateurDto {

    private String id;
    private String username;
    private String nom;
    private String prenom;
    private String mail;
    private String bio;
    private String photoUrl;

}
