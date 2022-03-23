package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private long id;
    private String contenu;
    private String idReservation;
    private Date date;
    private int note;
    private Long idLogement;
    private Long idUtilisateur;
    private String nomUtilisateur;
}
