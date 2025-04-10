package com.formula1.capbank.dtos.Transaction;

public record DebitDTO (
        Long id,
        Double montant,
        String description
) {

}
