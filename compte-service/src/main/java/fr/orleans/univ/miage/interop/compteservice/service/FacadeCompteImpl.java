package fr.orleans.univ.miage.interop.compteservice.service;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.repository.CompteRepository;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacadeCompteImpl implements FacadeCompte{

    public CompteRepository compteRepository;

    @Override
    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    public Compte findCompteByIdCompte(Long idCompte) {
        return compteRepository.findCompteByIdCompte(idCompte);
    }


    @Override
    public Collection<Compte> findComptesByIdUser(String idUser) {
        return compteRepository.findComptesByIdUser(idUser);
    }

    @Override
    public Compte updateCompte(Compte compte) throws CompteIntrouvableException {
        Optional<Compte> compte1 = compteRepository.findById(compte.getIdCompte());
        if (compte1.isPresent()){
            compte1.get().setIdCompte(compte.getIdCompte());
            compte1.get().setTypeCompte(compte.getTypeCompte());
            compte1.get().setLibelleCompte(compte.getLibelleCompte());

            return (compteRepository.save(compte1.get()));
        }
        throw new CompteIntrouvableException();
    }

    @Override
    public void deleteCompteByIdCompte(Long idCompte) {
        compteRepository.deleteCompteByIdCompte(idCompte);
    }

    @Override
    public void deleteComptesByIdUser(String idUser) {
        compteRepository.deleteComptesByIdUser(idUser);
    }
}
