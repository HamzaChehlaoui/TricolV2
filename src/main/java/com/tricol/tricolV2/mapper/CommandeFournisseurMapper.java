package com.tricol.tricolV2.mapper;

import com.tricol.tricolV2.dto.CommandeFournisseurDTO;
import com.tricol.tricolV2.entity.CommandeFournisseur;
import com.tricol.tricolV2.entity.Fournisseur;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {LigneCommandeMapper.class})
public interface CommandeFournisseurMapper {

    @Mapping(target = "fournisseur.id", source = "fournisseurId")
    @Mapping(target = "lignes", source = "lignes")
    @Mapping(target = "mouvementsStock", ignore = true)
    CommandeFournisseur toEntity(CommandeFournisseurDTO dto);

    @Mapping(target = "fournisseurId", source = "fournisseur.id")
    @Mapping(target = "lignes", source = "lignes")
    CommandeFournisseurDTO toDTO(CommandeFournisseur entity);

    @AfterMapping
    default void ensureFournisseur(@MappingTarget CommandeFournisseur entity, CommandeFournisseurDTO dto) {
        if (entity.getFournisseur() == null && dto.getFournisseurId() != null) {
            Fournisseur f = new Fournisseur();
            f.setId(dto.getFournisseurId());
            entity.setFournisseur(f);
        }
        if (entity.getLignes() != null) {
            entity.getLignes().forEach(l -> l.setCommande(entity));
        }
    }
}
