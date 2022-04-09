package fr.orleans.univ.miage.m2.rbnblogementservice.dto;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Image;

import java.util.List;
import java.util.Set;

public record LogementDto(
        String libelle,
        String address,
        String city,
        int nbVoyageurs,
        UtilisateurDto utilisateurDto,
        Set<Image> images,
        List<Equipement> equipements,
        List<Categorie> categories
) {}
