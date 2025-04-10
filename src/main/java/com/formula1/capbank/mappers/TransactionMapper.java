package com.formula1.capbank.mappers;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.entities.Transactions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapper {

    private final ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDTO fromTransaction(Transactions transaction) {
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        return transactionDTO;
    }
}
