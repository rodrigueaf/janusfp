package com.gt.gestfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.base.entity.AbstractAuditingEntity;
import com.gt.gestfinance.util.Constants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Entité représentant un utilisateur
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Entity
@Table
public class Utilisateur extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;
    @Pattern(regexp = Constants.LOGIN_REGEX, message = "error.user.login.incorrect")
    @Size(min = 1, max = 50)
    @Column(unique = true, nullable = false)
    private String login;
    @JsonIgnore
    private String motDePasse;
    @Size(max = 50)
    private String prenom;
    @Size(max = 50)
    private String nom;
    @Email
    @Size(min = 5, max = 100)
    private String email;
    @NotNull
    @Column(nullable = false)
    private boolean active = false;
    @Size(max = 20)
    @Column(length = 20)
    @JsonIgnore
    private String cleDActivation;
    @Size(max = 20)
    @Column(length = 20)
    private String cleDeMiseAJourDuMotDePasse;
    @JsonIgnore
    private Instant dateEtHeureDeMiseAJour = null;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @JsonIgnore
    private String chaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @JsonIgnore
    private String numeroDIdentificationFiscale;
    @JsonIgnore
    private String codeDuGestionnaire;
    @ManyToOne
    @JoinColumn( nullable = false)
    private Profil profil;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "utilisateur_permission",
            joinColumns = {
                    @JoinColumn(name = "utilisateur_id", referencedColumnName = "identifiant")},
            inverseJoinColumns = {
                    @JoinColumn(name = "permission_nom", referencedColumnName = "nom")})
    @BatchSize(size = 20)
    private Set<Permission> permissions = new HashSet<>();

    public Utilisateur() {
        // Do something
    }

    public Utilisateur(String login, String motDePasse, String prenom,
                       String nom, String email) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
    }

    public Utilisateur(Long identifiant, String login, String prenom, String nom,
                       String email, boolean active,
                       Set<String> permissions) {
        this(login, null, prenom, nom, email);
        this.identifiant = identifiant;
        this.active = active;
        this.permissions = (permissions != null) ? Permission.recupererLesPermissionsAPartirDeLeurNom(permissions) : null;
    }

    public String getNumeroDIdentificationFiscale() {
        return numeroDIdentificationFiscale;
    }

    public void setNumeroDIdentificationFiscale(String numeroDIdentificationFiscale) {
        this.numeroDIdentificationFiscale = numeroDIdentificationFiscale;
    }

    public Long getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Long identifiant) {
        this.identifiant = identifiant;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.toLowerCase(Locale.ENGLISH);
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCleDActivation() {
        return cleDActivation;
    }

    public void setCleDActivation(String cleDActivation) {
        this.cleDActivation = cleDActivation;
    }

    public String getCleDeMiseAJourDuMotDePasse() {
        return cleDeMiseAJourDuMotDePasse;
    }

    public void setCleDeMiseAJourDuMotDePasse(String cleDeMiseAJourDuMotDePasse) {
        this.cleDeMiseAJourDuMotDePasse = cleDeMiseAJourDuMotDePasse;
    }

    public Instant getDateEtHeureDeMiseAJour() {
        return dateEtHeureDeMiseAJour;
    }

    public void setDateEtHeureDeMiseAJour(Instant dateEtHeureDeMiseAJour) {
        this.dateEtHeureDeMiseAJour = dateEtHeureDeMiseAJour;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public String getChaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire() {
        return chaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire;
    }

    public void setChaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire(String chaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire) {
        this.chaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire = chaineConcatenantCoupleDeNumeroDeCompteEtCodeGestionnaire;
    }

    public String getCodeDuGestionnaire() {
        return codeDuGestionnaire;
    }

    public boolean isGestionnaire() {
        return codeDuGestionnaire != null;
    }

    public void setCodeDuGestionnaire(String codeDuGestionnaire) {
        this.codeDuGestionnaire = codeDuGestionnaire;
    }

    public boolean estUnGestionnaire() {
        return this.codeDuGestionnaire != null;
    }

    public String getNomEtPrenoms() {
        StringJoiner nomCompletJoiner = new StringJoiner(" ");
        if (prenom != null) {
            nomCompletJoiner.add(prenom);
        }
        if (nom != null) {
            nomCompletJoiner.add(nom);
        }
        return nomCompletJoiner.toString();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Utilisateur utilisateur = (Utilisateur) o;

        return login.equals(utilisateur.login);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return login.hashCode();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Utilisateur{"
                + "login='" + login + '\''
                + ", prenom='" + prenom + '\''
                + ", nom='" + nom + '\''
                + ", email='" + email + '\''
                + ", active='" + active + '\''
                + ", cleDActivation='" + cleDActivation + '\''
                + "}";
    }
}
