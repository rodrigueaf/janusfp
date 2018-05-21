package com.gt.gestfinance.repository;


import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.entity.CompteType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Le repository de l'entité Compte
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface CompteRepository extends BaseEntityRepository<Compte, Integer> {

    Optional<Compte> findByLibelle(String libelle);

    @Query("select c from Compte c left join fetch c.sousComptes s where c.identifiant = ?1")
    Optional<Compte> findWithSousComptesByIdentifiant(Integer identifiant);

    List<Compte> findByCompteTypeNot(CompteType compteType);

    @Query("select c from Compte c where c.id in :idSet")
    List<Compte> recupererLaListeVersionnee(@Param("idSet") Integer[] idSet);
}
