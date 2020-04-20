package com.gms.controller.security;

import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.SecurityConstant;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import com.gms.service.security.token.JWTService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
public class SecurityController {

    /**
     * Instance of {@link UserService}.
     */
    private final UserService userService;
    /**
     * Instance of {@link SecurityConstant}.
     */
    private final SecurityConstant securityConstant;
    /**
     * Instance of {@link JWTService}.
     */
    private final JWTService jwtService;

    /**
     * Registers a new {@link EUser} setting it as active with some properties such as enabled to `true`. This method
     * is intended to be used by the Spring framework and should not be overridden. Doing so may produce unexpected
     * results.
     *
     * @param userEntityModel {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link org.springframework.web.bind.annotation.ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping("${gms.security.sign_up_url}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EUser> signUpUser(@RequestBody @Valid final EntityModel<? extends EUser> userEntityModel)
            throws GmsGeneralException {
        final EUser user = userService.signUp(userEntityModel.getContent(), UserService.EmailStatus.NOT_VERIFIED);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            throw GmsGeneralException.builder()
                    .messageI18N("user.add.error")
                    .finishedOK(false)
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
    }

    /**
     * Provides a new access token given a refresh token. This method is intended to be used by the Spring framework
     * and should not be overridden. Doing so may produce unexpected results.
     *
     * @param payload Payload with the information required to provide a new refresh token.
     * @return {@link  Map} with the new login information.
     */
    @PostMapping(SecurityConstant.ACCESS_TOKEN_URL)
    public Map<String, Object> refreshToken(@Valid @RequestBody final RefreshTokenPayload payload) {
        String oldRefreshToken = payload.getRefreshToken();
        if (oldRefreshToken != null) {
            try {
                String[] keys = {securityConstant.getAuthoritiesHolder()};
                Map<String, Object> claims = jwtService.getClaimsExtended(oldRefreshToken, keys);
                String subject = claims.get(jwtService.subjectKey()).toString();
                String authorities = claims.get(keys[0]).toString();
                Date issuedAt = (Date) claims.get(jwtService.issuedAtKey());

                String newAccessToken = jwtService.createToken(subject, authorities);
                String newRefreshToken = jwtService.createRefreshToken(subject, authorities);

                return jwtService.createLoginData(subject, newAccessToken, issuedAt, authorities, newRefreshToken);
            } catch (JwtException e) {
                //return 401
                throw new GmsSecurityException(SecurityConstant.ACCESS_TOKEN_URL, "security.token.refresh.invalid");
            }
        }
        throw new GmsSecurityException(SecurityConstant.ACCESS_TOKEN_URL, "security.token.refresh.required");
    }

}
