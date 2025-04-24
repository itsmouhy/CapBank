package com.formula1.capbank.repositories;

import com.formula1.capbank.entities.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    Page<Transactions> findByCompteIdOrderByDateDesc(Long compteId, Pageable pageable);
    Optional<List<Transactions>> findByCompteIdOrderByDateDesc(Long compteId);
    @Query("SELECT t.date, t.type, SUM(t.montant) " +
            "FROM Transactions t " +
            "WHERE t.date BETWEEN :start AND :end " +
            "GROUP BY t.date, t.type " +
            "ORDER BY t.date ASC")
    List<Object[]> findStatsJournalieresParCompte(@Param("compteId") Long compteId,
                                                  @Param("start") LocalDate start,
                                                  @Param("end") LocalDate end);

    @Query("""
        SELECT t
        FROM Transactions t
        WHERE t.compte.utilisateur.id= :userId
        ORDER BY t.date DESC
    """)
    Page<Transactions> findByUserIdOrderByDateDesc(@Param("userId") Long userId,
                                                   Pageable pageable);
}
