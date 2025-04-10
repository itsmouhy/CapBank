package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Utilisateur.ModifierUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.NewUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.mappers.UtilisateurMapper;
import com.formula1.capbank.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepo;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    void testModificationUtilisateur() {
        ModifierUtilisateurRequest modifierUtilisateurRequest = new ModifierUtilisateurRequest(1L, "new.email@example.com");
        Utilisateur utilisateur = new Utilisateur(1L, "Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com", "password");
        UtilisateurResponse utilisateurResponse = new UtilisateurResponse(1L, "Youssef", "Mouhyeddine", "new.email@example.com");
        when(utilisateurRepo.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        when(utilisateurRepo.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepo.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(utilisateurMapper.toResponse(any(Utilisateur.class))).thenReturn(utilisateurResponse);
        UtilisateurResponse result = utilisateurService.modificationUtilisateur(modifierUtilisateurRequest);
        assertNotNull(result);
        assertEquals("new.email@example.com", result.email());
        verify(utilisateurRepo).findByEmail("new.email@example.com");
        verify(utilisateurRepo).findById(1L);
        verify(utilisateurRepo).save(any(Utilisateur.class));
    }

    @Test
    void testConsultUtilisateur() {
        Utilisateur utilisateur = new Utilisateur(1L, "Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com", "password");
        UtilisateurResponse utilisateurResponse = new UtilisateurResponse(1L, "Youssef", "Mouhyeddine", "Youssef.Mouhyeddine@example.com");

        when(utilisateurRepo.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurMapper.toResponse(any(Utilisateur.class))).thenReturn(utilisateurResponse);

        UtilisateurResponse result = utilisateurService.consultUtilisateur(1L);

        assertNotNull(result);
        assertEquals("Youssef", result.nom());
        assertEquals("Mouhyeddine", result.prenom());
        assertEquals("Youssef.Mouhyeddine@example.com", result.email());

        verify(utilisateurRepo).findById(1L);
    }
}
