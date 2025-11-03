package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.CommandeFournisseurDTO;
import com.tricol.tricolV2.entity.enums.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurService {

    CommandeFournisseurDTO create(CommandeFournisseurDTO dto);
    CommandeFournisseurDTO update(Long id, CommandeFournisseurDTO dto);
    void delete(Long id);
    Optional<CommandeFournisseurDTO> getById(Long id);
    List<CommandeFournisseurDTO> getAll();
    Page<CommandeFournisseurDTO> getPaged(Pageable pageable);

    CommandeFournisseurDTO updateStatut(Long id, StatutCommande statut);

    Page<CommandeFournisseurDTO> searchByStatut(StatutCommande statut, Pageable pageable);
    Page<CommandeFournisseurDTO> searchByFournisseurSociete(String societe, Pageable pageable);
    Page<CommandeFournisseurDTO> searchByDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
