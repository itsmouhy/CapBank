package com.formula1.capbank.dtos.Compte;

import java.time.LocalDate;

public record RibResponse(String nom, String prenom, Long rib, LocalDate date, String numeroCompte) {
}
