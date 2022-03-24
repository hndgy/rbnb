package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Repository
public interface DisponibiliteRepo extends MongoRepository<Disponibilite, String> {

    Disponibilite findByIdLogement(Long idLogement);

   Collection<Disponibilite> findAllDispoById(Long idLogement);


    Disponibilite findByDateDebutAndIdLogement(Date dateDebut, Long idLogement);

    Disponibilite findByDateFinAndIdLogement(Date dateFin, Long idLogement);
}