package com.gt.gestfinance.repository;

import com.gt.base.repository.BaseEntityRepository;
import com.gt.gestfinance.entity.Profil;

import java.util.Optional;

/**
 * Le repository de l'entité Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface ProfilRepository extends BaseEntityRepository<Profil, Integer> {

    /**
     * Retourner un profil à partir de son nom
     *
     * @param nom : Le nom
     * @return Optional
     */
    Optional<Profil> findOneByNom(String nom);

    /**
     * Retourner un profil avec ses permission à partir de son nom
     *
     * @param nom : Le nom
     * @return Optional
     */
    Optional<Profil> findOneWithPermissionsByNom(String nom);
}
