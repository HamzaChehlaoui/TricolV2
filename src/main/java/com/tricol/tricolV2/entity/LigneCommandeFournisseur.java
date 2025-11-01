package com.tricol.tricolV2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La quantité est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "La quantité doit être supérieure à zéro")
    private BigDecimal quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être supérieur à zéro")
    private BigDecimal prixUnitaire;

    @NotNull(message = "Le montant de la ligne est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant de la ligne doit être supérieur à zéro")
    private BigDecimal montantLigne;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    @NotNull(message = "Le produit est obligatoire")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    @NotNull(message = "La commande fournisseur est obligatoire")
    private CommandeFournisseur commande;
}
