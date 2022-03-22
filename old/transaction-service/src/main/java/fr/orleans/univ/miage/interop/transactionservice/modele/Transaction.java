package fr.orleans.univ.miage.interop.transactionservice.modele;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Document("transaction")
public class Transaction {

    @Id
    private String idTransaction;
    private Type type;
    private String idToken;
    private String amount;
    private Double fees;
    private LocalDateTime date;

    public Transaction(Type type, String idToken, String amount, Double fees, LocalDateTime date) {
        this.idTransaction = "";
        this.type = type;
        this.idToken = idToken;
        this.amount = amount;
        this.fees = fees;
        this.date = date;
    }

    //sans la date
    public Transaction(Type type, String idToken, String amount, Double fees) {
        this.idTransaction = "";
        this.type = type;
        this.idToken = idToken;
        this.amount = amount;
        this.fees = fees;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
