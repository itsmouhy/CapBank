package com.formula1.capbank.repositories;

import com.formula1.capbank.entities.Facture;
import com.formula1.capbank.enums.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByCompteId(Long compteId);
    List<Facture> findByEstPayeeFalse();
    List<Facture> findByTypeService(TypeService typeService);
    List<Facture> findByFournisseur(String fournisseur);

}
