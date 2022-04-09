package fr.orleans.univ.miage.m2.rbnblogementservice.dto;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Image;

import java.util.List;
import java.util.Set;

public record CreationLogementDto(
        String libelle,
        String address,
        String city,
        int nbVoyageurs,
        Set<Image> images,
        List<Long> idEquipements,
        List<Long> idCategories
) {

}
