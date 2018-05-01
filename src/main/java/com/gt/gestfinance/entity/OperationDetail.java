package com.gt.gestfinance.entity;

import com.gt.gestfinance.util.BaseConstant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entité représentant un détail de l'opération
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 03/04/2018
 */
@Entity
@Table
public class OperationDetail extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    @NotNull
    @Column(nullable = false)
    private OperationSens operationSens;
    @Column(nullable = false)
    private Double montant;
    @ManyToOne
    @JoinColumn
    private Compte compte;
    @ManyToOne
    @JoinColumn
    private Tresorerie tresorerie;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Operation operation;

    public OperationDetail() {
        // Do something
    }

    public Tresorerie getTresorerie() {
        return tresorerie;
    }

    public void setTresorerie(Tresorerie tresorerie) {
        this.tresorerie = tresorerie;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public OperationSens getOperationSens() {
        return operationSens;
    }

    public void setOperationSens(OperationSens operationSens) {
        this.operationSens = operationSens;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
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
        return "OperationDetail{" +
                "identifiant=" + identifiant +
                ", operationSens=" + operationSens +
                ", montant=" + montant +
                ", compte=" + compte +
                ", operation=" + operation +
                '}';
    }
}
