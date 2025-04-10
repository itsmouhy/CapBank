package com.formula1.capbank.dtos.Transaction;

public record TransferRequestDTO (
    Long compteSource,
    Long compteDest,
    Double montant
) {

}
