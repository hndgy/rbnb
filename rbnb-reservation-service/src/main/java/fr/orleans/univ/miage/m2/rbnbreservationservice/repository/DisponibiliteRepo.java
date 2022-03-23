package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "disponibilite")
public interface DisponibiliteRepo extends MongoRepository<Disponibilite, String> {

    Disponibilite findByIdLogement(Long idLogement);
}