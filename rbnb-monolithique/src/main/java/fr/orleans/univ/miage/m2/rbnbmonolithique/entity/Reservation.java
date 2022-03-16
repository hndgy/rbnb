package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    private String id;
    private Date dateDebut;
    private Date dateFin;
    private int nbVoyageurs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logement_id")
    private Logement logement;

}
