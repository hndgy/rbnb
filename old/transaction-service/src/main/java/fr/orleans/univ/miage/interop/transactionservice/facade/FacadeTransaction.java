package fr.orleans.univ.miage.interop.transactionservice.facade;

import fr.orleans.univ.miage.interop.transactionservice.facade.exceptions.TransactionNotFoundException;
import fr.orleans.univ.miage.interop.transactionservice.modele.Transaction;
import fr.orleans.univ.miage.interop.transactionservice.modele.Type;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public interface FacadeTransaction {

    Transaction createTransaction(Type type, String token, String amount, Double fees, LocalDateTime date);

    //sans la date
    Transaction createTransaction(Type type, String token, String amount, Double fees);

    Transaction getTransactionById(String id)
            throws TransactionNotFoundException;

    void deleteTransaction(String id)
            throws TransactionNotFoundException;

    Transaction updateTransaction(String id, Type type, String token, String amount, Double fees, LocalDateTime date)
            throws TransactionNotFoundException;

//    Token getTokenByTransactionId(String idTransaction);

    Collection<Transaction> getAllTransactions()
            throws TransactionNotFoundException;

    Collection<Transaction> getAllTransactionsByType(String type)
            throws TransactionNotFoundException;

    Collection<Transaction> getAllTransactionsByToken(String idToken)
            throws TransactionNotFoundException;

    Collection<Transaction> getAllTransactionsByDate(Date date)
            throws TransactionNotFoundException;

}
