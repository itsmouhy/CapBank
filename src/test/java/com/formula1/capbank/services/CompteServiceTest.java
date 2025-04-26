package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Compte.CompteHistoriqueDTO;
import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Compte.NouveauCompterequest;
import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.enums.TransactionStatus;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import com.formula1.capbank.mappers.CompteMapper;
import com.formula1.capbank.repositories.CompteRepository;
import com.formula1.capbank.repositories.TransactionsRepository;
import com.formula1.capbank.repositories.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompteServiceTest {

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private CompteMapper compteMapper;

    @InjectMocks
    private CompteService compteService;

    private Utilisateur utilisateur;
    private Compte compte;

    @BeforeEach
    void setUp() {
        utilisateur = Utilisateur.builder()
                .id(23L)
                .nom("Dupont")
                .prenom("Jean")
                .build();
        compte = Compte.builder()
                .id(10L)
                .numeroCompte("CC123")
                .solde(500.0)
                .date(LocalDate.of(2024, 4, 26))
                .rib(987654321L)
                .utilisateur(utilisateur)
                .build();
    }

    @Test
    void testDetailsCompte_success() {
        // Given
        when(compteRepository.findById(10L)).thenReturn(Optional.of(compte));
        CompteResponse compteResponse = new CompteResponse(10L, "CC123", 500.0, compte.getDate(), 987654321L);
        when(compteMapper.toResponse(compte)).thenReturn(compteResponse);

        // When
        CompteResponse result = compteService.detailsCompte(10L);

        // Then
        assertThat(result).isEqualTo(compteResponse);
    }

    @Test
    void testDetailsCompte_nullId() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.detailsCompte(null))
                .withMessageContaining("Identifiant n'est pas renseigner");
    }

    @Test
    void testDetailsCompte_notFound() {
        // Given
        when(compteRepository.findById(20L)).thenReturn(Optional.empty());

        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.detailsCompte(20L))
                .withMessageContaining("Le compte n'existe pas");
    }

    @Test
    void testNouveuauCompte_success() {
        // Given
        NouveauCompterequest req = new NouveauCompterequest(23L, 1000.0);
        when(utilisateurRepository.findById(23L)).thenReturn(Optional.of(utilisateur));
        Compte savedCompte = Compte.builder()
                .id(11L)
                .numeroCompte("CC999")
                .solde(1000.0)
                .date(LocalDate.now())
                .rib(123456L)
                .utilisateur(utilisateur)
                .build();
        when(compteRepository.save(any())).thenReturn(savedCompte);
        CompteResponse expected = new CompteResponse(11L, "CC999", 1000.0, savedCompte.getDate(), 123456L);
        when(compteMapper.toResponse(savedCompte)).thenReturn(expected);

        // When
        CompteResponse result = compteService.nouveauCompte(req);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testNouveauCompte_nullUserId() {
        // Given
        NouveauCompterequest req = new NouveauCompterequest(null, 100.0);

        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.nouveauCompte(req))
                .withMessageContaining("Veuillez validez les informations renseigner");
    }

    @Test
    void testNouveauCompte_userNotExists() {
        // Given
        NouveauCompterequest req = new NouveauCompterequest(99L, 100.0);
        when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.nouveauCompte(req))
                .withMessageContaining("L'utilisateur n'existe pas");
    }

    @Test
    void testSuppCompte_success() {
        // Given
        when(compteRepository.findById(10L)).thenReturn(Optional.of(compte));
        // on simule un utilisateur avec 2 comptes
        utilisateur.setComptes(List.of(compte, compte));
        // When
        boolean result = compteService.suppCompte(10L);

        // Then
        assertThat(result).isTrue();
        verify(compteRepository).delete(compte);
    }

    @Test
    void testSuppCompte_notFound() {
        // Given
        when(compteRepository.findById(5L)).thenReturn(Optional.empty());

        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.suppCompte(5L))
                .withMessageContaining("Le compte n'existe pas");
    }

    @Test
    void testSuppCompte_onlyOneAccount() {
        // Given
        when(compteRepository.findById(10L)).thenReturn(Optional.of(compte));
        utilisateur.setComptes(List.of(compte));
        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> compteService.suppCompte(10L))
                .withMessageContaining("Impossible de supprimer le compte. Nombre de comptes ne peut pas etre inferieur a 1");
    }

    @Test
    void testDebit_success() throws Exception {
        // Given
        when(compteRepository.findByNumeroCompte("CC123")).thenReturn(Optional.of(compte));

        // When
        compteService.debit("CC123", 200.0, "Retrait X", "CC999");

        // Then
        assertThat(compte.getSolde()).isEqualTo(300.0);
        verify(transactionsRepository, atLeastOnce()).save(any(Transactions.class));
    }

    @Test
    void testDebit_insufficientBalance() {
        // Given
        when(compteRepository.findByNumeroCompte("CC123")).thenReturn(Optional.of(compte));

        // When / Then
        assertThatThrownBy(() -> compteService.debit("CC123", 2000.0, "Retrait X", "CC999"))
                .isInstanceOf(SoldeNotSufficientException.class)
                .hasMessageContaining("Solde non suffisant !!!");
    }

    @Test
    void testCredit_success() throws Exception {
        // Given
        when(compteRepository.findByNumeroCompte("CC123")).thenReturn(Optional.of(compte));

        // When
        compteService.credit("CC123", 300.0, "Dépôt Y", "CC888");

        // Then
        assertThat(compte.getSolde()).isEqualTo(800.0);
        verify(transactionsRepository, atLeastOnce()).save(any(Transactions.class));
    }

    @Test
    void testTransfer_success() throws Exception {
        // Given
        when(compteRepository.findByNumeroCompte("CC123")).thenReturn(Optional.of(compte));
        Compte other = Compte.builder()
                .id(20L)
                .numeroCompte("CC456")
                .solde(0.0)
                .date(LocalDate.now())
                .rib(111L)
                .utilisateur(utilisateur)
                .build();
        when(compteRepository.findByNumeroCompte("CC456")).thenReturn(Optional.of(other));

        // When
        compteService.transfer("CC123", "CC456", 150.0);

        // Then
        assertThat(compte.getSolde()).isEqualTo(350.0);
        assertThat(other.getSolde()).isEqualTo(150.0);
    }

    @Test
    void testGetCompteHistorique_success() throws CompteNotFoundException {
        // Given
        Transactions t1 = Transactions.builder()
                .id(1L)
                .montant(50.0)
                .type(TransactionType.DEPOT)
                .status(TransactionStatus.COMPLETED)
                .noCompteRecipient("X")
                .description("Dépôt")
                .date(LocalDate.now())
                .compte(compte)
                .build();
        Page<Transactions> page = new PageImpl<>(List.of(t1), PageRequest.of(0, 5), 1);
        when(compteRepository.findById(10L)).thenReturn(Optional.of(compte));
        when(transactionsRepository.findByCompteIdOrderByDateDesc(10L, PageRequest.of(0, 5)))
                .thenReturn(page);

        // When
        CompteHistoriqueDTO histo = compteService.getCompteHistorique(10L, 0, 5);

        // Then
        assertThat(histo.transactionDTOS())
                .hasSize(1)
                .first()
                .extracting(TransactionDTO::description)
                .isEqualTo("Dépôt");
    }

    /*@Test
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
    }*/
}
