package com.formula1.capbank.services;

import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.TransactionNotFoundException;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {

    private final TransactionsRepository transactionsRepository;

    public TransactionService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public void deleteTransaction(Long id) throws TransactionNotFoundException {
        if (!transactionsRepository.existsById(id))
            throw new TransactionNotFoundException("Transaction non trouvé");
        transactionsRepository.deleteById(id);
    }
}
