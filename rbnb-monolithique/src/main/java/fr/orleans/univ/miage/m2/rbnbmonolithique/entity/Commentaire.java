package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Commentaire")
public class Commentaire {
    @Id
    private String id;
    @OneToOne
    private Reservation reservation;
    private String contenu;
    private int etoiles;

}
