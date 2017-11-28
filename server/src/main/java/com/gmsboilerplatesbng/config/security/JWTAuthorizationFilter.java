package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.component.security.IAuthenticationFacade;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.request.security.SecurityConst;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    private final IAuthenticationFacade authFacade;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService,
                                  IAuthenticationFacade authenticationFacade) {
        super(authenticationManager);
        this.userService = userService;
        this.authFacade = authenticationFacade;
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
                Collection<? extends GrantedAuthority> authorities = null;
                // get authorities from context if user is already logged in
                Authentication ctxAuth = this.authFacade.getAuthentication();
                if (((UserDetails)ctxAuth.getPrincipal()).getUsername().equals(user)) {
                    authorities = ctxAuth.getAuthorities();
                }
                // otherwise get authorities from service (user is being logged in)
                if (authorities == null) {
                    authorities = this.userService.getUserAuthorities(user);
                }
                return new UsernamePasswordAuthenticationToken(user, authorities, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
