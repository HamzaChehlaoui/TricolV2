package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.ProduitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProduitService {

    ProduitDTO addProduit(ProduitDTO produitDTO);
    ProduitDTO updateProduit(Long id, ProduitDTO produitDTO);
    void deleteProduit(Long id);
    Optional<ProduitDTO> getProduitById(Long id);
    List<ProduitDTO> getAllProduits();
    Page<ProduitDTO> getProduits(Pageable pageable);
    Page<ProduitDTO> searchByNom(String nom, Pageable pageable);
    Page<ProduitDTO> searchByCategorie(String categorie, Pageable pageable);
    Page<ProduitDTO> searchByPrixBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    List<ProduitDTO> searchByStockLessThan(BigDecimal stockMin);
    List<ProduitDTO> searchByStockGreaterThan(BigDecimal stockMax);
    Long cout();
}
