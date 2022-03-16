package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Commentaire;
import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CommentaireRepo extends MongoRepository<Commentaire, String> {

    @Query(value = "{ }")
    Commentaire getCommentaireByReservation(String idReservation);
}
