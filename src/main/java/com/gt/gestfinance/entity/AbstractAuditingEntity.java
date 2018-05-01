package com.gt.gestfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.util.State;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Classe de base des entités
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 10/08/2017
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * L'utilisateur qui a créé l'enregistrement
     */
    @CreatedBy
    @Column(name = "CBY", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    /**
     * Le date et heure de création de l'enregistrement
     */
    @CreatedDate
    @Column(name = "CDT", nullable = false)
    @JsonIgnore
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    /**
     * L'utilisateur qui a dernierement modifié la donnée
     */
    @LastModifiedBy
    @Column(name = "LMB", length = 50)
    @JsonIgnore
    private String lastModifiedBy;

    /**
     * Le date et heure de la derniere modification
     */
    @LastModifiedDate
    @Column(name = "LMD")
    @JsonIgnore
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModifiedDate = new Date();

    /**
     * L'état correspondant à l'activation, la désactivation ou la suppression
     * d'une entité
     */
    @NotNull
    @Column(name = "ETAT", nullable = false)
    protected State state = State.ENABLED;

    /**
     * L'état correspondant à l'utilisation d'un objet
     */
    @Column(name = "USED")
    protected boolean used = false;

    /**
     * le getter
     *
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Le setter
     *
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * le getter
     *
     * @return
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Le setter
     *
     * @param createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * le getter
     *
     * @return
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Le setter
     *
     * @param lastModifiedBy
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * le getter
     *
     * @return
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Le setter
     *
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * le getter
     *
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * Le setter
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * le getter
     *
     * @return
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Le setter
     *
     * @param used
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
}
