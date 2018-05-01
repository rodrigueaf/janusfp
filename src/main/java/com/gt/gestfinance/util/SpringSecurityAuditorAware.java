package com.gt.gestfinance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @since 10/08/2017
 * @version 1.0
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private SecurityUtils securityUtils;

    /**
     * @see AuditorAware#getCurrentAuditor()
     * @return
     */
    @Override
    public String getCurrentAuditor() {
        String userName = securityUtils.getCurrentUserLogin();
        return userName != null ? userName : BaseConstant.SYSTEM_ACCOUNT;
    }

    @Autowired
    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }
}
