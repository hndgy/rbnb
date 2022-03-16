package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Disponibilite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DisponibiliteRepo extends MongoRepository<Disponibilite, String> {

    Disponibilite getDisponibiliteByIdLogement(Long idLogement);
}
