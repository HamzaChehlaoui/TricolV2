package com.tricol.tricolV2.controller;

import com.tricol.tricolV2.dto.CommandeFournisseurDTO;
import com.tricol.tricolV2.entity.enums.StatutCommande;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.service.CommandeFournisseurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/commandes-fournisseur")
public class CommandeFournisseure {

    private final CommandeFournisseurService service;

    public CommandeFournisseure(CommandeFournisseurService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CommandeFournisseurDTO> create(@Valid @RequestBody CommandeFournisseurDTO dto) {
        CommandeFournisseurDTO created = service.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeFournisseurDTO> update(@PathVariable("id") Long id,
                                                         @Valid @RequestBody CommandeFournisseurDTO dto) {
        CommandeFournisseurDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<CommandeFournisseurDTO> updateStatut(@PathVariable("id") Long id,
                                                               @RequestParam("value") StatutCommande statut) {
        CommandeFournisseurDTO updated = service.updateStatut(id, statut);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Commande supprimée avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeFournisseurDTO> getById(@PathVariable("id") Long id) {
        CommandeFournisseurDTO dto = service.getById(id)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'id : " + id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<CommandeFournisseurDTO>> getAll() {
        List<CommandeFournisseurDTO> list = service.getAll();
        if (list.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getPaged(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommandeFournisseurDTO> p = service.getPaged(pageable);
        if (p.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", p.getContent());
        response.put("currentPage", p.getNumber());
        response.put("totalItems", p.getTotalElements());
        response.put("totalPages", p.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/statut")
    public ResponseEntity<Page<CommandeFournisseurDTO>> searchByStatut(@RequestParam("value") StatutCommande statut,
                                                                       Pageable pageable) {
        Page<CommandeFournisseurDTO> p = service.searchByStatut(statut, pageable);
        if (p.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée pour le statut : " + statut);
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/search/fournisseur")
    public ResponseEntity<Page<CommandeFournisseurDTO>> searchByFournisseurSociete(@RequestParam("q") String societe,
                                                                                   Pageable pageable) {
        Page<CommandeFournisseurDTO> p = service.searchByFournisseurSociete(societe, pageable);
        if (p.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée pour le fournisseur : " + societe);
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/search/dateBetween")
    public ResponseEntity<Page<CommandeFournisseurDTO>> searchByDateBetween(@RequestParam("start") LocalDateTime start,
                                                                            @RequestParam("end") LocalDateTime end,
                                                                            Pageable pageable) {
        Page<CommandeFournisseurDTO> p = service.searchByDateBetween(start, end, pageable);
        if (p.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée dans cet intervalle de dates");
        }
        return ResponseEntity.ok(p);
    }
}
