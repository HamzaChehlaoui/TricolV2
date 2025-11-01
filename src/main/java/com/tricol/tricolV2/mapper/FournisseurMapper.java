package com.tricol.tricolV2.mapper;

import com.tricol.tricolV2.dto.FournisseurDTO;
import com.tricol.tricolV2.entity.Fournisseur;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {

        Fournisseur toEntity(FournisseurDTO dto);

        FournisseurDTO toDTO(Fournisseur fournisseur);

        List<FournisseurDTO> toDTOs(List<Fournisseur> fournisseurs);
}
