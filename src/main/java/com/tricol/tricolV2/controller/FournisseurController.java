package com.tricol.tricolV2.controller;

import com.tricol.tricolV2.dto.FournisseurDTO;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.service.FournisseurServiceImpl;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/fournisseurs")
public class FournisseurController {

    private final FournisseurServiceImpl fournisseurService;
    public FournisseurController(FournisseurServiceImpl fournisseurService){
        this.fournisseurService =fournisseurService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO>getFournisseurById(@PathVariable("id") Long id){
       FournisseurDTO dto = fournisseurService.getFournisseurById(id)
               .orElseThrow(()->new NotFoundException("Fournisseur non trouvé avec l'id : " + id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<FournisseurDTO>addFournisseur(@Valid @RequestBody  FournisseurDTO fournisseurDTO){

        FournisseurDTO dto = fournisseurService.addFournisseur(fournisseurDTO);

        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO>updateFournisseur(@PathVariable("id") Long id , @Valid @RequestBody FournisseurDTO fournisseurDTO){
        FournisseurDTO dto = fournisseurService.updateFournisseur(id, fournisseurDTO);
        return  ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<FournisseurDTO>>getAll(){
        List<FournisseurDTO> fournisseurs = fournisseurService.getAllFournisseurs();
        if(fournisseurs.isEmpty()){
            throw new NotFoundException("Aucun fournisseur trouvé");
        }
        return  ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getFournisseurs(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FournisseurDTO> fournisseurPage = fournisseurService.getFournisseurs(pageable);

        if (fournisseurPage.isEmpty()) {
            throw new NotFoundException("Aucun fournisseur trouvé");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", fournisseurPage.getContent());
        response.put("currentPage", fournisseurPage.getNumber());
        response.put("totalItems", fournisseurPage.getTotalElements());
        response.put("totalPages", fournisseurPage.getTotalPages());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/searchBySociete/{societe}")
    public ResponseEntity<List<FournisseurDTO>>getFournisseursBysociete(@PathVariable("societe") String societe){
        List<FournisseurDTO>fournisseurDTOList = fournisseurService.searchBySociete(societe);

        if(fournisseurDTOList.isEmpty()) {
            throw new NotFoundException("Aucun fournisseur trouvé pour la société : " + societe);
        }

        return ResponseEntity.ok(fournisseurDTOList);
    }

    @GetMapping("/searchByVille/{ville}")
    public ResponseEntity<List<FournisseurDTO>>getFourinsseursByVille(@PathVariable("ville") String ville){
        List<FournisseurDTO> fournisseurDTOList = fournisseurService.searchByVille(ville);

        if(fournisseurDTOList.isEmpty()){
            throw new NotFoundException("Aucun fournisseur trouvé pour la ville : " + ville);
        }
        return ResponseEntity.ok(fournisseurDTOList);
    }

    @GetMapping("/searchByIce/{ice}")
    public ResponseEntity<FournisseurDTO>getFournisseurByIce(@PathVariable("ice") String ice){
        FournisseurDTO fournisseurDTO = fournisseurService.searchByIce(ice)
                .orElseThrow(()->new NotFoundException("Aucun fournisseur trouvé pour la ICE : " + ice));
        return ResponseEntity.ok(fournisseurDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        fournisseurService.deleteFournisseur(id);
        String message = "Fournisseur supprimé avec succès";
        return ResponseEntity.ok().body(Map.of("message" , message));
    }


}
