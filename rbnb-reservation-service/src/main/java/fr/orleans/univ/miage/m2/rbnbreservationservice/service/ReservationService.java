package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.DisponibiliteDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.Logement;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.LogementDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.ReservationDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface ReservationService {
    HashMap<Logement, Collection<Reservation>> getReservationsByHote(Long idHote) throws ReservationIntrouvableException;

    Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) throws ReservationIntrouvableException;
    Reservation createReservation(ReservationDTO reservation, Principal principal) throws LogementsIndisponibleException, CapaciteLogementDepasseException, LogementIntrouvableException;
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs, String token) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException;
    void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException;
    void annulerReservation(String idReservation) throws ReservationIntrouvableException;

    Reservation getReservationsByIdReservation(String idReservation) throws ReservationIntrouvableException;

    Collection<Disponibilite> setDisponibilite(List<DisponibiliteDTO> disponibilitesDTO, String token) throws LogementIntrouvableException;
}
