package com.gms.component.security.authentication;

import org.springframework.security.core.Authentication;

/**
 * IAuthenticationFacade
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
public interface IAuthenticationFacade {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);
}
