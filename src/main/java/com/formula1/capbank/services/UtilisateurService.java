package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Utilisateur.ModifierUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.NewUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.mappers.UtilisateurMapper;
import com.formula1.capbank.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class UtilisateurService implements IUtilisateurService{
    private UtilisateurRepository utilisateurRepo;
    private UtilisateurMapper utilisateurMapper;
    @Override
    public UtilisateurResponse nouveauUtilisateur(NewUtilisateurRequest utilisateur) {
        // verifier le mail
        Utilisateur u = utilisateurRepo.findByNom(utilisateur.email().trim().toLowerCase()).orElse(null);
        if(u != null)
            throw new IllegalArgumentException("Email existe deja");
        return utilisateurMapper.toResponse(utilisateurRepo.save(Utilisateur.builder()
                .nom(utilisateur.nom().toLowerCase())
                .prenom(utilisateur.prenom().toLowerCase())
                .email(utilisateur.email())
                .password(utilisateur.password())
                .build()));
    }

    @Override
    public UtilisateurResponse modificationUtilisateur(ModifierUtilisateurRequest utilisateur) {
        // verifier les informations recu
        if(utilisateur.id() == null || utilisateur.email() == null)
            throw new IllegalArgumentException("L'identifiant ou l'email a modifié n'est pas assigné");

        // verifier que le nouveau mail n'existe pas deja
        if(utilisateurRepo.findByEmail(utilisateur.email()).orElse(null) != null)
            throw new IllegalArgumentException("L'email existe déja");
        // recherche d'utilisateur
        Utilisateur user = utilisateurRepo.findById(utilisateur.id()).orElse(null);
        if(user == null)
            throw new IllegalArgumentException("L'utilisateur n'existe pas");

        user.setEmail(utilisateur.email());

        return utilisateurMapper.toResponse(utilisateurRepo.save(user));
    }

    @Override
    public UtilisateurResponse consultUtilisateur(Long id) {
        Utilisateur utilisateur;
        if(id.equals(0L)){
        throw new IllegalArgumentException("L'identifiant ne doit pas etre null");
        }
        utilisateur = utilisateurRepo.findById(id).orElse(null);
        if(utilisateur == null)
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        return utilisateurMapper.toResponse(utilisateur);
    }
}
