package fr.orleans.univ.miage.m2.rbnblogementservice.repository;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogementRepository extends JpaRepository<Logement, Long> {

    List<Logement> findLogementsByIdProprietaire(String idProprietaire);
    Logement findLogementByIdProprietaireAndId(String idProprietaire, Long idLogement);

    List<Logement> findLogementsByAddress(String address);
    List<Logement> findLogementsByCity(String city);
    List<Logement> findLogementsByCityAndNbVoyageursGreaterThanEqual(String city, int nbVoyageurs);
    List<Logement> findLogementsByNbVoyageursGreaterThanEqual(int nbVoyageurs);

}
