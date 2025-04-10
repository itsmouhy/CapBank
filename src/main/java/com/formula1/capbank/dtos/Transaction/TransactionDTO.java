package com.formula1.capbank.dtos.Transaction;

import com.formula1.capbank.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionDTO {
    Long id;
    Double montant;
    TransactionType type;
    String description;
    LocalDate date;
}
