package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Disponibilite {
    @Id
    private String id;
    private Date dateDebut;
    private Date dateFin;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "logement_id")
    private Logement logement;

}
