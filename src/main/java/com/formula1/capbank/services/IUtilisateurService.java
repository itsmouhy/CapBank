package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Utilisateur.ModifierUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.NewUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;

public interface IUtilisateurService {
    UtilisateurResponse nouveauUtilisateur(NewUtilisateurRequest utilisateur);
    UtilisateurResponse modificationUtilisateur(ModifierUtilisateurRequest utilisateur);
    UtilisateurResponse consultUtilisateur(Long id);
}
