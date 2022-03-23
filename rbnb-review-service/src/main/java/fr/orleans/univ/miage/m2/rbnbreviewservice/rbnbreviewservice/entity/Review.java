package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String contenu;

    private String idReservation;

    private Date date;

    private int note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_logement", nullable = false)
    private Logement logement;

//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
//    private Collection<Notation> notations;

}

