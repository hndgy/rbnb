package fr.orleans.univ.miage.interop.compteservice.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "compte")
public class Compte {

    public Compte(String idUser, String libelleCompte, String typeCompte) {
        this.idUser = idUser;
        this.libelleCompte = libelleCompte;
        this.typeCompte = typeCompte;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_compte", nullable = false)
    private Long idCompte;

    @Column(name = "id_user", nullable = false)
    private String idUser;

    @Column(name = "libelle")
    private String libelleCompte;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private String typeCompte;




}
