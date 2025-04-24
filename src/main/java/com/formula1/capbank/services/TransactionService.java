package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.TransactionNotFoundException;
import com.formula1.capbank.mappers.TransactionMapper;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    private final TransactionsRepository transactionsRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionsRepository transactionsRepository, TransactionMapper transactionMapper) {
        this.transactionsRepository = transactionsRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<TransactionDTO> getRecentTransactions(Long userId, int size) {
        Page<Transactions> page = transactionsRepository
                .findByUserIdOrderByDateDesc(userId, PageRequest.of(0, size));
        return page.getContent()
                .stream()
                .map(tr -> new TransactionDTO(
                        tr.getId(),
                        tr.getMontant(),
                        tr.getType(),
                        tr.getDescription(),
                        tr.getDate(),
                        tr.getStatus(),
                        tr.getNoCompteRecipient()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransaction(Long id) throws TransactionNotFoundException {
        if (!transactionsRepository.existsById(id))
            throw new TransactionNotFoundException("Transaction non trouvé");
        transactionsRepository.deleteById(id);
    }
}
