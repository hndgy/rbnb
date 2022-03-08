package fr.orleans.univ.miage.interop.tokenservice.service;

import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.repository.TokenRepo;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService{

    public final TokenRepo tokenRepository;

    public TokenServiceImpl(TokenRepo tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Iterable<Token> saveTokens(List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }

    @Override
    public List<Token> getAllTokens() {
        List<Token> res = tokenRepository.findAll();
        if (res.size() > 0){
            return res;
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteTokenById(Long idToken) throws TokenNotFoundException {
        Optional<Token> token = tokenRepository.findById(idToken);

        if (token.isPresent()){
            tokenRepository.deleteById(idToken);
        }else {
            throw new TokenNotFoundException("Token non trouvé");
        }
    }

    @Override
    public Token getTokenById(Long idToken) {
        return tokenRepository.getById(idToken);
    }

    @Override
    public Token findTokenByName(String name) {
        return tokenRepository.findTokenByName(name);
    }


}
