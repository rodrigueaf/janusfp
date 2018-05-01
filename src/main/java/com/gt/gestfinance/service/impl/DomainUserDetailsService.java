package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.exception.UserNotActivatedException;
import com.gt.gestfinance.repository.UtilisateurRepository;
import com.gt.gestfinance.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authentifier un utilisateur à partir de la base de données
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);
    private final UtilisateurRepository utilisateurRepository;

    public DomainUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<Utilisateur> userFromDatabase = utilisateurRepository.findOneWithPermissionsByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.getActive() || !State.ENABLED.equals(user.getState())) {
                throw new UserNotActivatedException("Utilisateur " + lowercaseLogin + " was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = user.getPermissions().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getNom()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getMotDePasse(),
                    grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("Utilisateur " + lowercaseLogin + " was not found in the "
                + "database"));
    }
}
