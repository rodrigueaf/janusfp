package com.gt.gestfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.base.entity.AbstractAuditingEntity;
import com.gt.base.util.BaseConstant;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant une opération
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 03/04/2018
 */
@Entity
@Table
public class Operation extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    private String description;
    private Double montantTotal;
    private OperationType operationType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistement;
    @ManyToOne
    @JoinColumn
    private Operation operationBudget;
    @ManyToOne
    @JoinColumn
    private Budget budget;
    @ManyToOne
    @JoinColumn
    private Exploitation exploitation;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operation")
    private List<OperationDetail> operationDetails;

    public Operation() {
        // Do something
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Operation getOperationBudget() {
        return operationBudget;
    }

    public void setOperationBudget(Operation operationBudget) {
        this.operationBudget = operationBudget;
    }

    public List<OperationDetail> getOperationDetails() {
        return operationDetails;
    }

    public void setOperationDetails(List<OperationDetail> operationDetails) {
        this.operationDetails = operationDetails;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setDateEnregistement(Date dateEnregistement) {
        this.dateEnregistement = dateEnregistement;
    }

    public Date getDateEnregistement() {
        return dateEnregistement;
    }

    public Exploitation getExploitation() {
        return exploitation;
    }

    public void setExploitation(Exploitation exploitation) {
        this.exploitation = exploitation;
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
        return "Operation{" +
                "identifiant=" + identifiant +
                ", description='" + description + '\'' +
                ", montantTotal=" + montantTotal +
                ", operationBudget=" + operationBudget +
                '}';
    }
}
