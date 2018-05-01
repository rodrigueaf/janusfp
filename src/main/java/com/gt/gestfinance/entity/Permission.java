package com.gt.gestfinance.entity;

import com.gt.gestfinance.util.BaseConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entité représentant une permission
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Entity
@Table
public class Permission extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Size(min = 1, max = 50)
    @Id
    @Column(length = 50)
    private String nom;
    @Size(min = 1, max = 100)
    @Column(length = 100)
    private String libelleParDefaut;

    public Permission() {
        // Do something later
    }

    public Permission(String nom) {
        this.nom = nom;
    }

    public Permission(String nom, String libelleParDefaut) {
        this(nom);
        this.libelleParDefaut = libelleParDefaut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLibelleParDefaut() {
        return libelleParDefaut;
    }

    public void setLibelleParDefaut(String libelleParDefaut) {
        this.libelleParDefaut = libelleParDefaut;
    }

    static Set<Permission> recupererLesPermissionsAPartirDeLeurNom(Set<String> strings) {
        return strings.stream().map(string -> {
            Permission auth = new Permission();
            auth.setNom(string);
            return auth;
        }).collect(Collectors.toSet());
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 3;
        return 71 * hash + Objects.hashCode(this.nom);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        return BaseConstant.entityEqualById(
                this.getClass(), this, obj, "getNom");
    }
}
