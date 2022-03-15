package fr.orleans.univ.miage.interop.transactionservice.controller;


import fr.orleans.univ.miage.interop.transactionservice.facade.FacadeTransaction;
import fr.orleans.univ.miage.interop.transactionservice.facade.exceptions.TransactionNotFoundException;
import fr.orleans.univ.miage.interop.transactionservice.modele.Transaction;
import fr.orleans.univ.miage.interop.transactionservice.modele.Type;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("")
public class TransactionController {

    public TransactionController(FacadeTransaction facadeTransaction) {
        this.facadeTransaction = facadeTransaction;
    }

    private final FacadeTransaction facadeTransaction;

    @PostMapping("/transaction")
    public ResponseEntity<Object> createTransaction
            (
                    @RequestBody String transactionBody
//                    @RequestParam String date
            )
    {
        Type newType;
        String idToken = "tokentest";
//        if (Objects.equals(type, "in")) {
//            newType = Type.IN;
//        }
//        else newType = Type.OUT;

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

//        LocalDateTime dateTime = LocalDateTime.parse(date);

//        try {
            /*
            Transaction transaction = this.facadeTransaction.createTransaction(newType, idToken, amount, fees
//                    , dateTime
                    );
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(transaction.getIdTransaction())
                    .toUri();

             */

//            return ResponseEntity.created(uri)
//                    .header("idTransaction", transaction.getIdTransaction()).build();
//        }
//        catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
        return ResponseEntity.ok().build();

    }

    @GetMapping("/transaction/{idTransaction}")
    public ResponseEntity<Object> getTransaction(@PathVariable String idTransaction) {
        try {
            return ResponseEntity.ok(this.facadeTransaction.getTransactionById(idTransaction));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/transaction/{idTransaction}")
    ResponseEntity<Object> deleteTransaction(@PathVariable String idTransaction) {
        try {
            this.facadeTransaction.deleteTransaction(idTransaction);
            return ResponseEntity.ok().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/transaction/{idTransaction}")
    ResponseEntity<Object> updateTransaction
            (
                    @PathVariable String idTransaction,
                    @RequestParam String type,
                    @RequestParam String idToken,
                    @RequestParam String amount,
                    @RequestParam Double fees,
                    @RequestParam LocalDateTime date
            )
    {
        Type newType;
        if (Objects.equals(type, "in")) {
            newType = Type.IN;
        }
        else newType = Type.OUT;
        try {
            Transaction transaction = this.facadeTransaction
                    .updateTransaction(idTransaction, newType, idToken, amount, fees, date);
            return ResponseEntity.ok(transaction);
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/transaction")
    ResponseEntity<Object> getAllTransactions() {
        try {
            return ResponseEntity.ok(this.facadeTransaction.getAllTransactions());
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/transaction/1")
    ResponseEntity<Object> getAllTransactionsByType(@RequestParam String type) {
        try {
            return ResponseEntity.ok(this.facadeTransaction.getAllTransactionsByType(type));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/transaction/2")
    ResponseEntity<Object> getAllTransactionsByToken(@RequestParam String idToken) {
        try {
            return ResponseEntity.ok(this.facadeTransaction.getAllTransactionsByToken(idToken));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/transaction/3")
    ResponseEntity<Object> getAllTransactionsByDate(@RequestParam Date date) {
        try {
            return ResponseEntity.ok(this.facadeTransaction.getAllTransactionsByDate(date));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
