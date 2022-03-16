package fr.orleans.univ.miage.m2.rbnbmonolithique.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Reservation {
    @Id
    private String id;
    
    @Field(name = "date_debut")
    private Date dateDebut;
    
    @Field(name = "date_fin")
    private Date dateFin;
    @Field(name="nb_voyageurs")
    private int nbVoyageurs;

    @Field(name = "id_client")
    private Long idClient;

    @Field(name = "id_logement")
    private Logement idLogement;

}
