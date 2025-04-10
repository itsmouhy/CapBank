package com.formula1.capbank.dtos.Transaction;

public record CreditDTO (
    Long id,
    Double montant,
    String description
) {

}
