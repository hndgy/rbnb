package fr.orleans.univ.miage.interop.compteservice.service;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;

import java.util.Collection;

public interface FacadeCompte {

    Compte saveCompte(Compte compte);

    Compte findCompteByIdCompte(Long idCompte);

    Compte findCompteByIdUser(String idUser);

    Collection<Compte> findComptesByIdUser(String idUser);

    Compte createOrUpdateCompte(Compte compte);

    void deleteCompteByIdCompte(Long idCompte);

    void deleteComptesByIdUser(String idUser);




}
