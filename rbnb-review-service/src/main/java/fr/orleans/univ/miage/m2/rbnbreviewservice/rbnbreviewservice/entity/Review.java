package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity;

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
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String contenu;

    private Date date;

    private String idUtilisateur;

    private Long idLogement;

//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
//    private Collection<Notation> notations;

}

