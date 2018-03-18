package com.gms.controller.security;

import com.gms.component.security.token.JWTService;
import com.gms.controller.BaseController;
import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@RestController
@BasePathAwareController
public class SecurityController extends BaseController{

    private final UserService userService;
    private final SecurityConst sc;
    private final JWTService jwtService;

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
    public ResponseEntity signUpUser(@Valid @RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        EUser u = userService.signUp(user.getContent(), false);
        if (u != null) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else throw new GmsGeneralException("user.add.error", false, HttpStatus.CONFLICT);
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
    public Map refreshToken(@Valid @RequestBody RefreshTokenPayload payload) {
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
                throw new GmsSecurityException("access_token", "security.token.refresh.invalid");
            }
        }
        throw new GmsSecurityException("access_token", "security.token.refresh.required");
    }
}
