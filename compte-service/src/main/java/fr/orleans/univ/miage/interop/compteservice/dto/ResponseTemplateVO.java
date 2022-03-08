package fr.orleans.univ.miage.interop.compteservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {

    private CompteDto compteDto;

    private Collection<Transaction> transactions;
}
