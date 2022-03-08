package fr.orleans.univ.miage.interop.tokenservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Token")
public class Token {

    @Id
    @GeneratedValue
    @Column(name = "idToken")
    private Long idToken;

    @Column(name = "idCoinGecko")
    private String id;

    @Column(name = "symbole")
    private String symbol;

    @Column(name = "nom")
    private String name;

    @Column(name = "prix")
    private Double price;

}
