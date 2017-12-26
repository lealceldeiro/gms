package com.gms.controller.security;

import com.gms.component.security.authentication.IAuthenticationFacade;
import com.gms.component.security.token.JWTService;
import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SecurityController
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RequiredArgsConstructor
@RestController
@BasePathAwareController
public class SecurityController extends ResponseEntityExceptionHandler {

    private final MessageResolver msg;
    private final DefaultConst dc;
    private final UserService userService;
    private final SecurityConst sc;
    private final JWTService jwtService;
    private final IAuthenticationFacade authenticationFacade;

    /**
     * Registers a new {@link EUser} setting it as active with properties such as emailVerified and enabled to `true`.
     * This method overrides the default "user" url in the {@link com.gms.repository.security.user.EUserRepository}
     * by setting it path to 'user' (like in the `EUserRepository` interface, by doing `produces = 'application/hal+json'`
     * and by putting in within a controller annotated as `@BasePathAwareController`.
     * @param user {@link EUser} data to be created.
     * @param pra Injected automatically by Spring.
     * @return A {@link EUser} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = "user", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PersistentEntityResource register(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        return signUpUser(user, true, pra);
    }

    /**
     * Registers a new {@link EUser} setting it as active with some properties such as enabled to `true`.
     * @param user {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping("${gms.security.sign_up_url}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PersistentEntityResource signUp(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        return signUpUser(user, false, pra);
    }

    /**
     * Provides a new access token given a refresh token.
     * @return Map with the same information provided in the
     * {@link com.gms.config.security.JWTAuthenticationFilter#successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)}
     * method.
     */
    @PostMapping("access_token")
    @ResponseBody
    public Map refreshToken(@RequestBody RefreshTokenPayload payload) {
        String oldRefreshToken = payload.getRefreshToken();
        if (oldRefreshToken != null) {
            try {
                String[] keys = {sc.getAuthoritiesHolder()};
                Map claims = jwtService.getClaimsExtended(oldRefreshToken, keys);
                String sub = claims.get(JWTService.SUBJECT).toString();
                String authorities = claims.get(keys[0]).toString();
                Date iat = (Date) claims.get(JWTService.ISSUED_AT);

                String newAccessToken = jwtService.createToken(sub, authorities);
                String newRefreshToken = jwtService.createRefreshToken(sub, authorities);

                return jwtService.createLoginData(sub, newAccessToken, iat, authorities, newRefreshToken);
            } catch (JwtException e) {
                //return 401
                authenticationFacade.setAuthentication(null);
                throw new GmsSecurityException("access_token");
            }
        }
        authenticationFacade.setAuthentication(null);
        throw new GmsSecurityException("access_token");
    }

    private PersistentEntityResource signUpUser(Resource<EUser> user, Boolean emailVerified, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        EUser u = userService.signUp(user.getContent(), emailVerified);
        if (u != null) {
            return pra.toResource(u);
        }
        throw new GmsGeneralException("user.add.error", false);
    }


    /**
     * Handles all custom security exceptions.
     * @param ex {@link com.gms.util.exception.GmsSecurityException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(GmsSecurityException.class)
    protected ResponseEntity<Object> handleGmsSecurityException(GmsSecurityException ex, WebRequest req) {
        HashMap<String, Object> d = new HashMap<>();
        d.put("timestamp", System.currentTimeMillis());
        d.put("status", HttpStatus.UNAUTHORIZED.value());
        d.put("error", msg.getMessage("security.unauthorized"));
        d.put("path", dc.getApiBasePath() + "/" + ex.getPath());
        d.put(dc.getResMessageHolder(), msg.getMessage(ex.getMessage()));
        return handleExceptionInternal(ex, d, new HttpHeaders(), HttpStatus.UNAUTHORIZED, req);
    }

    /**
     * Handles all exceptions which where not specifically treated.
     * @param ex {@link GmsGeneralException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(GmsGeneralException.class)
    protected ResponseEntity<Object> handleGmsGeneralException(GmsGeneralException ex, WebRequest req) {
        String prefixCode = ex.finishedOK() ? "request.finished.OK" : "request.finished.KO";
        HashMap<String, Object> r = new HashMap<>();
        r.put(dc.getResMessageHolder(), msg.getMessage(ex.getMessage()) + ". " + msg.getMessage(prefixCode));
        return handleExceptionInternal(ex, r, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, req);
    }
}
