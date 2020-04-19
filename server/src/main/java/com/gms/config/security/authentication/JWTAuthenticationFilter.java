package com.gms.config.security.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.security.token.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
public final class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * Instance of {@link AuthenticationManager}.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Instance of {@link UserService}.
     */
    private final UserService userService;
    /**
     * Instance of {@link ObjectMapper}.
     */
    private final ObjectMapper oMapper;
    /**
     * Instance of {@link JWTService}.
     */
    private final JWTService jwtService;
    /**
     * Instance of {@link SecurityConst}.
     */
    private final SecurityConst sc;
    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * Instance of {@link MessageResolver}.
     */
    private final MessageResolver msg;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param req Incoming {@link HttpServletRequest}.
     * @param res {@link HttpServletResponse}
     * @return The {@link Authentication} instance after successful authentication.
     */
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) {
        try {
            final Iterator<Map.Entry<String, JsonNode>> fields =
                    oMapper.reader().readTree(req.getInputStream()).fields();

            String login = null;
            String password = null;
            Map.Entry<String, JsonNode> next;

            while (fields.hasNext()) {
                next = fields.next();
                if (next.getKey().equals(sc.getReqUsernameHolder())) {
                    login = next.getValue().asText();
                } else if (next.getKey().equals(sc.getReqPasswordHolder())) {
                    password = next.getValue().asText();
                }
            }

            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login, password, new HashSet<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param req        Incoming {@link HttpServletRequest}.
     * @param res        {@link HttpServletResponse}
     * @param chain      {@link FilterChain}
     * @param authResult {@link Authentication}
     * @throws IOException when there is an error writing to the output stream writing the response.
     */
    @Override
    protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res,
                                            final FilterChain chain, final Authentication authResult)
            throws IOException {

        Object principal = authResult.getPrincipal();
        String authorities = userService
                .getUserAuthoritiesForToken(((EUser) principal).getUsername(), SecurityConst.AUTHORITIES_SEPARATOR);

        Map<String, Object> returnMap;
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (!authorities.isEmpty()) { // user login was correct
            String sub = ((EUser) principal).getUsername();

            String accessToken = jwtService.createToken(sub, authorities);
            Map<String, Object> claims = jwtService.getClaimsExtended(accessToken);
            Date iat = (Date) claims.get(jwtService.issuedAtKey());

            String refreshToken = jwtService.createRefreshToken(sub, authorities);

            returnMap = jwtService.createLoginData(sub, accessToken, iat, authorities, refreshToken);
        } else {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            returnMap = ExceptionUtil.getResponseBodyForGmsSecurityException(new GmsSecurityException(sc.getSignUpUrl(),
                    "security.unauthorized"), msg, dc);
        }
        try (ServletOutputStream servletOutputStream = res.getOutputStream()) {
            servletOutputStream.println(oMapper.writeValueAsString(returnMap));
        }
    }

}
