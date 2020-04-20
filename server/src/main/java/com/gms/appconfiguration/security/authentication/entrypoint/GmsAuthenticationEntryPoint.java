package com.gms.appconfiguration.security.authentication.entrypoint;

import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Extends {@link AuthenticationEntryPoint} in order to provide custom means to customize the response on authentication
 * failure requests.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
interface GmsAuthenticationEntryPoint extends AuthenticationEntryPoint {
}
