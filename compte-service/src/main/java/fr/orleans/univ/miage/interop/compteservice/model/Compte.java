package fr.orleans.univ.miage.interop.compteservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "compte")
public class Compte {

    @Id
    @GeneratedValue
    @Column(name = "id_compte", nullable = false)
    private Long idCompte;

    @Column(name = "id_user", nullable = false)
    private String idUser;

    @Column(name = "libelle")
    private String libelleCompte;

    @Column(name = "type")
    private String typeCompte;




}
