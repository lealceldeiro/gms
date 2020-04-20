package com.gms.appconfiguration.security.authentication;

import org.springframework.security.core.Authentication;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface AuthenticationFacade {

    /**
     * Returns an {@link Authentication} instance.
     *
     * @return An {@link Authentication} instance.
     * @see Authentication
     */
    Authentication getAuthentication();

    /**
     * Set the {@link Authentication instance.
     *
     * @param authentication Authentication instance to be set.
     * @see Authentication
     */
    void setAuthentication(Authentication authentication);

}
