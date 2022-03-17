package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String libelle;

    @ManyToMany
    @JoinTable(
            name = "categorie_logement",
            joinColumns = @JoinColumn(name = "categorie_id"),
            inverseJoinColumns = @JoinColumn(name = "logement_id"))
    private Collection<Logement> logements;


}
