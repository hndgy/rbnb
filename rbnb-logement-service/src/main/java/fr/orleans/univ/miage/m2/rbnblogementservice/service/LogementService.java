package fr.orleans.univ.miage.m2.rbnblogementservice.service;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import fr.orleans.univ.miage.m2.rbnblogementservice.exception.LogementNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LogementService {

    Logement createOrUpdateLogement(Logement logement);

    Optional<Logement> getLogementById(Long idLogement) throws LogementNotFoundException;

    List<Logement> getAllLogementsByProprietaire(Long proprietaire) throws LogementNotFoundException;

    List<Logement> findAllLogementByAddress(String address) throws LogementNotFoundException;

    List<Logement> getAllLogements();

    void deleteLogement(Long id) throws LogementNotFoundException;


    Equipement getEquipementById(Long id);

    Categorie getCategorieById(Long id);

}
