package fr.orleans.univ.miage.m2.rbnblogementservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logement")
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "proprietaire_id")
    private String idProprietaire;

    @Column(name = "nb_voyageurs")
    private int nbVoyageurs;

    @ManyToMany
    private List<Equipement> equipements;

    @ManyToMany
    private List<Categorie> categories;

    @Column(name = "images")
    @OneToMany(mappedBy = "logement")
    private Set<Image> images;

}
