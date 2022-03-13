package fr.orleans.univ.miage.interop.tokenservice.service;

import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.repository.TokenRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private ModelMapper modelMapper;

    public final TokenRepo tokenRepository;
    public final NomicsService nomicsService;

    public TokenServiceImpl(TokenRepo tokenRepository, NomicsService nomicsService) {
        this.tokenRepository = tokenRepository;
        this.nomicsService = nomicsService;
    }

    @Override
    public Iterable<Token> saveTokens(List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }

    @Override
    public List<TokenDto> getAllTokens() throws TokenNotFoundException, InterruptedException {
       List<Token> tokens = tokenRepository.findAll();
       List<TokenDto> res = new ArrayList<>();
       for (var token: tokens){
           Thread.sleep(1000);
           var dto = this.modelMapper.map(token, TokenDto.class);
           dto.setPrice(nomicsService.getPrice(token.getIdApi()));
           res.add(dto);
        }
       return res;
    }

    @Override
    public void deleteTokenById(Long idToken) throws TokenNotFoundException {
        Optional<Token> token = tokenRepository.findById(idToken);

        if (token.isPresent()){
            tokenRepository.deleteById(idToken);
        }else {
            throw new TokenNotFoundException(idToken.toString());
        }
    }

    public Token getTokenById(Long id){
        return tokenRepository.getById(id);
    }

    @Override
    public Token getTokenByIdApi(String id) {
        return tokenRepository.getTokenByIdApi(id);
    }

      /* private Token[] convertToEntity(TokenDto[] tokenDto){
        Token[] tokenEntity = modelMapper.map(tokenDto, Token[].class);
        return tokenEntity;
    }*/

}
