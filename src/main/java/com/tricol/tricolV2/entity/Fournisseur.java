package com.tricol.tricolV2.entity;

import com.tricol.tricolV2.controller.CommandeFournisseure;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adresse;
    private String societe;
    private String contact;
    private String email;
    private String telephone;
    private String ville;
    private String ice;

    @OneToMany(mappedBy = "fournisseur" , cascade = CascadeType.ALL)
    private List<CommandeFournisseur> commandes;




}
