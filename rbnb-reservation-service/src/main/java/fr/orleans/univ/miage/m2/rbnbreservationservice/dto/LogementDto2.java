package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import java.util.List;
import java.util.Set;

public record LogementDto2(
        String libelle,
        String address,
        int nbVoyageurs,
        String utilisateurDto,
        Set<Image> images,
        List<Equipement> equipements,
        List<Categorie> categories
) {}
