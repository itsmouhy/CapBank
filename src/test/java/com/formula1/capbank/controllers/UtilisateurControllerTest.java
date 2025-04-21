package com.formula1.capbank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formula1.capbank.dtos.Utilisateur.ModifierUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.NewUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.services.IUtilisateurService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
@ExtendWith(SpringExtension.class)
@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUtilisateurService utilisateurService;

    @Test
    void testConsultUtilisateur() throws Exception {
        UtilisateurResponse utilisateurResponse = new UtilisateurResponse(1L, "Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com");

        when(utilisateurService.consultUtilisateur(1L)).thenReturn(utilisateurResponse);

        mockMvc.perform(get("/api/utilisateur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("Youssef"))
                .andExpect(jsonPath("$.prenom").value("Mouhyeddine"))
                .andExpect(jsonPath("$.email").value("Youssef.Mouhyeddine@example.com"));
    }

    @Test
    void testAjouterUtilisateur() throws Exception {
        NewUtilisateurRequest newUtilisateurRequest = new NewUtilisateurRequest("Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com", "password");

        UtilisateurResponse utilisateurResponse = new UtilisateurResponse(1L, "Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com");

        when(utilisateurService.nouveauUtilisateur(any(NewUtilisateurRequest.class))).thenReturn(utilisateurResponse);

        mockMvc.perform(post("/api/utilisateur/nouveau")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUtilisateurRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("Youssef"))
                .andExpect(jsonPath("$.prenom").value("Mouhyeddine"))
                .andExpect(jsonPath("$.email").value("Youssef.Mouhyeddine@example.com"));
    }

    @Test
    void testModifierEmailUtilisateur() throws Exception {
        ModifierUtilisateurRequest modifierUtilisateurRequest = new ModifierUtilisateurRequest(1L, "new.email@example.com");

        UtilisateurResponse utilisateurResponse = new UtilisateurResponse(1L, "Youssef", "Mouhyeddine", "new.email@example.com");

        when(utilisateurService.modificationUtilisateur(any(ModifierUtilisateurRequest.class))).thenReturn(utilisateurResponse);

        mockMvc.perform(put("/api/utilisateur/modifier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(modifierUtilisateurRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new.email@example.com"));
    }
}
*/
