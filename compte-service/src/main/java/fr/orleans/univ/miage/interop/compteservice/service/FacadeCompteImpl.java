package fr.orleans.univ.miage.interop.compteservice.service;

import fr.orleans.univ.miage.interop.compteservice.dto.ResponseTemplateVO;
import fr.orleans.univ.miage.interop.compteservice.dto.Transaction;
import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import fr.orleans.univ.miage.interop.compteservice.repository.CompteRepository;
import fr.orleans.univ.miage.interop.compteservice.service.Exception.CompteIntrouvableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class FacadeCompteImpl implements FacadeCompte{

    @Autowired
    public CompteRepository compteRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    public Compte findCompteByIdCompte(Long idCompte) throws CompteIntrouvableException {
        Optional<Compte> compte1 = compteRepository.findById(idCompte);
        if (compte1.isPresent()){
            return compteRepository.findCompteByIdCompte(idCompte);
        }
        throw new CompteIntrouvableException();
    }


    @Override
    public Collection<Compte> findComptesByIdOwner(String idUser) throws CompteIntrouvableException {
        Collection<Compte> compte1 = compteRepository.findComptesByIdOwner(idUser);
        if (!compte1.isEmpty()){
            return compteRepository.findComptesByIdOwner(idUser);
        }
        throw new CompteIntrouvableException();
    }

    @Override
    public Compte updateCompte(Compte compte) throws CompteIntrouvableException {
        Optional<Compte> compte1 = compteRepository.findById(compte.getIdCompte());
        if (compte1.isPresent()){
            compte1.get().setTypeCompte(compte.getTypeCompte());
            compte1.get().setLibelleCompte(compte.getLibelleCompte());
            return (compteRepository.save(compte1.get()));
        }
        throw new CompteIntrouvableException();
    }

    @Override
    public void deleteCompteByIdCompte(Long idCompte) throws CompteIntrouvableException {
        Optional<Compte> compte1 = compteRepository.findById(idCompte);
        if (compte1.isPresent()){
            compteRepository.deleteCompteByIdCompte(idCompte);
        }
        else
            throw new CompteIntrouvableException();
    }

    @Override
    public void deleteComptesByIdOwner(String idUser) throws CompteIntrouvableException {
        Collection<Compte> compte1 = compteRepository.findComptesByIdOwner(idUser);
        if (!compte1.isEmpty()){
            compteRepository.deleteComptesByIdOwner(idUser);
        }
        else
            throw new CompteIntrouvableException();
    }


    @Override
    public ResponseTemplateVO findCompteWithTransactions(Long idCompte) {
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Compte compte = compteRepository.findCompteByIdCompte(idCompte);

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("http://transaction-service/transaction/" + idCompte, Transaction[].class);
        Transaction [] transactionArray = response.getBody();
        Collection<Transaction> transactions = Arrays.stream(transactionArray).toList();
        //TODO : ????????

        vo.setCompte(compte);
        vo.setTransactions(transactions);

        return vo;
    }


}
