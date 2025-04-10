package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Transaction.DailyStatDTO;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.repositories.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TransactionsRepository transactionsRepository;

    public List<DailyStatDTO> getStatsJournalieresParCompte(Long compteId, LocalDate start, LocalDate end) {
        List<Object[]> rawStats = transactionsRepository.findStatsJournalieresParCompte(compteId, start, end);

        Map<LocalDate, DailyStatDTO> resultMap = new LinkedHashMap<>();

        for (Object[] row : rawStats) {
            LocalDate date = (LocalDate) row[0];
            TransactionType type = (TransactionType) row[1];
            Double montant = (Double) row[2];

            resultMap.putIfAbsent(date, new DailyStatDTO(date, 0.0, 0.0));
            DailyStatDTO stat = resultMap.get(date);

            if (type == TransactionType.DEPOT) {
                stat.setTotalDepot(montant);
            } else if (type == TransactionType.RETRAIT) {
                stat.setTotalRetrait(montant);
            }
        }

        return new ArrayList<>(resultMap.values());
    }

}

