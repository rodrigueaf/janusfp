package com.gt.gestfinance.service.impl;

import com.gt.base.exception.CustomException;
import com.gt.base.service.impl.BaseEntityService;
import com.gt.gestfinance.entity.Permission;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.repository.PermissionRepository;
import com.gt.gestfinance.repository.ProfilRepository;
import com.gt.gestfinance.repository.UtilisateurRepository;
import com.gt.gestfinance.service.IProfilService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe Service de l'entité ModePaiement
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 25/09/2017
 */
@Service
public class ProfilService extends BaseEntityService<Profil, Integer> implements IProfilService {

    private PermissionRepository permissionRepository;
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public ProfilService(ProfilRepository repository) {
        super(repository);
    }

    /**
     * @see IProfilService#modifierLesPermissions(String, Set)
     */
    @Override
    public Profil modifierLesPermissions(String nom, Set<String> permissions) throws CustomException {
        Optional<Profil> o = ((ProfilRepository) repository).findOneByNom(nom);

        if (!o.isPresent()) {
            throw new CustomException("error.profil.not.found");
        }

        Profil profilRecuperer = o.get();

        profilRecuperer.setPermissions(permissions.stream()
                .map(a -> permissionRepository.findOne(a)).collect(Collectors.toSet()));

        Profil profil = repository.saveAndFlush(profilRecuperer);

        Set<Permission> userPermissionSet = new HashSet<>();
        userPermissionSet.addAll(profilRecuperer.getPermissions());

        utilisateurRepository.findAllByProfilNom(nom)
                .forEach(u -> u.setPermissions(userPermissionSet));

        return profil;
    }

    @Override
    public boolean supprimer(Integer id) throws CustomException {
        if (!utilisateurRepository.findAllByProfilNom(repository
                .findOne(id).getNom()).isEmpty())
            throw new CustomException(MPConstants.PROFIL_DEJA_ATTRIBUER);
        return super.delete(id);
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

}
