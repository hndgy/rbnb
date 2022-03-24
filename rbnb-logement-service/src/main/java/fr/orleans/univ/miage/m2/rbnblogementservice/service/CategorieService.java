package fr.orleans.univ.miage.m2.rbnblogementservice.service;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;

import java.util.List;

public interface CategorieService {

    Categorie createCategorie(Categorie categorie);

    List<Categorie> getAllCategories();

    void deleteCategorie(Long id);

}
