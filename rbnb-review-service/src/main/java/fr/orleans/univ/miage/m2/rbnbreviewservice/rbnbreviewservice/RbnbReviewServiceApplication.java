package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice;

import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.entity.Prestation;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.NotationRepo;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.repository.PrestationRepo;
import fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class RbnbReviewServiceApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(RbnbReviewServiceApplication.class, args);
    }

   @Bean
    public CommandLineRunner runner(PrestationRepo prestationRepo) {
        return args -> {
            var p = new Prestation();
            var q = new Prestation();
            var v = new Prestation();
            p.setLibelle("Localisation");
            q.setLibelle("Propreté");
            v.setLibelle("Equipement");
            prestationRepo.save(p);
            prestationRepo.save(q);
            prestationRepo.save(v);
        };
    }
}
