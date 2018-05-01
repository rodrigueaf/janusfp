package com.gt.gestfinance.service;

import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.util.GrandLivreList;
import com.gt.gestfinance.util.RealisationParPrevisionList;

import java.util.Date;

/**
 * Interface Service de l'entité Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface ICompteService extends IBaseEntityService<Compte, Integer> {

    boolean supprimer(Integer compteId) throws CustomException;

    RealisationParPrevisionList realisationParPrevision(Integer budgetId);

    GrandLivreList recupererLeGrandLivre(Integer compteId, Date dateDebut, Date dateFin);
}
