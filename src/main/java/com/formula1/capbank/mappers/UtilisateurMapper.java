package com.formula1.capbank.mappers;

import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurMapper {
    private final CompteMapper compteMapper;

    public UtilisateurMapper(CompteMapper compteMapper) {
        this.compteMapper = compteMapper;
    }

    public UtilisateurResponse toResponse(Utilisateur u){
        return new UtilisateurResponse(
                u.getId(),
                u.getNom(),
                u.getPrenom(),
                u.getEmail(),
                Optional.ofNullable(u.getComptes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(compteMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }
}
