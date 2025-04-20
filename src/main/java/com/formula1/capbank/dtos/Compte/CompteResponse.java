package com.formula1.capbank.dtos.Compte;

import java.time.LocalDate;

public record CompteResponse(Long id, String numeroCompte,Double solde, LocalDate date, Long rib) {
    public void setId(long l) {
    }
}
