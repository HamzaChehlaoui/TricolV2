package com.tricol.tricolV2.entity;

import com.tricol.tricolV2.entity.enums.StatutCommande;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CommandeFournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCommande;
    private StatutCommande statut;
    private BigDecimal montantTotal;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur", cascade = CascadeType.ALL)
    private List<LigneCommandeFournisseur>lignes;
    private List <MouvementStock>mouvementsStock;

}
