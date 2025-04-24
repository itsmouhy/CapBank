package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.exceptions.TransactionNotFoundException;

import java.util.List;

public interface ITransactionService {
    /**
     * @param id
     * @throws TransactionNotFoundException
     */
    void deleteTransaction(Long id) throws TransactionNotFoundException;

    /**
     *
     * @param userId
     * @param size
     * @return
     */
    List<TransactionDTO> getRecentTransactions(Long userId, int size);
}
