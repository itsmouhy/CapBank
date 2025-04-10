package com.formula1.capbank.repositories;

import com.formula1.capbank.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Utilisateur, Long> {
}
