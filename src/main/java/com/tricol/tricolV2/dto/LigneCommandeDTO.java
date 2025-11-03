package com.tricol.tricolV2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeDTO {

    private Long id;

    @NotNull(message = "La quantité est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "La quantité doit être supérieure à zéro")
    private BigDecimal quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être supérieur à zéro")
    private BigDecimal prixUnitaire;

    private BigDecimal montantLigne;

    @NotNull(message = "Le produit est obligatoire")
    private Long produitId;

    private Long commandeId;
}
