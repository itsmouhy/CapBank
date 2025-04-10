package com.formula1.capbank.dtos.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatDTO {
    private LocalDate date;
    private Double totalDepot = 0.0;
    private Double totalRetrait = 0.0;
}