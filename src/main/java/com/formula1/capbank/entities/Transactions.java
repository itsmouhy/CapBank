package com.formula1.capbank.entities;

import com.formula1.capbank.enums.TransactionStatus;
import com.formula1.capbank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "compte_id", nullable = false)
    private Compte compte;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    private Double montant;
    @Column(nullable = false)
    private LocalDate date;
    private String description;
    @Column(nullable = false)
    private TransactionStatus status;
    @Column(name = "num_compte_recip", length = 30)
    private String noCompteRecipient;

}
