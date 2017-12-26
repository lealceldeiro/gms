package com.gms.controller.security;

import com.gms.component.security.authentication.IAuthenticationFacade;
import com.gms.component.security.token.JWTService;
import com.gms.controller.BaseController;
import com.gms.domain.security.user.EUser;
import com.gms.service.configuration.ConfigurationService;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * SecurityController
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@BasePathAwareController
public class SecurityController extends BaseController{

    private final UserService userService;
    private final ConfigurationService configuration;
    private final SecurityConst sc;
    private final JWTService jwtService;
    private final IAuthenticationFacade authenticationFacade;


    @Autowired
    public SecurityController(UserService userService, MessageResolver messageResolver,
                              ConfigurationService configuration, DefaultConst defaultConst, SecurityConst sc,
                              JWTService jwtService, IAuthenticationFacade authenticationFacade) {
        super(defaultConst);
        this.userService = userService;
        this.msg = messageResolver;
        this.configuration = configuration;
        this.sc = sc;
        this.jwtService = jwtService;
        this.authenticationFacade = authenticationFacade;
    }

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
    @PreAuthorize("permitAll()")
    @ResponseBody
    public PersistentEntityResource signUp(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        if (configuration.isUserUserRegistrationAllowed()) {
            return signUpUser(user, false, pra);
        }
        else throw new GmsGeneralException("user.add.not_allowed", false);
    }

    /**
     * Provides a new access token given a refresh token.
     * @return Map with the same information provided in the
     * {@link com.gms.config.security.JWTAuthenticationFilter#successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)}
     * method.
     */
    @PostMapping("access_token")
    @PreAuthorize("permitAll()")
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
            throws GmsGeneralException{
        EUser u = userService.signUp(user.getContent(), emailVerified);
        if (u != null) {
            return pra.toResource(u);
        }
        else throw new GmsGeneralException("user.add.error", false);
    }
}
