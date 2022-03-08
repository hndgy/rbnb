package fr.orleans.univ.miage.interop.tokenservice.controller;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.service.CoinGeckoService;
import fr.orleans.univ.miage.interop.tokenservice.service.TokenServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TokenController {

    private TokenServiceImpl tokenService;

    private CoinGeckoService coinGeckoService;

    public TokenController(TokenServiceImpl tokenService, CoinGeckoService coinGeckoService) {
        this.tokenService = tokenService;
        this.coinGeckoService = coinGeckoService;
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
    public String deleteCoinById(@PathVariable Long id) throws Exception {
        tokenService.deleteTokenById(id);
        return "redirect:/list";
    }

    public ResponseEntity<>

}
