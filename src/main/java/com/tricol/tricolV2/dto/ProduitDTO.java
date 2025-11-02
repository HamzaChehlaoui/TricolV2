package com.tricol.tricolV2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProduitDTO {

    private Long id;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom du produit ne doit pas dépasser 100 caractères")
    private String nom;

    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être supérieur à zéro")
    private BigDecimal prixUnitaire;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(max = 100, message = "La catégorie ne doit pas dépasser 100 caractères")
    private String categorie;

    @NotNull(message = "Le stock actuel est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le stock actuel ne peut pas être négatif")
    private BigDecimal stockActuel;

    @NotNull(message = "Le coût unitaire moyen est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le coût unitaire moyen ne peut pas être négatif")
    private BigDecimal coutUnitaireMoyen;
}
