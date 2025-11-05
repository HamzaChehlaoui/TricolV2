package com.tricol.tricolV2.service;

import com.tricol.tricolV2.config.AppProperties;
import com.tricol.tricolV2.dto.ProduitDTO;
import com.tricol.tricolV2.entity.MouvementStock;
import com.tricol.tricolV2.entity.Produit;
import com.tricol.tricolV2.entity.enums.TypeMouvement;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.mapper.ProduitMapper;
import com.tricol.tricolV2.repository.MouvementStockRepository;
import com.tricol.tricolV2.repository.ProduitRepository;
import com.tricol.tricolV2.util.ValorisationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final ProduitMapper produitMapper;
    private final AppProperties appProperties;

    public ProduitServiceImpl(ProduitRepository produitRepository,
                              MouvementStockRepository mouvementStockRepository,
                              ProduitMapper produitMapper,
                              AppProperties appProperties) {
        this.produitRepository = produitRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.produitMapper = produitMapper;
        this.appProperties = appProperties;
    }

    @Override
    public ProduitDTO addProduit(ProduitDTO produitDTO) {
        Produit produit = produitMapper.toEntity(produitDTO);
        Produit saved = produitRepository.save(produit);

        BigDecimal qty = defaultZero(saved.getStockActuel());
        if (qty.compareTo(BigDecimal.ZERO) > 0) {
            MouvementStock mvt = MouvementStock.builder()
                    .dateMouvement(LocalDateTime.now())
                    .type(TypeMouvement.ENTREE)
                    .quantite(qty)
                    .coutUnitaire(defaultZero(saved.getPrixUnitaire()))
                    .produit(saved)
                    .commentaire("Entrée initiale produit")
                    .build();
            mouvementStockRepository.save(mvt);

            BigDecimal newAvg = computeAvgCost(BigDecimal.ZERO, BigDecimal.ZERO, qty, defaultZero(saved.getPrixUnitaire()));
            saved.setCoutUnitaireMoyen(newAvg);
            produitRepository.save(saved);
        }

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
        BigDecimal oldStock = defaultZero(existing.getStockActuel());
        BigDecimal newStock = defaultZero(produitDTO.getStockActuel());
        existing.setStockActuel(newStock);

        Produit updated = produitRepository.save(existing);

        BigDecimal delta = newStock.subtract(oldStock);
        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            MouvementStock mvt = MouvementStock.builder()
                    .dateMouvement(LocalDateTime.now())
                    .type(TypeMouvement.ENTREE)
                    .quantite(delta)
                    .coutUnitaire(defaultZero(updated.getPrixUnitaire()))
                    .produit(updated)
                    .commentaire("Entrée suite à mise à jour produit")
                    .build();
            mouvementStockRepository.save(mvt);

            BigDecimal currentAvg = defaultZero(updated.getCoutUnitaireMoyen());
            BigDecimal newAvg = computeAvgCost(oldStock, currentAvg, delta, defaultZero(updated.getPrixUnitaire()));
            updated.setCoutUnitaireMoyen(newAvg);
            produitRepository.save(updated);
        }

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

    private BigDecimal computeAvgCost(BigDecimal currentQty,
                                      BigDecimal currentAvgCost,
                                      BigDecimal incomingQty,
                                      BigDecimal incomingUnitCost) {
        AppProperties.ValuationMethod method = appProperties.getValuationMethod();
        if (method == AppProperties.ValuationMethod.CUMP) {
            return ValorisationUtil.calculateCump(currentQty, currentAvgCost, incomingQty, incomingUnitCost);
        }
        return incomingUnitCost;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
