package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RbnbReviewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbnbReviewServiceApplication.class, args);
    }

}
