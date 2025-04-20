package com.formula1.capbank.dtos.Transaction;

public record CreditDTO (
    String numeroCompte,                // Numero du compte a crediter
    Double montant,
    String description,
    String numeroCompteRecipient        // Numero de compte expediteur
) {

}
