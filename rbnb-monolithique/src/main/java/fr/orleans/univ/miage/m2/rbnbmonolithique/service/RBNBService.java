package fr.orleans.univ.miage.m2.rbnbmonolithique.service;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.*;

import java.util.Collection;
import java.util.Date;

public interface RBNBService {

    Utilisateur inscrire(Utilisateur utilisateur);

    Collection<Reservation> getReservationsByHote(Long idHote);

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur);

    Collection<Disponibilite> getDisponibilitesByLogement(Long idLogement);

    Collection<Disponibilite> getDisponibilitesByDatesAndLogement(Long idLogement, Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDate(Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDateAndVille(Date dateDebut, Date dateFin, String ville);

    Logement createLogement(Logement logement);

    Reservation createReservation(Reservation reservation);

    Disponibilite createDispobilite(Disponibilite disponibilite);

    Commentaire createCommentaire(Commentaire commentaire);

    void updateLogement(Logement logement);

    void updateReservation(Reservation reservation);

    void updateDisponibilite(Disponibilite disponibilite);

    void updateCommentaire(Commentaire commentaire);




}
