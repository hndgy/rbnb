package fr.orleans.univ.miage.interop.compteservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompteDto {

    private Long idCompte;

    private String idOwner;

    private String libelleCompte;

    private String typeCompte;

}
