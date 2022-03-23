package fr.orleans.univ.miage.m2.rbnbreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RbnbReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbnbReservationServiceApplication.class, args);
    }

}
