package com.formula1.capbank.controllers;

import com.formula1.capbank.entities.Facture;
import com.formula1.capbank.enums.TypeService;
import com.formula1.capbank.services.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {
    private final FactureService factureService;

    @GetMapping("/{compteId}")
    public ResponseEntity<List<Facture>> getFacturesByCompte(@PathVariable Long compteId) {
        return ResponseEntity.ok(factureService.getFacturesByCompte(compteId));
    }

    @GetMapping("/impayees")
    public ResponseEntity<List<Facture>> getFacturesImpayees() {
        return ResponseEntity.ok(factureService.getFacturesImpayees());
    }

    @GetMapping("/type/{typeService}")
    public ResponseEntity<List<Facture>> getFacturesByType(@PathVariable TypeService typeService) {
        return ResponseEntity.ok(factureService.getFacturesByTypeService(typeService));
    }

    @GetMapping("/fournisseur/{fournisseur}")
    public ResponseEntity<List<Facture>> getFacturesByFournisseur(@PathVariable String fournisseur) {
        return ResponseEntity.ok(factureService.getFacturesByFournisseur(fournisseur));
    }

    @PostMapping("/payer/{factureId}")
    public ResponseEntity<String> payerFacture(@PathVariable Long factureId) {
        return ResponseEntity.ok(factureService.payerFacture(factureId));
    }
}
