package com.gt.gestfinance.dto;

import com.gt.gestfinance.entity.Permission;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.util.MPConstants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Modèle de représentation pour l'entité Profil.
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public class ManagedProfilVM {

    public Integer identifiant;
    @NotBlank(message = MPConstants.LE_NOM_DU_PROFIL_OBLIGATOIRE)
    @Size(max = 50)
    public String nom;
    public Set<Permission> permissions;
    private Profil profil;
    public boolean keepOldAuthorities;

    public Profil getProfil() {
        if (this.profil == null) {
            this.profil = new Profil(identifiant, nom, permissions);
        }
        return this.profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }
}
