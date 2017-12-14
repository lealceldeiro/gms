package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.component.security.token.JWTService;
import com.gmsboilerplatesbng.util.constant.SecurityConst;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * JWTAuthorizationFilter
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityConst sc;

    private final JWTService jwtService;

    @SuppressWarnings("WeakerAccess")
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityConst sc, JWTService jwtService) {
        super(authenticationManager);
        this.sc = sc;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(sc.getATokenHeader());
        if (header == null || !header.startsWith(sc.getATokenType())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest req) {
        String token = req.getHeader(sc.getATokenHeader());

        if (token != null) {
            try {
                //parse the token
                Map claims = jwtService.getClaimsExtended(
                        token.replace(sc.getATokenType(), ""), sc.getAuthoritiesHolder(), SecurityConst.PASS_HOLDER
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
                        String password = (String) claims.get(SecurityConst.PASS_HOLDER);  // encoded password

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