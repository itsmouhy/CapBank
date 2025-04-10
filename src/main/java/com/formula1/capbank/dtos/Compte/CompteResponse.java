package com.formula1.capbank.dtos.Compte;

import java.time.LocalDate;

public record CompteResponse(Long id, Double solde, LocalDate date, Long rib) {
    public void setId(long l) {
    }

    public void setSolde(double v) {
    }
}
