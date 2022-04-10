package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.DisponibiliteDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.Logement;
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
    HashMap<Logement, Collection<Reservation>> getReservationsByHote(String idHote, String token) throws ReservationIntrouvableException, LogementIntrouvableException;

    Collection<Reservation> getReservationsByVoyageur(String idVoyageur) throws ReservationIntrouvableException;
    Reservation createReservation(ReservationDTO reservation, Principal principal, String token) throws LogementsIndisponibleException, CapaciteLogementDepasseException, LogementIntrouvableException, UtilisateurInexistantException;
    void updateNbVoyageursReservation(String idRerservation, int nbVoyageurs, String token) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException;
    void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal, String token) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException, UtilisateurInexistantException;
    void annulerReservation(String idReservation) throws ReservationIntrouvableException;

    Reservation getReservationsByIdReservation(String idReservation) throws ReservationIntrouvableException;

    Collection<Disponibilite> setDisponibilite(Long idLogement, List<DisponibiliteDTO> disponibilitesDTO, String token, Principal principal) throws LogementIntrouvableException, UtilisateurInexistantException, LogementsDejaDisponibleException;

    void deleteDispoEtReservationWhenHostDeleted(String idHote, String token) throws ReservationIntrouvableException, LogementIntrouvableException;

    void deleteDispoEtReservationWhenClientDeleted(String idHote) throws ReservationIntrouvableException;

    void deleteReservationClientByClient(String idClient) throws ReservationIntrouvableException;

    void deleteReservationClientByHote(String idReservation) throws ReservationIntrouvableException;

    void deleteReservationClientWhenLogementDeleted(Long idLogement) throws ReservationIntrouvableException;
}