package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.ProduitDTO;
import com.tricol.tricolV2.entity.Produit;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.mapper.ProduitMapper;
import com.tricol.tricolV2.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ProduitServiceImpl(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }

    @Override
    public ProduitDTO addProduit(ProduitDTO produitDTO) {
        Produit produit = produitMapper.toEntity(produitDTO);
        System.out.println(produit);
        Produit saved = produitRepository.save(produit);
        return produitMapper.toDTO(saved);
    }

    @Override
    public ProduitDTO updateProduit(Long id, ProduitDTO produitDTO) {
        Produit existing = produitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'id : " + id));

        existing.setNom(produitDTO.getNom());
        existing.setDescription(produitDTO.getDescription());
        existing.setCategorie(produitDTO.getCategorie());
        existing.setPrixUnitaire(produitDTO.getPrixUnitaire());
        existing.setStockActuel(produitDTO.getStockActuel());
        existing.setCoutUnitaireMoyen(produitDTO.getCoutUnitaireMoyen());

        Produit updated = produitRepository.save(existing);
        return produitMapper.toDTO(updated);
    }

    @Override
    public void deleteProduit(Long id) {
        Produit existing = produitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'id : " + id));
        produitRepository.delete(existing);
    }

    @Override
    public Optional<ProduitDTO> getProduitById(Long id) {
        return produitRepository.findById(id).map(produitMapper::toDTO);
    }

    @Override
    public List<ProduitDTO> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProduitDTO> getProduits(Pageable pageable) {
        return produitRepository.findAll(pageable).map(produitMapper::toDTO);
    }

    @Override
    public Page<ProduitDTO> searchByNom(String nom, Pageable pageable) {
        return produitRepository.findByNomContainingIgnoreCase(nom, pageable).map(produitMapper::toDTO);
    }

    @Override
    public Page<ProduitDTO> searchByCategorie(String categorie, Pageable pageable) {
        return produitRepository.findByCategorieContainingIgnoreCase(categorie, pageable)
                .map(produitMapper::toDTO);
    }

    @Override
    public Page<ProduitDTO> searchByPrixBetween(BigDecimal min, BigDecimal max, Pageable pageable) {
        return produitRepository.findByPrixUnitaireBetween(min, max, pageable).map(produitMapper::toDTO);
    }

    @Override
    public List<ProduitDTO> searchByStockLessThan(BigDecimal stockMin) {
        return produitRepository.findByStockActuelLessThan(stockMin).stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitDTO> searchByStockGreaterThan(BigDecimal stockMax) {
        return produitRepository.findByStockActuelGreaterThan(stockMax).stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }
}
