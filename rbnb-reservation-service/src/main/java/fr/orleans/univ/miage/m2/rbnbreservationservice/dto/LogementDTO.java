package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import lombok.Data;

@Data
public class LogementDTO {

    private long idLogement;

    private long idProprietaire;

    private long nbMax;
}
