package com.formula1.capbank.controllers;

import com.formula1.capbank.dtos.Utilisateur.ModifierUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.NewUtilisateurRequest;
import com.formula1.capbank.dtos.Utilisateur.UtilisateurResponse;
import com.formula1.capbank.services.IUtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    private IUtilisateurService utilisateurService;


    @GetMapping("/{id}")
    public UtilisateurResponse consultUtilisateur(@PathVariable Long id){
        return utilisateurService.consultUtilisateur(id);
    }

    @PostMapping("/nouveau")
    public UtilisateurResponse ajouterUtilisateur(@RequestBody NewUtilisateurRequest utilisateur){

        return utilisateurService.nouveauUtilisateur(utilisateur);
    }

    @PutMapping("/modifier/{id}")
    public UtilisateurResponse modifierEmailUtilisateur(@RequestBody ModifierUtilisateurRequest utilisateur){
        return utilisateurService.modificationUtilisateur(utilisateur);
    }
}
