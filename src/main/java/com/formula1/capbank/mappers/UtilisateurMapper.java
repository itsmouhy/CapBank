package com.formula1.capbank.mappers;

import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.entities.Utilisateur;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurMapper {
    public UtilisateurResponse toResponse(Utilisateur u){
        return new UtilisateurResponse(u.getId(),u.getNom(),
                u.getPrenom(),u.getEmail());
    }
}
