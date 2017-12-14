package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.component.security.token.JWTService;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.SecurityConst;
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
import java.util.Map;

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

    private final JWTService jwtService;

    @SuppressWarnings("all")
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
        String authorities = userService.getUserAuthoritiesForToken(((EUser)principal).getUsername(), SecurityConst.AUTHORITIES_SEPARATOR);
        String sub = ((EUser)principal).getUsername();

        String accessToken = jwtService.createToken(sub, authorities);
        Map claims = jwtService.getClaimsExtended(accessToken);
        Date iat = (Date)claims.get(JWTService.ISSUED_AT);

        String refreshToken = jwtService.createRefreshToken(sub, authorities);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put(sc.getUsernameHolder(), sub);
        returnMap.put(sc.getATokenHolder(), accessToken);
        returnMap.put(sc.getATokenTypeHolder(), sc.getATokenType());
        returnMap.put(sc.getATokenHeaderToBeSentHolder(), sc.getATokenHeader());
        returnMap.put(sc.getExpirationHolder(), iat.getTime() + jwtService.getATokenExpirationTime());
        returnMap.put(sc.getExpiresInHolder(), jwtService.getATokenExpirationTime());
        returnMap.put(sc.getIssuedTimeHolder(), iat);
        returnMap.put(sc.getAuthoritiesHolder(), authorities.split(SecurityConst.AUTHORITIES_SEPARATOR));
        returnMap.put(sc.getRTokenHolder(), refreshToken);

        res.getOutputStream().println(oMapper.writeValueAsString(returnMap));
    }
}
