package com.tricol.tricolV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurDTO {
    private Long id;
    private String adresse;
    private String societe;
    private String contact;
    private String email;
    private String telephone;
    private String ville;
    private String ice;
}
