package com.gt.gestfinance.service;

import com.gt.base.service.IBaseEntityService;
import com.gt.gestfinance.entity.Tresorerie;
import com.gt.gestfinance.util.GrandLivreTresorerieList;

import java.util.Date;

/**
 * Interface Service de l'entité Tresorerie
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface ITresorerieService extends IBaseEntityService<Tresorerie, Integer> {

    Double calculerLeSolde(Tresorerie tresorerie);

    GrandLivreTresorerieList recupererLeGrandLivre(Integer tresorerieId, Date dateDebut, Date dateFin);
}
