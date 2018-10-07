package com.gms.component.security.authentication;

import org.springframework.security.core.Authentication;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface AuthenticationFacade {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

}
