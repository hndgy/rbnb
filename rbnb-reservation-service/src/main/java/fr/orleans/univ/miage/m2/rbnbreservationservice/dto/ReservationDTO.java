package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ReservationDTO {

    private Date dateDebut;

    private Date dateFin;

    private int nbVoyageurs;

    private Long idLogement;

}
