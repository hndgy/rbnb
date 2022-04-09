package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Notation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ReviewDto {
    private long id;
    private String contenu;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
    private LogementDto logement;
    private UtilisateurDto utilisateur;
    private List<NotationDto> notations;
}
