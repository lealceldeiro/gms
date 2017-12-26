package com.gms.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.component.security.token.JWTService;
import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.SecurityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final ObjectMapper oMapper;

    private final JWTService jwtService;

    private final SecurityConst sc;

    @SuppressWarnings("all")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {

            final Iterator<Map.Entry<String, JsonNode>> fields = oMapper.reader().readTree(req.getInputStream()).fields();
            String login = null;
            String password = null;
            Map.Entry<String, JsonNode> next;

            while (fields.hasNext()) {
                next = fields.next();
                if (next.getKey().equals(sc.getReqUsernameHolder())) {
                    login = next.getValue().asText();
                }
                else if (next.getKey().equals(sc.getReqPasswordHolder())) {
                    password = next.getValue().asText();
                }
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login ,password, new HashSet<>()));

        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult)
            throws IOException {

        Object principal = authResult.getPrincipal();
        String authorities = userService.getUserAuthoritiesForToken(((EUser)principal).getUsername(), SecurityConst.AUTHORITIES_SEPARATOR);
        String sub = ((EUser)principal).getUsername();

        String accessToken = jwtService.createToken(sub, authorities);
        Map claims = jwtService.getClaimsExtended(accessToken);
        Date iat = (Date)claims.get(JWTService.ISSUED_AT);

        String refreshToken = jwtService.createRefreshToken(sub, authorities);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map returnMap = jwtService.createLoginData(sub, accessToken, iat, authorities, refreshToken);

        res.getOutputStream().println(oMapper.writeValueAsString(returnMap));
    }
}
