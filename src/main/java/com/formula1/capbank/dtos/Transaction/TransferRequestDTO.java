package com.formula1.capbank.dtos.Transaction;

public record TransferRequestDTO (
    String numeroCompteSource,
    String numeroCompteDest,
    Double montant
) {

}
