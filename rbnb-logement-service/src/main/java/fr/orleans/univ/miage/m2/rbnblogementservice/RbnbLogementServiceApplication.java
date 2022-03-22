package fr.orleans.univ.miage.m2.rbnblogementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RbnbLogementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbnbLogementServiceApplication.class, args);
    }

}
