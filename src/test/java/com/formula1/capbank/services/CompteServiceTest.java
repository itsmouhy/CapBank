package com.formula1.capbank.services;

import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import com.formula1.capbank.repositories.CompteRepository;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CompteServiceTest {

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private CompteService compteService;

    private Compte compte;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        compte = new Compte();
        compte.setId(1L);
        compte.setSolde(1000.0);
    }

    @Test
    void testDebit_Success() throws CompteNotFoundException, SoldeNotSufficientException {
        when(compteRepository.findById(1L)).thenReturn(java.util.Optional.of(compte));
        compteService.debit(1L, 200.0, "Test debit");
        assertEquals(800.0, compte.getSolde());
        verify(transactionsRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    void testDebit_InsufficientBalance() {
        when(compteRepository.findById(1L)).thenReturn(java.util.Optional.of(compte));
        assertThrows(SoldeNotSufficientException.class, () -> compteService.debit(1L, 2000.0, "Test debit"));
    }

    @Test
    void testDebit_AccountNotFound() {
        assertThrows(CompteNotFoundException.class, () -> compteService.debit(999L, 200.0, "Test debit"));
    }
}
