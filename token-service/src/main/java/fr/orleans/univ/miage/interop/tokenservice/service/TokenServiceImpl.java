package fr.orleans.univ.miage.interop.tokenservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import fr.orleans.univ.miage.interop.tokenservice.exception.TokenNotFoundException;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.repository.TokenRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final String TOKEN_ALL_URL = "https://api.nomics.com/v1/currencies/ticker?key={apiKey}&per-page=100&convert=EUR";

    @Value("${api.key}")
    private String apiKey;

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

/*
    @Override
    public List<TokenDto> getAllTokens() throws TokenNotFoundException, InterruptedException, JsonProcessingException {
        List<Token> tokens = tokenRepository.findAll();
        List<TokenDto> res = new ArrayList<>();
        for (var token : tokens) {
            Thread.sleep(1000);
            var dto = this.modelMapper.map(token, TokenDto.class);
            dto.setPrice(nomicsService.getPrice(token.getIdApi()));
            res.add(dto);
        }
        return res;
    }
*/


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

    @Override
    public List<TokenDto> getAllPrice() throws JsonProcessingException {
        List<Token> tokens = tokenRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode priceField = mapper.readTree(restTemplate.getForObject(TOKEN_ALL_URL, String.class, apiKey));
        Map<String, String> mapToken = new HashMap<>();
        List<TokenDto> res = new ArrayList<>();
        for (JsonNode root : priceField){
            mapToken.putIfAbsent(root.path("id").asText(),root.path("price").asText());
        }
        for (Token token : tokens){
            mapToken.forEach((id, price)-> {
                if (id.equals(token.getIdApi())){
                    var dto = this.modelMapper.map(token, TokenDto.class);
                    dto.setPrice(new BigDecimal(price));
                    res.add(dto);
                }
            });
        }
        return res;
    }

}
