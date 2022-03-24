package fr.orleans.univ.miage.m2.rbnblogementservice.service;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;

import java.util.List;

public interface EquipementService {

    Equipement createEquipement(Equipement equipement);

    List<Equipement> getAllEquipements();

    void deleteEquipement(Long id);
}
