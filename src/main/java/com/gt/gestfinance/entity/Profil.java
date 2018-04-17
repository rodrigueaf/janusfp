package com.gt.gestfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.base.entity.AbstractAuditingEntity;
import com.gt.base.util.BaseConstant;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité représentant un profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Entity
@Table
public class Profil extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String nom;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "profil_permission",
            joinColumns = {
                    @JoinColumn(name = "profil_id", referencedColumnName = "identifiant")},
            inverseJoinColumns = {
                    @JoinColumn(name = "permission_nom", referencedColumnName = "nom")})
    @BatchSize(size = 20)
    private Set<Permission> permissions = new HashSet<>();

    public Profil() {
        // Do something
    }

    public Profil(Integer identifiant, String name, Set<Permission> permissions) {
        this.identifiant = identifiant;
        this.nom = name;
        this.permissions = permissions;
    }

    public Profil(String name) {
        this.nom = name;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.identifiant);
        return 31 * hash + Objects.hashCode(this.nom);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        return BaseConstant.entityEqualById(
                this.getClass(), this, obj, "getIdentifiant", "getNom");
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Profil{" + "identifiant=" + identifiant + ", nom=" + nom + '}';
    }

}
