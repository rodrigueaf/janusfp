package com.gt.gestfinance.entity;

import com.gt.gestfinance.util.BaseConstant;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entité représentant une association de compte
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Entity
@Table
public class Tresorerie extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    private Double soldeCourant = 0D;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Compte comptePhysique;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Compte compteLogique;

    public Tresorerie() {
        // Do something
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public Double getSoldeCourant() {
        return soldeCourant;
    }

    public void setSoldeCourant(Double soldeCourant) {
        this.soldeCourant = soldeCourant;
    }

    public Compte getComptePhysique() {
        return comptePhysique;
    }

    public void setComptePhysique(Compte comptePhysique) {
        this.comptePhysique = comptePhysique;
    }

    public Compte getCompteLogique() {
        return compteLogique;
    }

    public void setCompteLogique(Compte compteLogique) {
        this.compteLogique = compteLogique;
    }

    public void augmenterSolde(Double aAjouter) {
        this.soldeCourant += aAjouter;
    }

    public void diminuerSolde(Double aSoustraire) {
        this.soldeCourant -= aSoustraire;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return 31 * hash + Objects.hashCode(this.identifiant);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        return BaseConstant.entityEqualById(
                this.getClass(), this, obj, "getIdentifiant");
    }

    @Override
    public String toString() {
        return "Compte{" +
                "identifiant=" + identifiant +
                ", soldeCourant=" + soldeCourant +
                '}';
    }
}
