package com.formula1.capbank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "compte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String numeroCompte;
 private Double solde;
 @ManyToOne
 private Utilisateur utilisateur;
 private LocalDate date;
 @Getter
 @Setter
 private Long rib;
 @OneToMany (mappedBy = "compte")
 private List<Transactions> transactions;

}
