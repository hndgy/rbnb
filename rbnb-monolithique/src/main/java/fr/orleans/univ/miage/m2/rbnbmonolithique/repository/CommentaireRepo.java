package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Commentaire;
import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CommentaireRepo extends JpaRepository<Commentaire, Long> {


    Commentaire getCommentaireByIdReservation(String idReservation);
}
