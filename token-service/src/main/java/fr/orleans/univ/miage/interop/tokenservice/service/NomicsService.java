package fr.orleans.univ.miage.interop.tokenservice.service;

import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NomicsService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    private static final long TOKENS_CACHE_LIFETIME = TimeUnit.MINUTES.toMillis(10);
    private static final String TOKEN_PRICE_URL = "https://api.nomics.com/v1/currencies/ticker?key={apiKey}&ids={id}&per-page=1&convert=EUR";

    private final Map<String, Double> priceTokenCache = new HashMap<>();
    private final Map<TokenDto, LocalDateTime> tokenRefresh = new HashMap<>();
    /*private long lastTokensCacheRefresh = 0;*/


    public TokenDto[] getPrice(String id) throws IOException {
        return restTemplate.getForObject(TOKEN_PRICE_URL, TokenDto[].class, apiKey, id);
    }

   /*private void refreshCache() throws IOException {
       if (System.currentTimeMillis() - lastTokensCacheRefresh < TOKENS_CACHE_LIFETIME){
           return;
       }
       priceTokenCache.clear();
       tokenRefresh.clear();

       Map<String, Map<String, Double>> res = new ObjectMapper().readValue(new URL(TOKEN_PRICE_URL), new TypeReference<Map<String, Map<String, Double>>>() {});
       final TokenDto[] tokens = objectMapper.readValue(new URL(TOKEN_PRICE_URL), TokenDto[].class);

       for (TokenDto tokenDto : tokens){
           priceTokenCache.putIfAbsent(tokenDto.getId(), tokenDto.getPrice());
           tokenRefresh.putIfAbsent(tokenDto, LocalDateTime.now());
       }
       lastTokensCacheRefresh = System.currentTimeMillis();
       System.out.println("Cache rafraichit");
   }*/



}
