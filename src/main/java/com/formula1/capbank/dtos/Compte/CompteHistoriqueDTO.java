package com.formula1.capbank.dtos.Compte;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;

import java.util.List;

public record CompteHistoriqueDTO (
    Long compteId,
    Double montant,
    Integer currentPage,
    Integer totalPages,
    Integer pageSize,
    List<TransactionDTO> transactionDTOS
) {

}
