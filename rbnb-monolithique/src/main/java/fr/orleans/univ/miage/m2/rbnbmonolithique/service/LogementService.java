package fr.orleans.univ.miage.m2.rbnbmonolithique.service;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Disponibilite;
import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Logement;

import java.util.Collection;
import java.util.Date;

public interface LogementService {
    Collection<Disponibilite> getDisponibilitesByLogement(Long idLogement);

    Collection<Disponibilite> getDisponibilitesByDatesAndLogement(Long idLogement, Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDate(Date dateDebut, Date dateFin);

    Collection<Logement> getLogementDisponiblesByDateAndVille(Date dateDebut, Date dateFin, String ville);

    Logement createLogement(Logement logement);

    Disponibilite createDispobilite(Disponibilite disponibilite);

    void updateLogement(Logement logement);


    void updateDisponibilite(Disponibilite disponibilite);
}
