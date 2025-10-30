package com.tricol.tricolV2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class LigneCommandeFournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne ;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private CommandeFournisseur commande;
}
