package com.gt.gestfinance.dto;

import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.util.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Modèle de représentation pour l'entité Utilisateur.
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public class ManagedUserVM {

    public static final int LONGUEUR_MIN_DU_MOT_DE_PASSE = 4;
    public static final int LONGUEUR_MAX_DU_MOT_DE_PASSE = 100;
    public Long identifiant;
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    public String login;
    @Size(max = 50)
    public String prenom;
    @Size(max = 50)
    public String nom;
    @Email
    @Size(min = 5, max = 100)
    public String email;
    @Size(min = LONGUEUR_MIN_DU_MOT_DE_PASSE, max = LONGUEUR_MAX_DU_MOT_DE_PASSE)
    public String password;
    public boolean activated = false;
    public Set<String> authorities;
    public Utilisateur utilisateur;
    public Profil profil;

    public Utilisateur getUtilisateur() {
        if (this.utilisateur == null) {
            this.utilisateur = new Utilisateur(identifiant, login, prenom, nom,
                    email, activated, authorities);
            this.utilisateur.setProfil(this.profil);
        }
        return this.utilisateur;
    }
}
