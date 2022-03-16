package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Disponibilite")
public class Disponibilite {
    @Id
    private String id;

    @Field(name = "date_debut")
    private Date dateDebut;

    @Field(name = "date_fin")
    private Date dateFin;

    @Field(name = "id_logement")
    private Long idLogement;

}
