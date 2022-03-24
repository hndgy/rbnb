package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.DisponibiliteRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.ReservationRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.ReservationIntrouvableException;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final DisponibiliteRepo disponibiliteRepo;

    public ReservationServiceImpl(ReservationRepo reservationRepo, DisponibiliteRepo disponibiliteRepo) {
        this.reservationRepo = reservationRepo;
        this.disponibiliteRepo = disponibiliteRepo;
    }

    @Override
    public Collection<Reservation> getReservationsByHote(Long idHote) {
        return null;
    }

    @Override
    public Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) {
        return reservationRepo.findAllReservationsByIdClient(idVoyageur);
    }


    @Override
    public Reservation createReservation(Reservation reservation) {
        return null;
    }

    @Override
    public void updateNbVoyageursReservation(String idReservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException {
        Optional<Reservation> reservation;
        reservation = reservationRepo.findById(idReservation);
        if (reservation.isPresent()) {
            reservation.get().setNbVoyageurs(nbVoyageurs);
            reservationRepo.save(reservation.get());
        }
        else throw new ReservationIntrouvableException();
    }

    @Override
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin) throws LogementsIndisponibleException {

    }

    @Override
    public void annulerReservation(String idReservation) throws ReservationIntrouvableException {
        Optional<Reservation> reservation;
        reservation = reservationRepo.findById(idReservation);
        if (reservation.isPresent()) {
            reservationRepo.deleteById(idReservation);
        }
        else throw new ReservationIntrouvableException();
    }
}
