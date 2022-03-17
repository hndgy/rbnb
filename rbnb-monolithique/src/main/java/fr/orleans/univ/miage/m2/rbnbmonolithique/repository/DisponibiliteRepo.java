package fr.orleans.univ.miage.m2.rbnbmonolithique.repository;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Disponibilite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RepositoryRestResource(path = "disponibilite")
public interface DisponibiliteRepo extends MongoRepository<Disponibilite, String> {

    Disponibilite findByIdLogement(Long idLogement);
}
