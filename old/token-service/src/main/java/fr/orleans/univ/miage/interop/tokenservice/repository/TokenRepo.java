package fr.orleans.univ.miage.interop.tokenservice.repository;

import fr.orleans.univ.miage.interop.tokenservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
    Token getTokenByIdApi(String id);
}
