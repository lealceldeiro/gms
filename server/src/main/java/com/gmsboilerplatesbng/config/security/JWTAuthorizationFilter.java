package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.util.request.security.SecurityConstant;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(SecurityConstant.HEADER);
        if (header == null || !header.startsWith(SecurityConstant.TOKEN_TYPE)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest req) {
        String token = req.getHeader(SecurityConstant.HEADER);

        if (token != null) {
            //parse then token
            String user = Jwts.parser()
                    .setSigningKey(SecurityConstant.SECRET.getBytes())
                    .parseClaimsJws(token.replace(SecurityConstant.TOKEN_TYPE, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                //todo
                /*
                ArrayList authorities = Jwts.parser()
                        .setSigningKey(SecurityConstant.SECRET.getBytes())
                        .parseClaimsJws(token.replace(SecurityConstant.TOKEN_TYPE, ""))
                        .getBody()
                        .get(SecurityConstant.AUTHORITIES_HOLDER, ArrayList.class);*/
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
