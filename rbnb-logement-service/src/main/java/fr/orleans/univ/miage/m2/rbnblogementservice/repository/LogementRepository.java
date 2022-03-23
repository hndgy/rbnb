package fr.orleans.univ.miage.m2.rbnblogementservice.repository;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogementRepository extends JpaRepository<Logement, Long> {

    List<Logement> findLogementsByIdProprietaire(Long idProprietaire);

    List<Logement> findLogementsByAddress(String address);

}
