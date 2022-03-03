package fr.orleans.univ.miage.interop.compteservice.repository;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CompteRepository extends JpaRepository<Compte,Long> {

    Compte findCompteByIdCompte(Long idCompte);
    Collection<Compte> findComptesByIdOwner(String idOwner);

    Compte deleteCompteByIdCompte(Long idCompte);
    Collection<Compte> deleteComptesByIdOwner(String idOwner);
}
