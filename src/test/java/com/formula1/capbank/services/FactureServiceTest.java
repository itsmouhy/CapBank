package com.formula1.capbank.services;

import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Facture;
import com.formula1.capbank.enums.TypeService;
import com.formula1.capbank.repositories.FactureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FactureServiceTest {

    @Mock
    private FactureRepository factureRepository;

    @InjectMocks
    private FactureService factureService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetFacturesByCompte() {
        Facture facture1 = new Facture();
        facture1.setId(1L);
        facture1.setId(101L);

        Facture facture2 = new Facture();
        facture2.setId(2L);
        facture2.setId(101L);

        List<Facture> factures = Arrays.asList(facture1, facture2);

        when(factureRepository.findByCompteId(101L)).thenReturn(factures);

        List<Facture> result = factureService.getFacturesByCompte(101L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(factureRepository, times(1)).findByCompteId(101L);
    }

    @Test
    public void testGetFacturesImpayees() {
        Facture facture1 = new Facture();
        facture1.setId(1L);
        facture1.setEstPayee(false);

        Facture facture2 = new Facture();
        facture2.setId(2L);
        facture2.setEstPayee(false);

        List<Facture> unpaidFactures = Arrays.asList(facture1, facture2);

        when(factureRepository.findByEstPayeeFalse()).thenReturn(unpaidFactures);

        List<Facture> result = factureService.getFacturesImpayees();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(factureRepository, times(1)).findByEstPayeeFalse();
    }

    @Test
    public void testGetFacturesByTypeService() {
        Facture facture1 = new Facture();
        facture1.setId(1L);
        facture1.setTypeService(TypeService.INTERNET);

        Facture facture2 = new Facture();
        facture2.setId(2L);
        facture2.setTypeService(TypeService.ELECTRICITE);

        List<Facture> factures = Arrays.asList(facture1, facture2);

        when(factureRepository.findByTypeService(TypeService.INTERNET)).thenReturn(Arrays.asList(facture1));

        List<Facture> result = factureService.getFacturesByTypeService(TypeService.INTERNET);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeService.INTERNET, result.get(0).getTypeService());
        verify(factureRepository, times(1)).findByTypeService(TypeService.INTERNET);
    }

    @Test
    public void testGetFacturesByFournisseur() {
        Facture facture1 = new Facture();
        facture1.setId(1L);
        facture1.setFournisseur("ABC Corp");

        Facture facture2 = new Facture();
        facture2.setId(2L);
        facture2.setFournisseur("XYZ Corp");

        List<Facture> factures = Arrays.asList(facture1, facture2);

        when(factureRepository.findByFournisseur("ABC Corp")).thenReturn(Arrays.asList(facture1));

        List<Facture> result = factureService.getFacturesByFournisseur("ABC Corp");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC Corp", result.get(0).getFournisseur());
        verify(factureRepository, times(1)).findByFournisseur("ABC Corp");
    }

    @Test
    public void testPayerFacture() {
        Facture facture = new Facture();
        facture.setId(1L);
        facture.setEstPayee(false);
        facture.setMontant(100.0);

        Compte compte = new Compte();
        compte.setSolde(200.0);
        facture.setCompte(compte);

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        String result = factureService.payerFacture(1L);

        assertEquals("Facture payée avec succès.", result);
        assertTrue(facture.getEstPayee());
        assertEquals(100.0, compte.getSolde());
        verify(factureRepository, times(1)).save(facture);
    }

    @Test
    public void testPayerFactureSoldeInsuffisant() {
        Facture facture = new Facture();
        facture.setId(1L);
        facture.setEstPayee(false);
        facture.setMontant(300.0);

        Compte compte = new Compte();
        compte.setSolde(200.0);
        facture.setCompte(compte);

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        String result = factureService.payerFacture(1L);

        assertEquals("Solde insuffisant pour payer cette facture.", result);
        verify(factureRepository, never()).save(facture);
    }

    @Test
    public void testPayerFactureFactureDejaPayee() {
        Facture facture = new Facture();
        facture.setId(1L);
        facture.setEstPayee(true);

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        String result = factureService.payerFacture(1L);

        assertEquals("Cette facture a déjà été payée.", result);
        verify(factureRepository, never()).save(facture);
    }
}
