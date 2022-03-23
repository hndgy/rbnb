package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.ReservationRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class ReservationServiceImpl implements ReservationService{

    //TODO : Autowired + gestion indisponibilité + tester

    private ReservationRepo reservationRepo;

    @Override
    public Collection<Reservation> getReservationsByHote(Long idHote) {
        return reservationRepo.findAllReservationsByIdHost(idHote);
    }

    @Override
    public Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) {
        return reservationRepo.findAllReservationsByIdVoyageur(idVoyageur);
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepo.save(reservation);
    }

    @Override
    public void updateNbVoyageursReservation(String idReservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException {
        Reservation reservation = null;
        if (reservationRepo.findById(idReservation).isPresent()){
            reservation = reservationRepo.findById(idReservation).get();
            reservation.setNbVoyageurs(nbVoyageurs);
            reservationRepo.save(reservation);
        }

    }

    @Override
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin) throws LogementsIndisponibleException {
        Reservation reservation = null;
        if (reservationRepo.findById(idReservation).isPresent()){
            reservation = reservationRepo.findById(idReservation).get();
            reservation.setDateDebut(dateDebut);
            reservation.setDateFin(dateFin);
            reservationRepo.save(reservation);
        }
    }

    @Override
    public void annulerReservation(String idReservation) {
        reservationRepo.deleteById(idReservation);
    }
}
