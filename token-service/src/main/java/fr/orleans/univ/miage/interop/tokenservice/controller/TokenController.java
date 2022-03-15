package fr.orleans.univ.miage.interop.tokenservice.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.service.NomicsService;
import fr.orleans.univ.miage.interop.tokenservice.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {


    private final NomicsService nomicsService;
    private TokenService tokenService;

    @Autowired
    private ModelMapper modelMapper;


    public TokenController(TokenService tokenService, NomicsService nomicsService) {
        this.tokenService = tokenService;
        this.nomicsService = nomicsService;
    }

   /* @GetMapping("/")
    public Iterable<TokenDto> list() throws TokenNotFoundException, InterruptedException, JsonProcessingException {
        return tokenService.getAllTokens();
    }*/

    @DeleteMapping(value = "/{id}")
    public void deleteCoinById(@PathVariable Long id) throws Exception {
        tokenService.deleteTokenById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenDto> getToken(@PathVariable String id) throws TokenNotFoundException {
        Token token = tokenService.getTokenByIdApi(id);
        TokenDto tokenDto = this.modelMapper.map(token, TokenDto.class);
        BigDecimal priceDto = nomicsService.getPrice(id);
        tokenDto.setPrice(priceDto);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/test")
    public Iterable<TokenDto> listPrix() throws JsonProcessingException {
        return tokenService.getAllPrice();
    }

}
