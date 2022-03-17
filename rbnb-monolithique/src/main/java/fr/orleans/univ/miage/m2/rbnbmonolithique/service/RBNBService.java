package fr.orleans.univ.miage.m2.rbnbmonolithique.service;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.*;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.LogementIntrouvableException;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.NbVoyagageurIncorrecteException;

import java.util.Collection;
import java.util.Date;

public interface RBNBService {

    // UTILISATEUR
    Utilisateur inscrire(Utilisateur utilisateur);

    //RESERVATION
    Collection<Reservation> getReservationsByHote(Long idHote);

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur);
    Reservation createReservation(Reservation reservation);
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException;
    void updateDateReservation(String idReservation,Date dateDebut, Date dateFin) throws LogementsIndisponibleException;
    //void updateReservation(Reservation reservation);

    //LOGEMENT
    Collection<Disponibilite> getDisponibilitesByLogement(Long idLogement);

    Collection<Disponibilite> getDisponibilitesByDatesAndLogement(Long idLogement, Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDate(Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDateAndVille(Date dateDebut, Date dateFin, String ville);

    Logement createLogement(Logement logement);

    Disponibilite createDispobilite(Disponibilite disponibilite);

    void updateLogement(Logement logement);


    void updateDisponibilite(Disponibilite disponibilite);




    //COMMENTAIRE
    Collection<Commentaire> getAllCommentairesByLogement(Long idLogement) throws LogementIntrouvableException;

    Commentaire createCommentaire(Commentaire commentaire);

    void updateCommentaire(Commentaire commentaire);





}
