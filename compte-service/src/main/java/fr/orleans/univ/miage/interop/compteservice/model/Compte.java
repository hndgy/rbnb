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

    public Compte(String idOwner, String libelleCompte, String typeCompte) {
        this.idOwner = idOwner;
        this.libelleCompte = libelleCompte;
        this.typeCompte = typeCompte;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_compte", nullable = false)
    private Long idCompte;

    @Column(name = "id_owner", nullable = false)
    private String idOwner;

    @Column(name = "libelle")
    private String libelleCompte;

    //@Enumerated(EnumType.STRING) TODO : Enum
    @Column(name = "type")
    private String typeCompte;




}
