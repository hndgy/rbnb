package fr.orleans.univ.miage.interop.compteservice.service;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;

import java.util.Collection;

public interface FacadeCompte {

    Compte saveCompte(Compte compte);

    Compte findCompteByIdCompte(Long idCompte);

    Collection<Compte> findComptesByIdUser(String idUser);

    Compte updateCompte(Compte compte) throws CompteIntrouvableException;

    void deleteCompteByIdCompte(Long idCompte);

    void deleteComptesByIdUser(String idUser);

}
