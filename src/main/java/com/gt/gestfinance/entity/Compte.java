package com.gt.gestfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.util.BaseConstant;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant un profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Entity
public class Compte extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    @Size(min = 1, max = 300)
    @Column(length = 300, unique = true, nullable = false)
    private String libelle;
    @Column(nullable = false)
    private CompteType compteType = CompteType.AUTRE_COMPTE;
    private CompteForme compteForme;
    @Column(nullable = false)
    private Boolean utilisable = Boolean.FALSE;
    private Double soldeCourant = 0D;
    @ManyToOne
    @JoinColumn
    private Compte compteParent;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compteParent")
    private List<Compte> sousComptes;

    public Compte() {
        // Do something
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public String getLibelle() {
        return libelle;
    }

    public Boolean getUtilisable() {
        return utilisable;
    }

    public Double getSoldeCourant() {
        return soldeCourant;
    }

    public Compte getCompteParent() {
        return compteParent;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setCompteParent(Compte compteParent) {
        this.compteParent = compteParent;
    }

    public void setUtilisable(Boolean utilisable) {
        this.utilisable = utilisable;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public void setSoldeCourant(Double soldeCourant) {
        this.soldeCourant = soldeCourant;
    }

    public void setCompteType(CompteType compteType) {
        this.compteType = compteType;
    }

    public CompteType getCompteType() {
        return compteType;
    }

    public List<Compte> getSousComptes() {
        return sousComptes;
    }

    public void setSousComptes(List<Compte> sousComptes) {
        this.sousComptes = sousComptes;
    }

    public CompteForme getCompteForme() {
        return compteForme;
    }

    public void setCompteForme(CompteForme compteForme) {
        this.compteForme = compteForme;
    }

    public void augmenterSolde(Double aAjouter) {
        this.soldeCourant += aAjouter;
    }

    public void diminuerSolde(Double aSoustraire) {
        this.soldeCourant -= aSoustraire;
    }

    public void calculerNouveauSolde(OperationSens operationSens, Double montantOperation) {
        if (CompteType.SORTIE == this.getCompteType()) {
            if (OperationSens.DEBIT == operationSens)
                this.soldeCourant += montantOperation;
        } else if (CompteType.ENTREE == this.getCompteType()) {
            if (OperationSens.CREDIT == operationSens)
                this.soldeCourant += montantOperation;
        }
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
                ", libelle='" + libelle + '\'' +
                ", compteType=" + compteType +
                ", utilisable=" + utilisable +
                ", soldeCourant=" + soldeCourant +
                '}';
    }
}
