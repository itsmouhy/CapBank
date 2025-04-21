package com.formula1.capbank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formula1.capbank.dtos.Compte.NouveauCompterequest;
import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.services.CompteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/*@ExtendWith(SpringExtension.class)
@WebMvcTest(CompteController.class)
public class CompteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompteService compteService;

    @Test
    void testNouveauCompte() throws Exception {
        NouveauCompterequest compteRequest = new NouveauCompterequest(1L, 500.0);
        CompteResponse compteResponse = new CompteResponse(1L, 500.0, LocalDate.now(), 1L);
        when(compteService.nouveauCompte(any(NouveauCompterequest.class))).thenReturn(compteResponse);
        mockMvc.perform(post("/api/compte/nouveau")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(compteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(500.0));
    }
}*/
