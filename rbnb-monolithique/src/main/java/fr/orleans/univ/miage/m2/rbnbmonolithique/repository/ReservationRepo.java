package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepo extends MongoRepository<Reservation, String> {
    Reservation getReservationById(String id);
}
