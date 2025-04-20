package com.formula1.capbank.repositories;

import com.formula1.capbank.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte,Long> {
    Optional<Compte> findByRib(Long rib);
    Optional<Compte> findByNumeroCompte(String numeroCompte);
    Boolean existsByNumeroCompte(String numeroCompte);
}
