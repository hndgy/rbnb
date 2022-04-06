package fr.orleans.univ.miage.m2.rbnbreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private Long id;

    private String nom;

    private String url;

    private Logement logement;


}
