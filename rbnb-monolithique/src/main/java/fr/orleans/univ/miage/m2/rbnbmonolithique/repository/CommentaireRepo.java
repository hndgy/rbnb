package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Commentaire;
import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentaireRepo extends MongoRepository<Commentaire, String> {

    Commentaire getCommentaireByReservation(Reservation reservation);
}
