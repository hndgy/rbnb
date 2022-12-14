package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface DisponibiliteRepo extends MongoRepository<Disponibilite, String> {

    Collection<Disponibilite> findAllDispoByIdLogement(Long idLogement);

    Disponibilite findByDateDebutAndIdLogement(Date dateDebut, Long idLogement);

    Disponibilite findByDateFinAndIdLogement(Date dateFin, Long idLogement);
}