package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(path = "reservation")
public interface ReservationRepo extends MongoRepository<Reservation, String> {

    Collection<Reservation> findAllReservationsByIdHost (Long idHost);

    Collection<Reservation> findAllReservationsByIdVoyageur(Long idVoyageur);
}
