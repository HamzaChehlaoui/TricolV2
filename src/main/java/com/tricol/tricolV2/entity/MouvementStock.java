package com.tricol.tricolV2.entity;


import com.tricol.tricolV2.entity.enums.TypeMouvement;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private LocalDateTime dateMouvement;
    private TypeMouvement type ;
    private BigDecimal quantite;
    private BigDecimal coutUnitaire;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private CommandeFournisseur commande;
    private String commentaire ;
}
