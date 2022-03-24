package fr.orleans.univ.miage.m2.rbnblogementservice.dto;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public record LogementDto(
        String libelle,
        String address,
        int nbVoyageurs,
        Set<Image> images,
        List<Equipement> equipements,
        List<Categorie> categories
) {}
