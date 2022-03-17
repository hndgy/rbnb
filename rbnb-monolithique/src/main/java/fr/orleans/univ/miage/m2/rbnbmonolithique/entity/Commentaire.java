package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Commentaire")
public class Commentaire {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String idReservation;
    private String contenu;

    @OneToMany(mappedBy = "commentaire")
    private Collection<Notation> notations;

}
