package com.gms.config.security;

import com.gms.component.security.authentication.AuthenticationFacade;
import com.gms.util.constant.SecurityConst;
import com.gms.util.security.token.JWTService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    /**
     * Instance of {@link SecurityConst}.
     */
    private final SecurityConst sc;
    /**
     * Instance of {@link JWTService}.
     */
    private final JWTService jwtService;
    /**
     * Instance of {@link AuthenticationFacade}.
     */
    private final AuthenticationFacade authFacade;

    /**
     * Password holder for storing the password (hashed) in the token's claims.
     */
    private static final String PASS_HOLDER = "password";

    /**
     * Creates a new {@link JWTAuthorizationFilter}.
     *
     * @param authenticationManager An instance of {@link AuthenticationManager}.
     * @param securityConst         An instance of {@link SecurityConst}.
     * @param jwtServiceArg         An instance of {@link JWTService}.
     * @param authenticationFacade  An instance of {@link AuthenticationFacade}.
     */
    public JWTAuthorizationFilter(final AuthenticationManager authenticationManager, final SecurityConst securityConst,
                                  final JWTService jwtServiceArg, final AuthenticationFacade authenticationFacade) {
        super(authenticationManager);
        this.sc = securityConst;
        this.jwtService = jwtServiceArg;
        this.authFacade = authenticationFacade;
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param req   Incoming {@link HttpServletRequest}.
     * @param res   {@link HttpServletResponse}
     * @param chain {@link FilterChain}
     * @throws IOException      may be thrown by the {@code chain} instance.
     * @throws ServletException may be thrown by the {@code chain} instance.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res,
                                    final FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        if (authToken == null) {
            chain.doFilter(req, res);

            return;
        }

        authFacade.setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(final HttpServletRequest req) {
        String token = req.getHeader(sc.getATokenHeader());

        if (token != null && token.startsWith(sc.getATokenType())) {
            try {
                // parse the token
                Map<String, Object> claims = jwtService.getClaimsExtended(
                        token.replace(sc.getATokenType(), ""), sc.getAuthoritiesHolder(), PASS_HOLDER
                );
                String user = claims.get(jwtService.subjectKey()).toString();
                if (user != null) {
                    // split into an array the string holding the authorities by the defined separator
                    final String[] authoritiesS = claims
                            .get(sc.getAuthoritiesHolder()).toString()
                            .split(SecurityConst.AUTHORITIES_SEPARATOR);
                    if (authoritiesS.length > 0) {
                        HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
                        for (String a : authoritiesS) {
                            authorities.add(new SimpleGrantedAuthority(a));
                        }
                        String password = String.valueOf(claims.get(PASS_HOLDER));  // hashed password

                        return new UsernamePasswordAuthenticationToken(user, password, authorities);
                    }
                }
            } catch (JwtException e) {  // any problem with token, do not authenticate
                return null;
            }
        }

        return null;
    }

}
