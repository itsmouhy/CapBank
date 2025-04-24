package com.formula1.capbank.controllers;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.exceptions.TransactionNotFoundException;
import com.formula1.capbank.services.ITransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/recentTransactions/{userId}")
    public ResponseEntity<List<TransactionDTO>> getRecentTransactions(@PathVariable Long userId,
                                                                      @RequestParam(name = "size", defaultValue = "5") int size) {
        List<TransactionDTO> transactions = transactionService.getRecentTransactions(userId, size);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
