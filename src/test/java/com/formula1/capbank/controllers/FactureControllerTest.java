package com.formula1.capbank.controllers;

import com.formula1.capbank.entities.Facture;
import com.formula1.capbank.enums.TypeService;
import com.formula1.capbank.services.FactureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FactureControllerTest {

    @Mock
    private FactureService factureService;

    @InjectMocks
    private FactureController factureController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(factureController).build();
    }

    @Test
    void getFacturesByCompte_shouldReturnFactures() throws Exception {
        Long compteId = 1L;
        Facture facture = new Facture();  // Create a sample facture object
        List<Facture> factures = Arrays.asList(facture);

        when(factureService.getFacturesByCompte(compteId)).thenReturn(factures);

        mockMvc.perform(get("/api/factures/{compteId}", compteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(factureService, times(1)).getFacturesByCompte(compteId);
    }

    @Test
    void getFacturesImpayees_shouldReturnFactures() throws Exception {
        Facture facture = new Facture();  // Create a sample facture object
        List<Facture> factures = Arrays.asList(facture);

        when(factureService.getFacturesImpayees()).thenReturn(factures);

        mockMvc.perform(get("/api/factures/impayees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(factureService, times(1)).getFacturesImpayees();
    }

    @Test
    void getFacturesByType_shouldReturnFactures() throws Exception {
        TypeService typeService = TypeService.TELECOM;
        Facture facture = new Facture();  // Create a sample facture object
        List<Facture> factures = Arrays.asList(facture);

        when(factureService.getFacturesByTypeService(typeService)).thenReturn(factures);

        mockMvc.perform(get("/api/factures/type/{typeService}", typeService))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(factureService, times(1)).getFacturesByTypeService(typeService);
    }

    @Test
    void getFacturesByFournisseur_shouldReturnFactures() throws Exception {
        String fournisseur = "SomeFournisseur";
        Facture facture = new Facture();  // Create a sample facture object
        List<Facture> factures = Arrays.asList(facture);

        when(factureService.getFacturesByFournisseur(fournisseur)).thenReturn(factures);

        mockMvc.perform(get("/api/factures/fournisseur/{fournisseur}", fournisseur))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(factureService, times(1)).getFacturesByFournisseur(fournisseur);
    }

    @Test
    void payerFacture_shouldReturnSuccessMessage() throws Exception {
        Long factureId = 1L;
        String expectedResponse = "Facture paid successfully";

        when(factureService.payerFacture(factureId)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/factures/payer/{factureId}", factureId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(factureService, times(1)).payerFacture(factureId);
    }
}
