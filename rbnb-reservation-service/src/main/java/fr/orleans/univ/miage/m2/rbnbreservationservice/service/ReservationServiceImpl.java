package fr.orleans.univ.miage.m2.rbnbreservationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.orleans.univ.miage.m2.rbnbreservationservice.dto.*;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Disponibilite;
import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.DisponibiliteRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.repository.ReservationRepo;
import fr.orleans.univ.miage.m2.rbnbreservationservice.service.exceptions.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    public HashMap<Logement, Collection<Reservation>> getReservationsByHote(Long idHote) throws ReservationIntrouvableException { //TODO : Ptdrrrrr jamais de la vie ça fonctionne + manque modifier dto
        String urlLogements = "http://rbnb-logement-service/logement/"+ idHote;

        ResponseEntity<Logement[]> responseEntity =
                restTemplate.getForEntity(urlLogements, Logement[].class);

        Logement[] logements = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();

        assert logements != null;
        List<Logement> logementDTOS = Arrays.stream(logements)
                .map(logementDTO -> mapper.convertValue(logementDTO, Logement.class)).toList();


        Collection<Reservation> reservationsLogement = new ArrayList<>();
        HashMap<Logement, Collection<Reservation>> reservations = new HashMap<>();

        for (Logement logementDTO : logementDTOS
             ) {
            reservationsLogement =  reservationRepo.findAllReservationsByIdLogement(logementDTO.getId());
            reservations.put(logementDTO,reservationsLogement);
        }
        if (reservations.isEmpty()) {
            throw new ReservationIntrouvableException();
        }
        else {
            return reservations;
        }
    }

    @Override
    public Collection<Reservation> getReservationsByVoyageur(Long idVoyageur) throws ReservationIntrouvableException {
        Collection<Reservation> reservations = reservationRepo.findAllReservationsByIdClient(idVoyageur);
        if (!reservations.isEmpty()) {
            return reservations;
        }
        throw new ReservationIntrouvableException();
    }

    @Override
    public Reservation createReservation(ReservationDTO reservationDTO, Principal principal) throws LogementsIndisponibleException, CapaciteLogementDepasseException, LogementIntrouvableException {
        Date dateDebutReservation;
        Date dateFinReservation;
        Date dateDebutDispo;
        Date dateFinDispo;

/*
        String urlLogement2 = "http://rbnb-logement-service/"+ reservationDTO.getIdLogement();
        LogementDto2 logementDto2 = restTemplate.getForObject(urlLogement2, LogementDto2.class);

        if (logementDto2==null) {
            throw new LogementIntrouvableException();
        }
*/

        String urlLogement = "http://rbnb-logement-service/logement/"+ reservationDTO.getIdLogement();
        LogementDTO logementDto = restTemplate.getForObject(urlLogement, LogementDTO.class);
        //TODO : reverifier les dto

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

        if (disponibilites.isEmpty()){
            throw new LogementsIndisponibleException();
        }

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
//TODO : faire une classe mapper ?
    @Override
    public void updateNbVoyageursReservation(String idReservation, int nbVoyageurs, String token) throws NbVoyagageurIncorrecteException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException {
        Optional<Reservation> reservation;
        reservation = reservationRepo.findById(idReservation);

        if (reservation.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            String[] tokenArray = token.split(" ");
            headers.set("Accept", "application/json");
            headers.add("Authorization", "Bearer " + tokenArray[1]);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String urlLogement = "http://rbnb-logement-service/logement/"+ reservation.get().getIdLogement();

            ResponseEntity<LogementDTO> logementDTOResponseEntity =  restTemplate.exchange(urlLogement, HttpMethod.GET, entity, LogementDTO.class);
            //LogementDTO logementDto = restTemplate.getForObject(urlLogement, LogementDTO.class);

            LogementDTO logementDto = logementDTOResponseEntity.getBody();

            if (logementDto==null){
                throw new LogementIntrouvableException();
            }

            if (logementDto.getNbMax()<reservation.get().getNbVoyageurs()) {
                throw new CapaciteLogementDepasseException();
            }
            reservation.get().setNbVoyageurs(nbVoyageurs);
            reservationRepo.save(reservation.get());
        }
        else throw new ReservationIntrouvableException();
    }

    @Override
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException {
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
    public Reservation getReservationsByIdReservation(String idReservation) throws ReservationIntrouvableException {
        Optional<Reservation> reservation = reservationRepo.findById(idReservation);
        if (reservation.isPresent()) {
            return reservation.get();
        }
        throw new ReservationIntrouvableException();
    }

    @Override
    public Collection<Disponibilite> setDisponibilite(List<DisponibiliteDTO> disponibilitesDTO, String token) throws LogementIntrouvableException {
        //TODO : voir avec Benoit pour if logement existe
        if (disponibilitesDTO.isEmpty()){
            throw new NullPointerException();
        }

        HttpHeaders headers = new HttpHeaders();
        String[] tokenArray = token.split(" ");
        headers.set("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tokenArray[1]);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String urlLogement = "http://rbnb-logement-service/"+ disponibilitesDTO.get(1).getIdLogement();

        ResponseEntity<LogementDto2> logementDto2ResponseEntity = restTemplate.exchange(urlLogement, HttpMethod.GET, entity, LogementDto2.class);
        //LogementDto2 logementDto = restTemplate.getForObject(urlLogement, LogementDto2.class);

        Collection<Disponibilite> disponibilites = new ArrayList<>();

        LogementDto2 logementDto = logementDto2ResponseEntity.getBody();

        if (logementDto!=null) {
            for (DisponibiliteDTO disponibiliteDTO : disponibilitesDTO
                 ) {
                Disponibilite disponibilite = new Disponibilite();
                disponibilite.setIdLogement(disponibiliteDTO.getIdLogement());
                disponibilite.setDateDebut(disponibiliteDTO.getDateDebut());
                disponibilite.setDateFin(disponibiliteDTO.getDateFin());
                disponibilites.add(disponibilite);
            }
            return disponibilites;
        }
        throw new LogementIntrouvableException();

    }
}//TODO : Delete dispo et reservation quand utilisateur est delete