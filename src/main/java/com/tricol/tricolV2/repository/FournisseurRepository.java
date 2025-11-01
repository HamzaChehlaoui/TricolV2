package com.tricol.tricolV2.repository;

import com.tricol.tricolV2.entity.Fournisseur;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FournisseurRepository extends JpaRepository<Fournisseur ,Long> {

    List<Fournisseur> findBySocieteContainingIgnoreCase(String societe);
    List<Fournisseur> findByVille(String ville);
    Optional<Fournisseur> findByIce(String ice);

}
