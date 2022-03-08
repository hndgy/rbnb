package fr.orleans.univ.miage.interop.tokenservice.service;

import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;

import java.util.List;

public interface TokenService {

    Iterable<Token> saveTokens(List<Token> tokens);

    List<Token> getAllTokens();

    void deleteTokenById(Long idToken) throws TokenNotFoundException;

    Token getTokenById(Long idToken);

    Token findTokenByName(String name);

}
