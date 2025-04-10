package com.formula1.capbank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "utilisateur")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    @OneToMany (mappedBy = "utilisateur")
    private List<Compte> comptes;


    public Utilisateur(long l, String youssef, String mouhyeddine, String mail, String password) {
        this.id = l;
        this.nom = youssef;
        this.prenom = mouhyeddine;
        this.email = mail;
        this.password = password;
        this.comptes = comptes;
    }
}
