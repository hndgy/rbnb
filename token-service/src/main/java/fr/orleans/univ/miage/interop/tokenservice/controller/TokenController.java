package fr.orleans.univ.miage.interop.tokenservice.controller;
import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.service.NomicsService;
import fr.orleans.univ.miage.interop.tokenservice.service.TokenServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class TokenController {


    private TokenServiceImpl tokenService;

    private NomicsService nomicsService;

    @Autowired
    private ModelMapper modelMapper;

    public TokenController(TokenServiceImpl tokenService, NomicsService nomicsService) {
        this.tokenService = tokenService;
        this.nomicsService = nomicsService;
    }

    @GetMapping("/list")
    public Iterable<Token> list(){
        return tokenService.getAllTokens();
    }

    @GetMapping("/token/{id}")
    public ResponseEntity<Token> getToken(@PathVariable Long id){
        Token token = tokenService.getTokenById(id);
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "/delete/{id}")
    public void deleteCoinById(@PathVariable Long id) throws Exception {
        tokenService.deleteTokenById(id);
    }

    @GetMapping("token/price/{id}")
    public ResponseEntity<TokenDto[]> getPriceToken(@PathVariable String id) throws IOException {
        TokenDto[] tokenDto = nomicsService.getPrice(id);
//        Token[] token = convertToEntity(tokenDto);
        return ResponseEntity.ok(tokenDto);
    }

   /* private Token[] convertToEntity(TokenDto[] tokenDto){
        Token[] tokenEntity = modelMapper.map(tokenDto, Token[].class);
        return tokenEntity;
    }*/

}
