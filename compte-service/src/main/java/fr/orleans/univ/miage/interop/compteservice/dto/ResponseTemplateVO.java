package fr.orleans.univ.miage.interop.compteservice.dto;

import fr.orleans.univ.miage.interop.compteservice.model.Compte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {

    private Compte compte;

    private Collection<Transaction> transactions;
}
