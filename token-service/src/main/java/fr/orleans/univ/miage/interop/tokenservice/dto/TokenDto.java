package fr.orleans.univ.miage.interop.tokenservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String id;

    //private String urlPrice;

    public Double price;

}
