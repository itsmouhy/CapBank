package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.enums.TransactionStatus;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.exceptions.TransactionNotFoundException;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transactions tr1, tr2;

    @BeforeEach
    void setUp() {
        tr1 = Transactions.builder()
                .id(10L)
                .montant(100.0)
                .type(TransactionType.DEPOT)
                .description("depot test")
                .date(LocalDate.now().minusDays(1))
                .status(TransactionStatus.COMPLETED)
                .noCompteRecipient("CC123")
                .build();

        tr2 = Transactions.builder()
                .id(11L)
                .montant(50.0)
                .type(TransactionType.RETRAIT)
                .description("retrait test")
                .date(LocalDate.now())
                .status(TransactionStatus.COMPLETED)
                .noCompteRecipient("CC456")
                .build();
    }

    @Test
    void testGetRecentTransactions() {
        // given
        //Pageable page0 = PageRequest.of(0, 2, Sort.by("date").descending());
        Page<Transactions> page = new PageImpl<>(List.of(tr2, tr1), PageRequest.of(0, 2), 2);
        when(transactionsRepository.findByUserIdOrderByDateDesc(anyLong(), any(Pageable.class)))
                .thenReturn(page);

        // when
        List<TransactionDTO> transactionDTOS = transactionService.getRecentTransactions(5L, 2);

        // then
        assertThat(transactionDTOS).hasSize(2);
        assertThat(transactionDTOS.get(0).id()).isEqualTo(11L);
        assertThat(transactionDTOS.get(1).id()).isEqualTo(10L);
        verify(transactionsRepository).findByUserIdOrderByDateDesc(eq(5L), any(Pageable.class));
    }

    @Test
    void testDeleteTransaction_existId() throws TransactionNotFoundException {
        // given
        when(transactionsRepository.existsById(20L)).thenReturn(true);

        // when
        transactionService.deleteTransaction(20L);

        // then
        verify(transactionsRepository).deleteById(20L);
    }

    @Test
    void testDeleteTransaction_notExistId() throws TransactionNotFoundException {
        // given
        when(transactionsRepository.existsById(99L)).thenReturn(false);

        // then
        assertThatThrownBy(() -> transactionService.deleteTransaction(99L))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining("Transaction non trouvé");
        verify(transactionsRepository, never()).deleteById(99L);
    }

}
