package com.tricol.tricolV2.repository;

import com.tricol.tricolV2.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Page<Produit> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Produit> findByCategorieContainingIgnoreCase(String categorie, Pageable pageable);
    Page<Produit> findByPrixUnitaireBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    List<Produit> findByStockActuelLessThan(BigDecimal stockMin);
    List<Produit> findByStockActuelGreaterThan(BigDecimal stockMax);

    List<Produit> findByNomIgnoreCaseAndStockActuelGreaterThanOrderByStockActuelDesc(String nom, BigDecimal minStock);
}
