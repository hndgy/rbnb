package fr.orleans.univ.miage.m2.rbnbmonolithique.service;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.NbVoyagageurIncorrecteException;

import java.util.Collection;
import java.util.Date;

public interface ReservationService {
    Collection<Reservation> getReservationsByHote(Long idHote);

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur);
    Reservation createReservation(Reservation reservation);
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException;
    void updateDateReservation(String idReservation, Date dateDebut, Date dateFin) throws LogementsIndisponibleException;
    void annulerReservation(String idReservation);
}
