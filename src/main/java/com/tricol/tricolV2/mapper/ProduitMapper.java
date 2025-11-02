package com.tricol.tricolV2.mapper;

import com.tricol.tricolV2.dto.ProduitDTO;
import com.tricol.tricolV2.entity.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    @Mapping(target = "mouvements", ignore = true)
    @Mapping(target = "lignesCommande", ignore = true)
    Produit toEntity(ProduitDTO dto);

    ProduitDTO toDTO(Produit produit);

    List<ProduitDTO> toDTOs(List<Produit> produits);
}
