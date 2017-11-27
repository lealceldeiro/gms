package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.request.security.SecurityConst;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(SecurityConst.HEADER);
        if (header == null || !header.startsWith(SecurityConst.TOKEN_TYPE)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = getAuthToken(req);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(HttpServletRequest req) {
        String token = req.getHeader(SecurityConst.HEADER);

        if (token != null) {
            //parse the token
            String user = Jwts.parser()
                    .setSigningKey(SecurityConst.SECRET.getBytes())
                    .parseClaimsJws(token.replace(SecurityConst.TOKEN_TYPE, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                HashSet<GrantedAuthority> authorities = this.userService.getUserAuthorities(user);
                return new UsernamePasswordAuthenticationToken(user, authorities, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
