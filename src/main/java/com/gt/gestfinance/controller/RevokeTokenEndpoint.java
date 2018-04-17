package com.gt.gestfinance.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Contrôleur de suppression d'un token
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@FrameworkEndpoint
@Api(tags = {"oauth"})
public class RevokeTokenEndpoint {

    private ConsumerTokenServices tokenServices;

    /**
     * DELETE /oauth/token : Supprimer un token
     *
     * @param request
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    @ApiOperation(value = "Supprimer un token")
    public void supprimerLeToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            tokenServices.revokeToken(token);
        }
    }

    @Resource(name = "tokenServices")
    public void setTokenServices(ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }
}
