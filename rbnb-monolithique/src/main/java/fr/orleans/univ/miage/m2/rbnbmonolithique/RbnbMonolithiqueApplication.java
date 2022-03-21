package fr.orleans.univ.miage.m2.rbnbmonolithique;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RbnbMonolithiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbnbMonolithiqueApplication.class, args);
	}
}
