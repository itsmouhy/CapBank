package com.formula1.capbank.services;

import com.formula1.capbank.exceptions.TransactionNotFoundException;

public interface ITransactionService {
    /**
     * @param id
     * @throws TransactionNotFoundException
     */
    void deleteTransaction(Long id) throws TransactionNotFoundException;
}
