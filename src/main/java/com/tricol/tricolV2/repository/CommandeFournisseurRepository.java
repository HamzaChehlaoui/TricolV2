package com.tricol.tricolV2.repository;

import com.tricol.tricolV2.entity.CommandeFournisseur;
import com.tricol.tricolV2.entity.Fournisseur;
import com.tricol.tricolV2.entity.enums.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Long> {

    List<CommandeFournisseur> findByFournisseur(Fournisseur fournisseur);
    List<CommandeFournisseur> findByStatut(StatutCommande statut);
    List<CommandeFournisseur> findByDateCommande(LocalDateTime dateCommande);
    List<CommandeFournisseur> findByDateCommandeBetween(LocalDateTime start, LocalDateTime end);
    List<CommandeFournisseur> findByMontantTotalBetween(BigDecimal min, BigDecimal max);
    Page<CommandeFournisseur> findByStatut(StatutCommande statut, Pageable pageable);
    Page<CommandeFournisseur> findByFournisseur_SocieteContainingIgnoreCase(String societe, Pageable pageable);
    Page<CommandeFournisseur> findByDateCommandeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
