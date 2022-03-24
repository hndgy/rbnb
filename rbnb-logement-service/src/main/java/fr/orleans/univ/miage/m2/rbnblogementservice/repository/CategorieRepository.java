package fr.orleans.univ.miage.m2.rbnblogementservice.repository;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Categorie findCategorieByIdCategorie(Long id);
}
