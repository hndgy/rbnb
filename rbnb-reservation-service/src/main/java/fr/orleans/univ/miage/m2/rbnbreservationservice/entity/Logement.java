package fr.orleans.univ.miage.m2.rbnbreservationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Logement")
public class Logement {
    @Id
    private Long id;
    private String libelle;
/*
    @Field(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @Field(name = "logements")
    private Collection<Categorie> categories;

 */
}
