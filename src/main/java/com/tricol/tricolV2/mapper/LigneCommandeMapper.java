package com.tricol.tricolV2.mapper;

import com.tricol.tricolV2.dto.LigneCommandeDTO;
import com.tricol.tricolV2.entity.CommandeFournisseur;
import com.tricol.tricolV2.entity.LigneCommandeFournisseur;
import com.tricol.tricolV2.entity.Produit;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LigneCommandeMapper {

    @Mapping(target = "produit.id", source = "produitId")
    @Mapping(target = "commande.id", source = "commandeId")
    LigneCommandeFournisseur toEntity(LigneCommandeDTO dto);

    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "commandeId", source = "commande.id")
    LigneCommandeDTO toDTO(LigneCommandeFournisseur entity);

    @AfterMapping
    default void ensureRefs(@MappingTarget LigneCommandeFournisseur entity, LigneCommandeDTO dto) {
        if (entity.getProduit() == null && dto.getProduitId() != null) {
            Produit p = new Produit();
            p.setId(dto.getProduitId());
            entity.setProduit(p);
        }
        if (entity.getCommande() == null && dto.getCommandeId() != null) {
            CommandeFournisseur c = new CommandeFournisseur();
            c.setId(dto.getCommandeId());
            entity.setCommande(c);
        }
    }
}
