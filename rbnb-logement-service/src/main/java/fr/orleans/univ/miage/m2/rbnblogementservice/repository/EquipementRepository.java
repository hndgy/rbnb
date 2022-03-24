package fr.orleans.univ.miage.m2.rbnblogementservice.repository;

import fr.orleans.univ.miage.m2.rbnblogementservice.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {

    Equipement findEquipementByIdEquipement(Long id);

}
