package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.ReservationIntrouvableException;

import java.util.Collection;
import java.util.Date;


public interface ReservationService {
    Collection<Reservation> getReservationsByHote(Long idHote);

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur);
    Reservation createReservation(Reservation reservation) throws LogementsIndisponibleException;
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException;
    void updateDateReservation(String idReservation, Date dateDebut, Date dateFin) throws LogementsIndisponibleException, ReservationIntrouvableException;
    void annulerReservation(String idReservation) throws ReservationIntrouvableException;

    Reservation getReservationsByIdReservation(String idReservation);
}
