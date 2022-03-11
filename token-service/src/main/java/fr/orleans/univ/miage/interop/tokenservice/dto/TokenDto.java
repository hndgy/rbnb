package fr.orleans.univ.miage.interop.tokenservice.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.orleans.univ.miage.interop.tokenservice.MoneySerializer;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenDto {

    private String id;

    private String name;

    private String symbol;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal price;
    


}
