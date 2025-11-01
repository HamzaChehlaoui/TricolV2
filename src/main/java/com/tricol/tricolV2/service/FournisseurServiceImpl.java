package com.tricol.tricolV2.service;

import com.tricol.tricolV2.dto.FournisseurDTO;
import com.tricol.tricolV2.entity.Fournisseur;
import com.tricol.tricolV2.exception.NotFoundException;
import com.tricol.tricolV2.mapper.FournisseurMapper;
import com.tricol.tricolV2.repository.FournisseurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    public FournisseurServiceImpl (FournisseurRepository fournisseurRepository , FournisseurMapper fournisseurMapper){
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }
    @Override
    public FournisseurDTO addFournisseur(FournisseurDTO dto) {

        Fournisseur fournisseur = fournisseurMapper.toEntity(dto);
        Fournisseur saved = fournisseurRepository.save(fournisseur);

        return fournisseurMapper.toDTO(saved);

    }
    @Override
    public FournisseurDTO updateFournisseur(Long id, FournisseurDTO changes) {
        Fournisseur existing = fournisseurRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé"));

        existing.setSociete(changes.getSociete());
        existing.setAdresse(changes.getAdresse());
        existing.setContact(changes.getContact());
        existing.setEmail(changes.getEmail());
        existing.setTelephone(changes.getTelephone());
        existing.setVille(changes.getVille());
        existing.setIce(changes.getIce());

        Fournisseur updated = fournisseurRepository.save(existing);

        return fournisseurMapper.toDTO(updated);
    }
    @Override
    public void deleteFournisseur(Long id){
       fournisseurRepository.deleteById(id);
    }
    @Override
    public Optional<FournisseurDTO>getFournisseurById(Long id){
        return  fournisseurRepository.findById(id)
                .map(fournisseurMapper::toDTO);
    }
    @Override
    public List<FournisseurDTO>getAllFournisseurs(){
        return fournisseurRepository.findAll().stream()
                .map(fournisseurMapper::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<FournisseurDTO>searchBySociete(String societe){
        return fournisseurRepository.findBySocieteContainingIgnoreCase(societe)
                .stream().map(fournisseurMapper::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<FournisseurDTO>searchByVille(String ville){
        return fournisseurRepository.findByVille(ville).stream()
                .map(fournisseurMapper::toDTO).collect(Collectors.toList());
    }
    @Override
    public Optional<FournisseurDTO>searchByIce(String ice){
        return fournisseurRepository.findByIce(ice)
                .map(fournisseurMapper::toDTO);
    }
    @Override
    public Page<FournisseurDTO> getFournisseurs(Pageable pageable){
        return fournisseurRepository.findAll(pageable)
                .map(fournisseurMapper::toDTO);
    }

}
