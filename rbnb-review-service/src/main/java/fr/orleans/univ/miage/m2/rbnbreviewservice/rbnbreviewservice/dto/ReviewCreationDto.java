package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReviewCreationDto {
    private String contenu;
    private Date date;
    private Long idLogement;
    private String idUtilisateur;
    private List<NotationCreationDto> notations;
}