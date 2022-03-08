package fr.orleans.univ.miage.interop.tokenservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.orleans.univ.miage.interop.tokenservice.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CoinGeckoService {

    @Autowired
    private RestTemplate restTemplate;

    private static final long TOKENS_CACHE_LIFETIME = TimeUnit.MINUTES.toMillis(10);
    private static final String TOKEN_PRICE_URL = "https://api.coingecko.com/api/v3/simple/price?ids={id}&vs_currencies=eur";

    private final Map<String, Double> priceTokenCache = new HashMap<>();
    private final Map<TokenDto, LocalDateTime> tokenRefresh = new HashMap<>();
    private long lastTokensCacheRefresh = 0;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public TokenDto getPrice(String id) throws IOException {
        return restTemplate.getForObject(TOKEN_PRICE_URL, TokenDto.class, id);
    }

   private void refreshCache() throws IOException {
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
   }



}
