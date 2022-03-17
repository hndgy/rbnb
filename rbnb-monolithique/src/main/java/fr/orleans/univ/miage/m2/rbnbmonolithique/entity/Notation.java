package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Notation")
public class Notation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private int etoile;

    @ManyToOne
    private Prestation prestation;

    @ManyToOne
    @JoinColumn(name = "commentaire_id", referencedColumnName="id")
    private Commentaire commentaire;

}
