package com.formula1.capbank.dtos.Transaction;

public record DebitDTO (
        String numeroCompte,                // Numero du compte a debiter
        Double montant,
        String description,
        String numeroCompteRecipient        // Numero de compte beneficiaire
) {

}
