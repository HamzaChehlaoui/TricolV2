package com.tricol.tricolV2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nom;
    private String description;
    private BigDecimal prixUnitaire;
    private String categorie;
    private BigDecimal stockActuel;
    private BigDecimal coutUnitaireMoyen;

    @OneToMany(mappedBy = "mouvementStock")
    private List<MouvementStock>mouvements;

    @OneToMany(mappedBy = "produit")
    private List<LigneCommandeFournisseur>lignesCommande;
}
