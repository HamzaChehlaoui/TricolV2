package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.FournisseurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface FournisseurService {
    FournisseurDTO addFournisseur(FournisseurDTO dto);
    FournisseurDTO updateFournisseur(Long id, FournisseurDTO dto);
    void deleteFournisseur(Long id);
    Optional<FournisseurDTO> getFournisseurById(Long id);
    List<FournisseurDTO> getAllFournisseurs();
    List<FournisseurDTO> searchBySociete(String societe);
    List<FournisseurDTO> searchByVille(String ville);
    Optional<FournisseurDTO> searchByIce(String ice);
    Page<FournisseurDTO> getFournisseurs(Pageable pageable);
}
