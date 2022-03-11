package fr.orleans.univ.miage.interop.tokenservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Token")
@JsonIgnoreProperties(ignoreUnknown = true)
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



}
