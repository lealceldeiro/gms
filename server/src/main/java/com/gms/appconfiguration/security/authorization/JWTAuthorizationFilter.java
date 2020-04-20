package com.gms.appconfiguration.security.authorization;

import com.gms.appconfiguration.security.authentication.AuthenticationFacade;
import com.gms.util.constant.SecurityConstant;
import com.gms.service.security.token.JWTService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    /**
     * Instance of {@link SecurityConstant}.
     */
    private final SecurityConstant securityConstant;
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
     * @param securityConstant      An instance of {@link SecurityConstant}.
     * @param jwtServiceArg         An instance of {@link JWTService}.
     * @param authenticationFacade  An instance of {@link AuthenticationFacade}.
     */
    public JWTAuthorizationFilter(final AuthenticationManager authenticationManager,
                                  final SecurityConstant securityConstant, final JWTService jwtServiceArg,
                                  final AuthenticationFacade authenticationFacade) {
        super(authenticationManager);
        this.securityConstant = securityConstant;
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
        String token = req.getHeader(securityConstant.getATokenHeader());

        if (token != null && token.startsWith(securityConstant.getATokenType())) {
            try {
                // parse the token
                final String claimsJTW = token.replace(securityConstant.getATokenType(), "");
                final Map<String, Object> claims = jwtService.getClaimsExtended(claimsJTW,
                        securityConstant.getAuthoritiesHolder(), PASS_HOLDER);

                final String user = claims.get(jwtService.subjectKey()).toString();
                if (user != null) {
                    // split into an array the string holding the authorities by the defined separator
                    final String[] authorityValues = authoritiesFrom(claims);
                    if (authorityValues.length > 0) {
                        final Set<GrantedAuthority> authorities = grantedAuthoritiesFrom(authorityValues);
                        final String hashedPassword = String.valueOf(claims.get(PASS_HOLDER));

                        return new UsernamePasswordAuthenticationToken(user, hashedPassword, authorities);
                    }
                }
            } catch (JwtException e) {  // any problem with token, do not authenticate
                return null;
            }
        }

        return null;
    }

    private static Set<GrantedAuthority> grantedAuthoritiesFrom(final String[] authorities) {
        return Arrays.stream(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    private String[] authoritiesFrom(final Map<?, ?> jwtClaims) {
        return jwtClaims.get(securityConstant.getAuthoritiesHolder())
                        .toString()
                        .split(SecurityConstant.AUTHORITIES_SEPARATOR);
    }
}
