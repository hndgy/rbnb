package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

public interface ReservationRepo extends MongoRepository<Reservation, String> {

    Collection<Reservation> findAllReservationsByIdLogement (Long idHost);

    Collection<Reservation> findAllReservationsByIdClient(Long idClient);

    Collection<Reservation> findAllReservations();
}
