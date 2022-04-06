package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.Logement;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.LogementDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.ReservationDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.CapaciteLogementDepasseException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.ReservationIntrouvableException;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;


public interface ReservationService {
    HashMap<Logement, Collection<Reservation>> getReservationsByHote(Long idHote) throws ReservationIntrouvableException;

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) throws ReservationIntrouvableException;
    Reservation createReservation(ReservationDTO reservation, Principal principal) throws LogementsIndisponibleException, CapaciteLogementDepasseException;
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException, CapaciteLogementDepasseException;
    void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException;
    void annulerReservation(String idReservation) throws ReservationIntrouvableException;

    Reservation getReservationsByIdReservation(String idReservation) throws ReservationIntrouvableException;
}
