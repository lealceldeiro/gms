package com.gms.config.security;

import com.gms.component.security.authentication.AuthenticationFacade;
import com.gms.component.security.token.JWTService;
import com.gms.util.constant.SecurityConst;
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

    private final SecurityConst sc;

    private final JWTService jwtService;

    private final AuthenticationFacade authFacade;

    /**
     * Password holder for storing the password (hashed) in the token's claims
     */
    private static final String PASS_HOLDER = "password";

    @SuppressWarnings("WeakerAccess")
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityConst sc, JWTService jwtService,
                                  AuthenticationFacade authenticationFacade) {
        super(authenticationManager);
        this.sc = sc;
        this.jwtService = jwtService;
        this.authFacade = authenticationFacade;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        if (authToken == null) {
            chain.doFilter(req, res);
            return;
        }

        authFacade.setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest req) {
        String token = req.getHeader(sc.getATokenHeader());

        if (token != null && token.startsWith(sc.getATokenType())) {
            try {
                //parse the token
                Map claims = jwtService.getClaimsExtended(
                        token.replace(sc.getATokenType(), ""), sc.getAuthoritiesHolder(), PASS_HOLDER
                );
                String user = claims.get(JWTService.SUBJECT).toString();
                if (user != null) {
                    // split into an array the string holding the authorities by the defined separator
                    final String[] authoritiesS = claims.get(sc.getAuthoritiesHolder()).toString().split(SecurityConst.AUTHORITIES_SEPARATOR);
                    if (authoritiesS.length > 0) {
                        HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
                        for (String a : authoritiesS) {
                            authorities.add(new SimpleGrantedAuthority(a));
                        }
                        String password = (String) claims.get(PASS_HOLDER);  // hashed password

                        return new UsernamePasswordAuthenticationToken(user, password, authorities);
                    }
                }
            }
            catch (JwtException e) { //any problem with token, do not authenticate
                return null;
            }
        }
        return null;
    }

}