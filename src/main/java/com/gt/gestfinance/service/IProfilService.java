package com.gt.gestfinance.service;

import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.exception.CustomException;

import java.util.Set;

/**
 * Interface Service de l'entité Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface IProfilService extends IBaseEntityService<Profil, Integer> {

    Profil modifierLesPermissions(String name, Set<String> authorities) throws CustomException;

    boolean supprimer(Integer id) throws CustomException;
}
