package com.formula1.capbank.dtos.Utilisateur;


import com.formula1.capbank.dtos.Compte.CompteResponse;
import java.util.List;

public record UtilisateurResponse(Long id, String nom, String prenom, String email, List<CompteResponse> comptes) {

}
