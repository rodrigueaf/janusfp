package com.gt.gestfinance.repository;


import com.gt.gestfinance.entity.Tresorerie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Le repository de l'entité Tresorerie
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface TresorerieRepository extends BaseEntityRepository<Tresorerie, Integer> {

    @Query("select c from Tresorerie c where c.id in :idSet")
    List<Tresorerie> recupererLaListeVersionnee(@Param("idSet") Integer[] idSet);

    List<Tresorerie> findByCompteLogiqueIdentifiantOrComptePhysiqueIdentifiant(Integer comptePhysiqueId, Integer compteLogiqueId);
}
