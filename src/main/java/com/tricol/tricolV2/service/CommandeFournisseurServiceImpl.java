package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.CommandeFournisseurDTO;
import com.tricol.tricolV2.entity.CommandeFournisseur;
import com.tricol.tricolV2.entity.Fournisseur;
import com.tricol.tricolV2.entity.LigneCommandeFournisseur;
import com.tricol.tricolV2.entity.Produit;
import com.tricol.tricolV2.entity.enums.StatutCommande;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.mapper.CommandeFournisseurMapper;
import com.tricol.tricolV2.mapper.LigneCommandeMapper;
import com.tricol.tricolV2.repository.CommandeFournisseurRepository;
import com.tricol.tricolV2.repository.FournisseurRepository;
import com.tricol.tricolV2.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final CommandeFournisseurMapper commandeMapper;
    private final LigneCommandeMapper ligneMapper;
    private final MouvementStockService mouvementStockService;

    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeRepository,
                                          FournisseurRepository fournisseurRepository,
                                          ProduitRepository produitRepository,
                                          CommandeFournisseurMapper commandeMapper,
                                          LigneCommandeMapper ligneMapper,
                                          MouvementStockService mouvementStockService) {
        this.commandeRepository = commandeRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.produitRepository = produitRepository;
        this.commandeMapper = commandeMapper;
        this.ligneMapper = ligneMapper;
        this.mouvementStockService = mouvementStockService;
    }

    @Override
    public CommandeFournisseurDTO create(CommandeFournisseurDTO dto) {
        Fournisseur fournisseur = fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé avec l'id : " + dto.getFournisseurId()));

        CommandeFournisseur entity = commandeMapper.toEntity(dto);
        entity.setId(null);
        entity.setFournisseur(fournisseur);

        // Ensure bidirectional association is set before persisting
        if (entity.getLignes() != null) {
            for (LigneCommandeFournisseur ligne : entity.getLignes()) {
                ligne.setCommande(entity);
            }
        }

        bindProduitsAndCompute(entity);

        CommandeFournisseur saved = commandeRepository.save(entity);
        return commandeMapper.toDTO(saved);
    }

    @Override
    public CommandeFournisseurDTO update(Long id, CommandeFournisseurDTO dto) {
        CommandeFournisseur existing = commandeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'id : " + id));

        if (dto.getFournisseurId() != null) {
            Fournisseur fournisseur = fournisseurRepository.findById(dto.getFournisseurId())
                    .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé avec l'id : " + dto.getFournisseurId()));
            existing.setFournisseur(fournisseur);
        }

        existing.setDateCommande(dto.getDateCommande());
        existing.setStatut(dto.getStatut());

        // reset lines and rebuild
        existing.getLignes().clear();
        List<LigneCommandeFournisseur> lignes = dto.getLignes().stream()
                .map(ligneMapper::toEntity)
                .peek(l -> l.setCommande(existing))
                .collect(Collectors.toList());
        existing.getLignes().addAll(lignes);

        bindProduitsAndCompute(existing);

        CommandeFournisseur updated = commandeRepository.save(existing);
        return commandeMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        CommandeFournisseur existing = commandeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'id : " + id));
        commandeRepository.delete(existing);
    }

    @Override
    public Optional<CommandeFournisseurDTO> getById(Long id) {
        return commandeRepository.findById(id).map(commandeMapper::toDTO);
    }

    @Override
    public List<CommandeFournisseurDTO> getAll() {
        return commandeRepository.findAll().stream().map(commandeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<CommandeFournisseurDTO> getPaged(Pageable pageable) {
        return commandeRepository.findAll(pageable).map(commandeMapper::toDTO);
    }

    @Override
    @Transactional
    public CommandeFournisseurDTO updateStatut(Long id, StatutCommande statut) {
        CommandeFournisseur existing = commandeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'id : " + id));
        boolean wasLivree = existing.getStatut() == StatutCommande.LIVREE;
        // If moving to LIVREE, ensure stock movements succeed BEFORE persisting status change
        if (!wasLivree && statut == StatutCommande.LIVREE) {
            if (!mouvementStockService.movementsExistForCommande(existing.getId())) {
                // Will throw BusinessException if stock insufficient; transaction will rollback
                mouvementStockService.createEntriesForCommande(existing);
            }
        }

        existing.setStatut(statut);
        CommandeFournisseur updated = commandeRepository.save(existing);
        return commandeMapper.toDTO(updated);
    }

    @Override
    public Page<CommandeFournisseurDTO> searchByStatut(StatutCommande statut, Pageable pageable) {
        return commandeRepository.findByStatut(statut, pageable).map(commandeMapper::toDTO);
    }

    @Override
    public Page<CommandeFournisseurDTO> searchByFournisseurSociete(String societe, Pageable pageable) {
        return commandeRepository.findByFournisseur_SocieteContainingIgnoreCase(societe, pageable)
                .map(commandeMapper::toDTO);
    }

    @Override
    public Page<CommandeFournisseurDTO> searchByDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return commandeRepository.findByDateCommandeBetween(start, end, pageable)
                .map(commandeMapper::toDTO);
    }

    private void bindProduitsAndCompute(CommandeFournisseur commande) {
        BigDecimal total = BigDecimal.ZERO;
        if (commande.getLignes() != null) {
            for (LigneCommandeFournisseur ligne : commande.getLignes()) {
                Produit produit = produitRepository.findById(ligne.getProduit().getId())
                        .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'id : " + ligne.getProduit().getId()));
                ligne.setProduit(produit);

                BigDecimal qte = ligne.getQuantite() == null ? BigDecimal.ZERO : ligne.getQuantite();
                BigDecimal pu = produit.getPrixUnitaire() == null ? BigDecimal.ZERO : produit.getPrixUnitaire();
                ligne.setPrixUnitaire(pu);
                BigDecimal montantLigne = pu.multiply(qte).setScale(2, RoundingMode.HALF_UP);
                ligne.setMontantLigne(montantLigne);
                total = total.add(montantLigne);
            }
        }
        commande.setMontantTotal(total.setScale(2, RoundingMode.HALF_UP));
    }
}
