package com.gt.gestfinance.controller.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
@Entity
@Table(name = "ACEUSE")
public class User extends AbstractAuditingEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * L'identifiant
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USEID")
    private Long id;

    /**
     * Le login
     */
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USELOG", length = 50, unique = true, nullable = false)
    private String login;

    /**
     * Le mot de passe
     */
    @JsonIgnore
    @Size(min = 60, max = 60)
    @Column(name = "USEPAS", length = 60)
    private String password;

    /**
     * Le prénom
     */
    @Size(max = 50)
    @Column(name = "USEPRE", length = 50)
    private String firstName;

    /**
     * Le nom
     */
    @Size(max = 50)
    @Column(name = "USENOM", length = 50)
    private String lastName;

    /**
     * Le constructeur
     */
    public User() {
        // do something
    }

    /**
     * Le constructeur
     *
     * @param login
     * @param password
     * @param firstName
     * @param lastName
     */
    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Le getter
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Le setter
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Le getter
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     * Lowercase the login before saving it in database
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = (login != null) ? login.toLowerCase(Locale.ENGLISH) : null;
    }

    /**
     * Le getter
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Le setter
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Le getter
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Le setter
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Le getter
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Le setter
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @see Object#hashCode()
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * @see Object#equals(Object)
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.id, other.id);
    }

    /**
     * @see Object#toString()
     * @return
     */
    @Override
    public String toString() {
        return "User{"
                + "login='" + login + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + "}";
    }
}
