package com.tricol.tricolV2.entity;

import com.tricol.tricolV2.entity.enums.StatutCommande;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de commande est obligatoire")
    @PastOrPresent(message = "La date de commande doit être dans le passé ou aujourd’hui")
    private LocalDateTime dateCommande;

    @NotNull(message = "Le statut de la commande est obligatoire")
    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être supérieur à zéro")
    private BigDecimal montantTotal;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @NotNull(message = "Le fournisseur est obligatoire")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommandeFournisseur> lignes;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MouvementStock> mouvementsStock;
}
