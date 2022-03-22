package fr.orleans.univ.miage.m2.rbnbmonolithique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RbnbMonolithiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbnbMonolithiqueApplication.class, args);
	}
}
