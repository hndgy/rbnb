package fr.orleans.univ.miage.interop.tokenservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;

import java.util.List;

public interface TokenService {

    Iterable<Token> saveTokens(List<Token> tokens);

/*
    List<TokenDto> getAllTokens() throws TokenNotFoundException, InterruptedException, JsonProcessingException;
*/

    void deleteTokenById(Long idToken) throws TokenNotFoundException;

    Token getTokenByIdApi(String id);

    List<TokenDto> getAllPrice() throws JsonProcessingException;
}
