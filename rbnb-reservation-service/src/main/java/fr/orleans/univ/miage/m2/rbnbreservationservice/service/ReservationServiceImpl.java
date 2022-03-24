package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;

import java.util.Collection;
import java.util.Date;

public class ReservationServiceImpl implements ReservationService {
    @Override
    public Collection<Reservation> getReservationsByHote(Long idHote) {
        return null;
    }

    @Override
    public Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) {
        return null;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return null;
    }

    @Override
    public void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException {

    }

    @Override
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin) throws LogementsIndisponibleException {

    }

    @Override
    public void annulerReservation(String idReservation) {

    }
}
