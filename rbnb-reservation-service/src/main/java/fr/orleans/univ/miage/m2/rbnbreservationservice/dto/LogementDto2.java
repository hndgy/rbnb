package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LogementDto2 {
    private String libelle;
    private String address;
    private int nbVoyageurs;
    private String utilisateurDto;
    private Set<Image> images;
    private List<Equipement> equipements;
    private List<Categorie> categories;
}

