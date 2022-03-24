package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
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
    public Reservation createReservation(Reservation reservation) throws LogementsIndisponibleException {
        Date dateDebutReservation;
        Date dateFinReservation;
        Date dateDebutDispo;
        Date dateFinDispo;
        Collection<Disponibilite> disponibilites = disponibiliteRepo.findAllDispoById((reservation.getIdLogement()));

        dateDebutReservation = reservation.getDateDebut();
        dateFinReservation = reservation.getDateFin();

        for (Disponibilite dispo : disponibilites
             ) {
            dateDebutDispo = dispo.getDateDebut();
            dateFinDispo = dispo.getDateFin();

            if ( ((dateDebutReservation.after(dateDebutDispo))||dateDebutReservation.equals(dateDebutDispo)) && ((dateFinReservation.before(dateFinDispo))||dateFinReservation.equals(dateFinDispo)) ) {
                disponibiliteRepo.deleteById(dispo.getId());

                Disponibilite disponibilite1 = new Disponibilite();
                disponibilite1.setIdLogement(reservation.getIdLogement());
                disponibilite1.setDateDebut(dateDebutDispo);
                disponibilite1.setDateDebut(dateDebutReservation);
                disponibiliteRepo.save(disponibilite1);

                Disponibilite disponibilite2 = new Disponibilite();
                disponibilite2.setIdLogement(reservation.getIdLogement());
                disponibilite2.setDateDebut(dateDebutDispo);
                disponibilite2.setDateDebut(dateDebutReservation);
                disponibiliteRepo.save(disponibilite2);

                return reservationRepo.save(reservation);
            }
        }
        throw new LogementsIndisponibleException();
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
            Disponibilite disponibilite1 = disponibiliteRepo.findByDateDebutAndIdLogement(reservation.get().getDateDebut(), reservation.get().getIdLogement());
            Disponibilite disponibilite2 = disponibiliteRepo.findByDateFinAndIdLogement(reservation.get().getDateFin(), reservation.get().getIdLogement());

            Disponibilite disponibilite3 = new Disponibilite();
            disponibilite3.setIdLogement(reservation.get().getIdLogement());
            disponibilite3.setDateDebut(disponibilite1.getDateDebut());
            disponibilite3.setDateFin(disponibilite2.getDateFin());

            disponibiliteRepo.deleteById(disponibilite1.getId());
            disponibiliteRepo.deleteById(disponibilite2.getId());

            disponibiliteRepo.save(disponibilite3);
            reservationRepo.deleteById(idReservation);
        }
        else throw new ReservationIntrouvableException();
    }
}
