package fr.orleans.univ.miage.m2.rbnblogementservice.service;

import fr.orleans.univ.miage.m2.rbnblogementservice.dto.LogementDto;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import fr.orleans.univ.miage.m2.rbnblogementservice.exception.LogementNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LogementService {

    Logement createOrUpdateLogement(Logement logement);

    Optional<Logement> getLogementById(Long idLogement) throws LogementNotFoundException;

//    LogementDto getLogementDetailById(Long idLogement) throws LogementNotFoundException;

    LogementDto getLogementDetailById(Long idLogement, String token) throws LogementNotFoundException;

    LogementDto getLogementDetailById(Long idLogement) throws LogementNotFoundException;

    List<Logement> getAllLogementsByIdProprietaire(String proprietaire) throws LogementNotFoundException;

//    Optional<Logement> getLogementByIdProprietaireAndId(String proprietaire, Long idLogement) throws LogementNotFoundException;

    List<Logement> findAllLogementByAddress(String address) throws LogementNotFoundException;

    List<Logement> findAllLogementByCity(String city) throws LogementNotFoundException;

    List<Logement> getAllLogements();

    void deleteLogement(Long id) throws LogementNotFoundException;


    Equipement getEquipementById(Long id);

    Categorie getCategorieById(Long id);

}
