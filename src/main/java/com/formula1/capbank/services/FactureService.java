package com.formula1.capbank.services;

import com.formula1.capbank.entities.Facture;
import com.formula1.capbank.enums.TypeService;
import com.formula1.capbank.repositories.FactureRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureService {
    private final FactureRepository factureRepository;

    public List<Facture> getFacturesByCompte(Long compteId) {
        List<Facture> factures = factureRepository.findByCompteId(compteId);
        System.out.println("Factures trouvées: " + factures.size()); // Vérifie si Hibernate trouve des factures
        return factures;
    }
    @PostConstruct
    public void testRepository() {
        List<Facture> factures = factureRepository.findAll();
        System.out.println("🔍 Factures dans la base: " + factures.size());
    }

    // Liste des factures non payées
    public List<Facture> getFacturesImpayees() {
        return factureRepository.findByEstPayeeFalse();
    }

    // Liste des factures par type de service
    public List<Facture> getFacturesByTypeService(TypeService typeService) {
        return factureRepository.findByTypeService(typeService);
    }

    // Liste des factures par fournisseur
    public List<Facture> getFacturesByFournisseur(String fournisseur) {
        return factureRepository.findByFournisseur(fournisseur);
    }

    // Payer une facture
    public String payerFacture(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        if (facture.getEstPayee()) {
            return "Cette facture a déjà été payée.";
        }

        // Vérifier que le solde du compte est suffisant
        if (facture.getCompte().getSolde() < facture.getMontant()) {
            return "Solde insuffisant pour payer cette facture.";
        }

        // Déduction et mise à jour
        facture.getCompte().setSolde(facture.getCompte().getSolde() - facture.getMontant());
        facture.setEstPayee(true);
        facture.setDatePaiement(LocalDate.now());

        factureRepository.save(facture);
        return "Facture payée avec succès.";
    }
}
