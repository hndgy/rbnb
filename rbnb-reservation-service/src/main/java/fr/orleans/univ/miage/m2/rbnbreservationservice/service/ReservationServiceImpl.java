package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.LogementDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.ReservationDTO;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.DisponibiliteRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.ReservationRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.CapaciteLogementDepasseException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.LogementsIndisponibleException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.NbVoyagageurIncorrecteException;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.ReservationIntrouvableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final DisponibiliteRepo disponibiliteRepo;
    private final RestTemplate restTemplate;

    public ReservationServiceImpl(ReservationRepo reservationRepo, DisponibiliteRepo disponibiliteRepo, RestTemplate restTemplate) {
        this.reservationRepo = reservationRepo;
        this.disponibiliteRepo = disponibiliteRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    public Collection<Reservation> getReservationsByHote(Long idHote) { //TODO : Ptdrrrrr jamais de la vie ça fonctionne
        String urlLogements = "http://rbnb-logement-service/logement/"+ idHote;
        //Collection<LogementDTO> logements  = restTemplate.getForObject(urlLogements, LogementDTO.class);
        LogementDTO[] logements = restTemplate.getForObject(urlLogements, LogementDTO[].class);

        Collection<Reservation> reservationsLogement = new ArrayList<>();
        Collection<Reservation> reservations = new ArrayList<>();

        assert logements != null;
        for (LogementDTO logementDTO : logements
             ) {
            reservationsLogement =  reservationRepo.findAllReservationsByIdLogement(logementDTO.getIdLogement());
            reservations.addAll(reservationsLogement);
        }
        return reservations;
    }

    @Override
    public Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) {
        return reservationRepo.findAllReservationsByIdClient(idVoyageur);
    }

    @Override
    public Reservation createReservation(ReservationDTO reservationDTO, Principal principal) throws LogementsIndisponibleException, CapaciteLogementDepasseException {
        Date dateDebutReservation;
        Date dateFinReservation;
        Date dateDebutDispo;
        Date dateFinDispo;

        String urlLogement = "http://rbnb-logement-service/logement/"+ reservationDTO.getIdLogement();
        LogementDTO logementDto = restTemplate.getForObject(urlLogement, LogementDTO.class);

        assert logementDto != null;
        if (logementDto.getNbMax()<reservationDTO.getNbVoyageurs()) {
            throw new CapaciteLogementDepasseException();
        }

        Reservation reservation = new Reservation();
        reservation.setNbVoyageurs(reservationDTO.getNbVoyageurs());
        reservation.setIdLogement(reservationDTO.getIdLogement());
        reservation.setDateDebut(reservationDTO.getDateDebut());
        reservation.setDateFin(reservationDTO.getDateFin());
        reservation.setIdClient(Long.valueOf(principal.getName()));

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
    public void updateNbVoyageursReservation(String idReservation, int nbVoyageurs) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException, CapaciteLogementDepasseException {
        Optional<Reservation> reservation;
        reservation = reservationRepo.findById(idReservation);

        if (reservation.isPresent()) {
            String urlLogement = "http://rbnb-logement-service/logement/"+ reservation.get().getIdLogement();
            LogementDTO logementDto = restTemplate.getForObject(urlLogement, LogementDTO.class);

            assert logementDto != null;
            if (logementDto.getNbMax()<reservation.get().getNbVoyageurs()) {
                throw new CapaciteLogementDepasseException();
            }
            reservation.get().setNbVoyageurs(nbVoyageurs);
            reservationRepo.save(reservation.get());
        }
        else throw new ReservationIntrouvableException();
    }

    @Override
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException {
        Optional<Reservation> reservation2 = reservationRepo.findById(idReservation);
        Reservation reservation = reservation2.get();
        reservation.setDateFin(dateFin);
        reservation.setDateDebut(dateDebut);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setNbVoyageurs(reservationDTO.getNbVoyageurs());
        reservationDTO.setIdLogement(reservationDTO.getIdLogement());
        reservationDTO.setDateDebut(reservationDTO.getDateDebut());
        reservationDTO.setDateFin(reservationDTO.getDateFin());

        this.annulerReservation(idReservation);

        this.createReservation(reservationDTO, principal);
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

    @Override
    public Reservation getReservationsByIdReservation(String idReservation) {
        return reservationRepo.findById(idReservation).get();
    }
}
