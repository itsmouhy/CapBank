package com.formula1.capbank.services;

import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import com.formula1.capbank.repositories.CompteRepository;
import com.formula1.capbank.repositories.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

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
        compte = Compte.builder()
                .id(1L)
                .numeroCompte("ACC234")
                .solde(1000.0)
                .date(LocalDate.now())
                .build();
    }

    @Test
    void testDebit_Success() throws CompteNotFoundException, SoldeNotSufficientException {
        // Arrange
        when(compteRepository.findByNumeroCompte("ACC234"))
                .thenReturn(Optional.of(compte));
        // Act
        compteService.debit("ACC234", 200.0, "Test debit", "EDQ134");
        // Assert
        assertEquals(800.0, compte.getSolde());
        verify(compteRepository).save(compte);
        verify(transactionsRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    void testDebit_InsufficientBalance() {
        when(compteRepository.findByNumeroCompte("ACC234"))
                .thenReturn(Optional.of(compte));
        assertThrows(SoldeNotSufficientException.class,
                () -> compteService.debit("ACC234", 2000.0, "Test debit", "EDQ134"));
        verify(transactionsRepository, never()).save(any());
    }

    @Test
    void testDebit_AccountNotFound() {
        when(compteRepository.findByNumeroCompte("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(
                CompteNotFoundException.class,
                () -> compteService.debit("UNKNOWN", 100.0, "Test", "EDQ134")
        );
        verify(transactionsRepository, never()).save(any());
    }

    @Test
    void testCredit_Success() throws CompteNotFoundException {
        // Arrange
        when(compteRepository.findByNumeroCompte("ACC234"))
                .thenReturn(Optional.of(compte));

        // Act
        compteService.credit(
                "ACC234",
                300.0,
                "Test credit",
                "EDQ134"
        );

        // Assert
        assertEquals(1300.0, compte.getSolde());
        verify(compteRepository).save(compte);
        verify(transactionsRepository, times(1)).save(argThat(tx ->
                tx.getCompte().equals(compte) &&
                        tx.getType() == TransactionType.DEPOT &&
                        tx.getMontant().equals(300.0) &&
                        tx.getDescription().equals("Test credit") &&
                        tx.getNoCompteRecipient().equals("EDQ134")
        ));
    }

    @Test
    void testCredit_AccountNotFound() {
        when(compteRepository.findByNumeroCompte("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(
                CompteNotFoundException.class,
                () -> compteService.credit("UNKNOWN", 100.0, "Test credit", "SOURCE789")
        );
        verify(transactionsRepository, never()).save(any());
    }
}
