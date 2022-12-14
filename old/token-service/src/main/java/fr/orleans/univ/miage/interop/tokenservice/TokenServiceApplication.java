package fr.orleans.univ.miage.interop.tokenservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import fr.orleans.univ.miage.interop.tokenservice.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class TokenServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public CommandLineRunner runner(TokenService tokenService){
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Token>> typeReference = new TypeReference<List<Token>>() {};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/response.json");
			try {
				List<Token> tokens = mapper.readValue(inputStream,typeReference);
				tokenService.saveTokens(tokens);
				System.out.println("Tokens saved");
			}catch (IOException e){
				System.out.println("Unable to save " + e.getMessage());
			}
		};
	}


}
