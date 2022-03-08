package fr.orleans.univ.miage.interop.compteservice.service;

import fr.orleans.univ.miage.interop.compteservice.dto.ResponseTemplateVO;
import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;

import java.util.Collection;

public interface FacadeCompte {

    Compte saveCompte(Compte compte);

    Compte findCompteByIdCompte(Long idCompte) throws CompteIntrouvableException;

    Collection<Compte> findComptesByIdOwner(String idUser) throws CompteIntrouvableException;

    Compte updateCompte(Compte compte) throws CompteIntrouvableException;

    void deleteCompteByIdCompte(Long idCompte) throws CompteIntrouvableException;

    void deleteComptesByIdOwner(String idUser) throws CompteIntrouvableException;

    //ResponseTemplateVO findTransactionsByIdCompte(Long idCompte);
}
