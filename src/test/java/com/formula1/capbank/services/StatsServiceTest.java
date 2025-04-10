package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Transaction.DailyStatDTO;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void testGetStatsJournalieresParCompte() {
        LocalDate start = LocalDate.of(2025, 4, 8);
        LocalDate end = LocalDate.of(2025, 4, 9);
        Long compteId = 1L;
        List<Object[]> rawStats = Arrays.asList(
                new Object[] { LocalDate.of(2025, 4, 8), TransactionType.DEPOT, 100.0 },
                new Object[] { LocalDate.of(2025, 4, 8), TransactionType.RETRAIT, 50.0 },
                new Object[] { LocalDate.of(2025, 4, 9), TransactionType.DEPOT, 200.0 },
                new Object[] { LocalDate.of(2025, 4, 9), TransactionType.RETRAIT, 30.0 }
        );

        when(transactionsRepository.findStatsJournalieresParCompte(compteId, start, end)).thenReturn(rawStats);
        List<DailyStatDTO> stats = statsService.getStatsJournalieresParCompte(compteId, start, end);
        assertEquals(2, stats.size());

        DailyStatDTO stat1 = stats.get(0);
        assertEquals(LocalDate.of(2025, 4, 8), stat1.getDate());
        assertEquals(100.0, stat1.getTotalDepot());
        assertEquals(50.0, stat1.getTotalRetrait());

        DailyStatDTO stat2 = stats.get(1);
        assertEquals(LocalDate.of(2025, 4, 9), stat2.getDate());
        assertEquals(200.0, stat2.getTotalDepot());
        assertEquals(30.0, stat2.getTotalRetrait());
        verify(transactionsRepository).findStatsJournalieresParCompte(compteId, start, end);
    }
}
