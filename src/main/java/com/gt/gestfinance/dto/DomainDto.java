package com.gt.gestfinance.dto;

import com.gt.gestfinance.entity.Permission;

import java.util.Set;

/**
 * Entité représentant un domaine
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public class DomainDto {

    public final String name;
    public final Set<Permission> permissions;

    public DomainDto(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
