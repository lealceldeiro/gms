package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.component.security.IAuthenticationFacade;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.request.security.SecurityConst;
import io.jsonwebtoken.Claims;
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
import java.util.HashSet;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityConst sc;

    private final UserService userService;

    private final IAuthenticationFacade authFacade;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService,
                                  IAuthenticationFacade authenticationFacade, SecurityConst sc) {
        super(authenticationManager);
        this.userService = userService;
        this.authFacade = authenticationFacade;
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
            //parse the token
            Claims claims = Jwts.parser()
                    .setSigningKey(sc.SECRET.getBytes())
                    .parseClaimsJws(token.replace(sc.TOKEN_TYPE, ""))
                    .getBody();
            String user = claims.getSubject();
            final String[] authoritiesS = claims.get(sc.AUTHORITIES_HOLDER).toString().split(sc.AUTHORITIES_SEPARATOR);

            if (user != null && authoritiesS.length > 0) {
                HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
                for (String a : authoritiesS) {
                    authorities.add(new SimpleGrantedAuthority(a));
                }
                String password = (String)claims.get(sc.PASSWORD_HOLDER);

                return new UsernamePasswordAuthenticationToken(user, password, authorities);
            }
            return null;
        }
        return null;
    }

}