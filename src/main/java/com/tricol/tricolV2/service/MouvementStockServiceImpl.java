package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.MouvementStockDTO;
import com.tricol.tricolV2.entity.CommandeFournisseur;
import com.tricol.tricolV2.entity.LigneCommandeFournisseur;
import com.tricol.tricolV2.entity.MouvementStock;
import com.tricol.tricolV2.entity.Produit;
import com.tricol.tricolV2.entity.enums.TypeMouvement;
import com.tricol.tricolV2.repository.MouvementStockRepository;
import com.tricol.tricolV2.repository.ProduitRepository;
import com.tricol.tricolV2.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;

    public MouvementStockServiceImpl(MouvementStockRepository mouvementRepository,
                                     ProduitRepository produitRepository) {
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
    }

    @Override
    @Transactional
    public void createEntriesForCommande(CommandeFournisseur commande) {
        if (commande == null || commande.getLignes() == null) {
            return;
        }

        for (LigneCommandeFournisseur ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();
            BigDecimal quantiteDemandee = defaultZero(ligne.getQuantite());

            if (quantiteDemandee.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal restante = quantiteDemandee;
            var candidats = produitRepository
                    .findByNomIgnoreCaseAndStockActuelGreaterThanOrderByStockActuelDesc(
                            produit.getNom(), BigDecimal.ZERO);

            // Pré-vérification: si le stock total disponible < demandé, lever une erreur avant toute sortie
            BigDecimal totalDisponible = candidats.stream()
                    .map(c -> defaultZero(c.getStockActuel()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalDisponible.compareTo(quantiteDemandee) < 0) {
                throw new BusinessException("La quantité demandée n'est pas disponible en stock pour le produit: " + produit.getNom());
            }

            for (Produit candidat : candidats) {
                if (restante.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal dispo = defaultZero(candidat.getStockActuel());
                if (dispo.compareTo(BigDecimal.ZERO) <= 0) continue;

                BigDecimal aSortir = dispo.min(restante);

                BigDecimal coutUnitaire = defaultZero(candidat.getPrixUnitaire());

                MouvementStock mouvement = MouvementStock.builder()
                        .dateMouvement(LocalDateTime.now())
                        .type(TypeMouvement.SORTIE)
                        .quantite(aSortir)
                        .coutUnitaire(coutUnitaire)
                        .produit(candidat)
                        .commande(commande)
                        .commentaire("Sortie (allocation multi-produits) commande " + commande.getId())
                        .build();
                mouvementRepository.save(mouvement);

                BigDecimal nouveauStock = dispo.subtract(aSortir);
                candidat.setStockActuel(nouveauStock.max(BigDecimal.ZERO));
                // CUMP inchangé pour les sorties
                produitRepository.save(candidat);

                restante = restante.subtract(aSortir);
            }

            if (restante.compareTo(BigDecimal.ZERO) > 0) {
                // Défense en profondeur: ceci ne devrait pas arriver après la pré-vérification
                throw new BusinessException("La quantité demandée n'est pas disponible en stock pour le produit: " + produit.getNom());
            }
        }
    }

    @Override
    public boolean movementsExistForCommande(Long commandeId) {
        return mouvementRepository.existsByCommande_Id(commandeId);
    }

    @Override
    public Page<MouvementStockDTO> getPaged(Pageable pageable) {
        return mouvementRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public Page<MouvementStockDTO> getByProduit(Long produitId, Pageable pageable) {
        return mouvementRepository.findByProduit_Id(produitId, pageable).map(this::toDto);
    }

    @Override
    public Page<MouvementStockDTO> getByCommande(Long commandeId, Pageable pageable) {
        return mouvementRepository.findByCommande_Id(commandeId, pageable).map(this::toDto);
    }

    @Override
    public Page<MouvementStockDTO> getByType(TypeMouvement type, Pageable pageable) {
        return mouvementRepository.findByType(type, pageable).map(this::toDto);
    }

    private MouvementStockDTO toDto(MouvementStock m) {
        return MouvementStockDTO.builder()
                .id(m.getId())
                .dateMouvement(m.getDateMouvement())
                .type(m.getType())
                .quantite(m.getQuantite())
                .coutUnitaire(m.getCoutUnitaire())
                .produitId(m.getProduit() != null ? m.getProduit().getId() : null)
                .commandeId(m.getCommande() != null ? m.getCommande().getId() : null)
                .commentaire(m.getCommentaire())
                .build();
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return Objects.requireNonNullElse(value, BigDecimal.ZERO);
    }
}


