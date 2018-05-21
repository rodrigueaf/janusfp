package com.gt.gestfinance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by RODRIGUE on 15/09/2016.
 */
@Entity
public class Version extends AbstractAuditingEntity {

    public static final String MOTIF_AJOUT = "A";
    public static final String MOTIF_MODIFICATION = "M";
    public static final String MOTIF_SUPPRESSION = "S";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String attribut;
    private Integer valeur;
    private String motif;

    public Version() {
    }

    public Version(Integer id, String attribut, Integer valeur, String motif) {
        this.id = id;
        this.attribut = attribut;
        this.valeur = valeur;
        this.motif = motif;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribut() {
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }

    public Integer getValeur() {
        return valeur;
    }

    public void setValeur(Integer valeur) {
        this.valeur = valeur;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version domaine = (Version) o;

        return !(id != null ? !id.equals(domaine.id) : domaine.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
