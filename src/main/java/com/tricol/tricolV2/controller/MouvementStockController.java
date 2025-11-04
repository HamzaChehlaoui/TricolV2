package com.tricol.tricolV2.controller;

import com.tricol.tricolV2.dto.MouvementStockDTO;
import com.tricol.tricolV2.entity.enums.TypeMouvement;
import com.tricol.tricolV2.service.MouvementStockService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementStockController {

    private final MouvementStockService mouvementService;

    public MouvementStockController(MouvementStockService mouvementService) {
        this.mouvementService = mouvementService;
    }

    @GetMapping
    public Page<MouvementStockDTO> getPaged(@ParameterObject Pageable pageable) {
        return mouvementService.getPaged(pageable);
    }

    @GetMapping(params = "produitId")
    public Page<MouvementStockDTO> byProduit(@RequestParam Long produitId, @ParameterObject Pageable pageable) {
        return mouvementService.getByProduit(produitId, pageable);
    }

    @GetMapping(params = "commandeId")
    public Page<MouvementStockDTO> byCommande(@RequestParam Long commandeId, @ParameterObject Pageable pageable) {
        return mouvementService.getByCommande(commandeId, pageable);
    }

    @GetMapping(params = "type")
    public Page<MouvementStockDTO> byType(@RequestParam TypeMouvement type, @ParameterObject Pageable pageable) {
        return mouvementService.getByType(type, pageable);
    }
}
