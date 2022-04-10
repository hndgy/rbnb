package fr.orleans.univ.miage.m2.rbnbreservationservice.repository;

import fr.orleans.univ.miage.m2.rbnbreservationservice.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReservationRepo extends MongoRepository<Reservation, String> {

    Collection<Reservation> findAllReservationsByIdLogement (Long idLogement);

    Collection<Reservation> findAllReservationsByIdClient(String idClient);

    //Collection<Reservation> findAllReservations();
}
