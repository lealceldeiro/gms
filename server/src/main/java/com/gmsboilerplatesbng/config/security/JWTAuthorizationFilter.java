package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.util.request.security.SecurityConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
import java.util.Date;
import java.util.HashSet;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityConst sc;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityConst sc) {
        super(authenticationManager);
        this.sc = sc;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(sc.HEADER);
        if (header == null || !header.startsWith(sc.TOKEN_TYPE)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest req) {
        String token = req.getHeader(sc.HEADER);

        if (token != null) {
            try {
                //parse the token
                Claims claims = Jwts.parser()
                        .setSigningKey(sc.SECRET.getBytes())
                        .parseClaimsJws(token.replace(sc.TOKEN_TYPE, ""))
                        .getBody();
                String user = claims.getSubject();
                final String[] authoritiesS = claims.get(sc.AUTHORITIES_HOLDER).toString().split(sc.AUTHORITIES_SEPARATOR);
                Date now = new Date(System.currentTimeMillis());

                if (user != null && authoritiesS.length > 0 && claims.getExpiration().before(now)) {
                    HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
                    for (String a : authoritiesS) {
                        authorities.add(new SimpleGrantedAuthority(a));
                    }
                    String password = (String) claims.get(sc.PASSWORD_HOLDER);

                    return new UsernamePasswordAuthenticationToken(user, password, authorities);
                }
                return null;
            }
            catch (JwtException e) { //any problem with token, do not authenticate
                return null;
            }
        }
        return null;
    }

}