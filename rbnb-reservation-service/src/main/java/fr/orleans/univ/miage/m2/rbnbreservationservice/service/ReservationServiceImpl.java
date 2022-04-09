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

    @Override //TODO : revoir les DTO ?
    public HashMap<Logement, Collection<Reservation>> getReservationsByHote(Long idHote, String token) throws ReservationIntrouvableException, LogementIntrouvableException {
        HttpHeaders headers = new HttpHeaders();
        String[] tokenArray = token.split(" ");
        headers.set("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tokenArray[1]);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String urlLogements = "http://rbnb-logement-service/logement/"+ idHote;

        ResponseEntity<Logement[]> responseEntity =
                restTemplate.exchange(urlLogements, HttpMethod.GET, entity,  Logement[].class);

        Logement[] logements = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();

        if (logements==null) {
            throw new LogementIntrouvableException();
        }

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
    public Reservation createReservation(ReservationDTO reservationDTO, Principal principal, String token) throws LogementsIndisponibleException, CapaciteLogementDepasseException, LogementIntrouvableException, UtilisateurInexistantException {
        Date dateDebutReservation;
        Date dateFinReservation;
        Date dateDebutDispo;
        Date dateFinDispo;

        HttpHeaders headers = new HttpHeaders();
        String[] tokenArray = token.split(" ");
        headers.set("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tokenArray[1]);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String urlLogement = "http://rbnb-logement-service/logement/"+ reservationDTO.getIdLogement();
        ResponseEntity<LogementDTO> logementDTOResponseEntity = restTemplate.exchange(urlLogement, HttpMethod.GET, entity, LogementDTO.class);
        //LogementDTO logementDto = restTemplate.getForObject(urlLogement, LogementDTO.class);
        LogementDTO logementDTO = logementDTOResponseEntity.getBody();
        //TODO : reverifier les dto

        if (logementDTO==null){
            throw new LogementIntrouvableException();
        }

        String urlUtilisateur = "http://localhost:9002/Utilisateur/"+ principal.getName();
        ResponseEntity<UtilisateurDto> restUtilisateurDto = restTemplate.exchange(urlUtilisateur, HttpMethod.GET, entity, UtilisateurDto.class);

        if (restUtilisateurDto.getBody() == null) {
            throw new UtilisateurInexistantException();
        }

        if (logementDTO.getNbMax()<reservationDTO.getNbVoyageurs()) {
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
    public void updateDateReservation(String idReservation, Date dateDebut, Date dateFin, Principal principal, String token) throws LogementsIndisponibleException, ReservationIntrouvableException, CapaciteLogementDepasseException, LogementIntrouvableException, UtilisateurInexistantException {
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

        this.createReservation(reservationDTO, principal, token);
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
    public Collection<Disponibilite> setDisponibilite(Long idLogement, List<DisponibiliteDTO> disponibilitesDTO, String token) throws LogementIntrouvableException {
        if (disponibilitesDTO.isEmpty()){
            throw new NullPointerException();
        }

        HttpHeaders headers = new HttpHeaders();
        String[] tokenArray = token.split(" ");
        headers.set("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tokenArray[1]);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String urlLogement = "http://rbnb-logement-service/"+ idLogement;

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

    @Override
    public void deleteDispoEtReservationWhenHostDeleted(Long idHote, String token) throws ReservationIntrouvableException, LogementIntrouvableException {
        HashMap<Logement, Collection<Reservation>> reservationsMap =  this.getReservationsByHote(idHote,token);

        Collection<Collection<Reservation>> reservations1 = reservationsMap.values();

        for (Collection<Reservation> reservationCollection: reservations1
             ) {
            for (Reservation reservation : reservationCollection
                 ) {
                this.annulerReservation(reservation.getId());
                //TODO : RabbitMq notifier le client de l'annulation de la reservation
            }
        }
    }

    @Override
    public void deleteDispoEtReservationWhenClientDeleted(Long idClient) throws ReservationIntrouvableException {
        Collection<Reservation> reservations = this.getReservationsByVoyageur(idClient);
        for (Reservation reservation : reservations
             ) {
            this.annulerReservation(reservation.getId());
            //TODO: RabbitMq notifier l'hote de l'annulation de la reservation
        }
    }

    @Override
    public void deleteReservationClientByClient(Long idClient) throws ReservationIntrouvableException {
       Collection<Reservation> reservations = reservationRepo.findAllReservationsByIdClient(idClient);

        for (Reservation reservation : reservations
             ) {
             this.annulerReservation(reservation.getId());
             //TODO: RabbitMq notifier l'hote de l'annulation de la reservation
        }
    }

    @Override
    public void deleteReservationClientByHote(String idReservation) throws ReservationIntrouvableException {
        this.annulerReservation(idReservation);
        //TODO: RabbitMq notifier le client de l'annulation de sa reservation
    }

    @Override
    public void deleteReservationClientWhenLogementDeleted(Long idLogement) throws ReservationIntrouvableException {
        Collection<Reservation> reservations = reservationRepo.findAllReservationsByIdLogement(idLogement);

        for (Reservation reservation : reservations
             ) {
            this.annulerReservation(reservation.getId());
            //TODO: RabbitMq notifier le client de l'annulation de la reservation
        }
    }
}