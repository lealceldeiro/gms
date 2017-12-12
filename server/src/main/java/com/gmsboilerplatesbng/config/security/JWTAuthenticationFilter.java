package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.SecurityConst;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * JWTAuthenticationFilter
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final SecurityConst sc;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final ObjectMapper oMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {

            EUser credentials = new ObjectMapper().readValue(req.getInputStream(), EUser.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(), credentials.getPassword(), new HashSet<>()
            ));

        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        Object principal = authResult.getPrincipal();
        String authorities = userService.getUserAuthoritiesForToken(((EUser)principal).getUsername(), sc.AUTHORITIES_SEPARATOR);

        long currentMillis = System.currentTimeMillis();
        long expiration = currentMillis + (sc.EXPIRATION_TIME * 1000);
        String sub = ((EUser)principal).getUsername();
        String jwt = Jwts.builder()
                .setSubject(sub)
                .setExpiration(new Date(expiration))
                .setIssuedAt(new Date(currentMillis))
                .signWith(SignatureAlgorithm.HS512, sc.SECRET.getBytes())
                .claim(sc.AUTHORITIES_HOLDER, authorities)
                .claim(sc.PASSWORD_HOLDER, ((EUser) principal).getPassword())
                .compact();

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put(sc.USERNAME_HOLDER, sub);
        returnMap.put(sc.TOKEN_HOLDER, jwt);
        returnMap.put(sc.AUTHORITIES_HOLDER, authorities.split(sc.AUTHORITIES_SEPARATOR));
        returnMap.put(sc.TOKEN_TYPE_HOLDER, sc.TOKEN_TYPE);
        returnMap.put(sc.HEADER_TO_BE_SENT_HOLDER, sc.HEADER);
        returnMap.put(sc.EXPIRATION_HOLDER, expiration);
        returnMap.put(sc.ISSUED_TIME_HOLDER, currentMillis);

        res.getOutputStream().println(oMapper.writeValueAsString(returnMap));
    }
}
