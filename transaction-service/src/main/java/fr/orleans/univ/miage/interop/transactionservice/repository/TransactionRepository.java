package fr.orleans.univ.miage.interop.transactionservice.repository;

import fr.orleans.univ.miage.interop.transactionservice.modele.Transaction;
import fr.orleans.univ.miage.interop.transactionservice.modele.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> getAllByIdTokenIs(String idToken);

    Transaction getByIdTransaction(String idTransaction);

    void deleteByIdTransaction(String idTransaction);

    List<Transaction> getAllByType(String type);




}
