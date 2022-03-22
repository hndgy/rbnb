package fr.orleans.univ.miage.interop.transactionservice.facade;

import fr.orleans.univ.miage.interop.transactionservice.TransactionServiceApplication;
import fr.orleans.univ.miage.interop.transactionservice.modele.Transaction;
import fr.orleans.univ.miage.interop.transactionservice.modele.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class FacadeTransactionImplTest {

    FacadeTransactionImpl facadeTransaction;

    public FacadeTransactionImplTest(FacadeTransactionImpl facadeTransaction) {
        this.facadeTransaction = facadeTransaction;
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void createTransaction() {


        Transaction transaction = new Transaction(Type.IN, "test", "150", 50.0, LocalDateTime.now());
        var res = this.facadeTransaction.createTransaction(Type.IN, "test", "150", 50.0, LocalDateTime.now());

        assertTrue(true);
    }
}