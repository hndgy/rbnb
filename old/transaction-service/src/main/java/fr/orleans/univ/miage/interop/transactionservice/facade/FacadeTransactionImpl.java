package fr.orleans.univ.miage.interop.transactionservice.facade;

import fr.orleans.univ.miage.interop.transactionservice.facade.exceptions.TransactionNotFoundException;
import fr.orleans.univ.miage.interop.transactionservice.modele.Transaction;
import fr.orleans.univ.miage.interop.transactionservice.modele.Type;
import fr.orleans.univ.miage.interop.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Service("FacadeTransaction")
public class FacadeTransactionImpl implements FacadeTransaction {

    private final TransactionRepository transactionRepository;
    public FacadeTransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Type type, String token, String amount, Double fees, LocalDateTime date) {
        Transaction transaction = new Transaction(type, token, amount, fees, date);
        System.out.println(transaction);
        this.transactionRepository.insert(transaction);
        return transaction;
    }

    @Override
    public Transaction createTransaction(Type type, String token, String amount, Double fees) {
        Transaction transaction = new Transaction(type, token, amount, fees);
        System.out.println(transaction);
        this.transactionRepository.insert(transaction);
        return transaction;
    }

    @Override
    public Transaction getTransactionById(String id) {
        return this.transactionRepository.getByIdTransaction(id);
    }

    @Override
    public void deleteTransaction(String id) {
        this.transactionRepository.deleteByIdTransaction(id);
    }

    @Override
    public Transaction updateTransaction(String id, Type type, String token, String amount, Double fees, LocalDateTime date) {
        Transaction transaction = this.transactionRepository.getByIdTransaction(id);
        transaction.setType(type);
        transaction.setIdToken(token);
        transaction.setAmount(amount);
        transaction.setFees(fees);
        transaction.setDate(date);
        return this.transactionRepository.save(transaction);
    }

    @Override
    public Collection<Transaction> getAllTransactions() throws TransactionNotFoundException {
        return this.transactionRepository.findAll();
    }

    @Override
    public Collection<Transaction> getAllTransactionsByType(String type) {
        return this.transactionRepository.getAllByType(type);
    }

    @Override
    public Collection<Transaction> getAllTransactionsByToken(String idToken) {
        return null;
    }

    @Override
    public Collection<Transaction> getAllTransactionsByDate(Date date) {
        return null;
    }

}
