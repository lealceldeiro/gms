package com.gmsboilerplatesbng.component.security.token;

import com.gmsboilerplatesbng.util.constant.SecurityConst;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWTService
 * Utility service for generating jwt.
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 * @version 0.1
 * Dec 13, 2017
 */
@RequiredArgsConstructor
@Component
public class JWTService {

    private final SecurityConst sc;

    /**
     * Returns json web token with a subject, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject){
        JwtBuilder builder = getBuilder(subject);
        return builder.compact();
    }

    /**
     * Returns json web token with a subject, an expiration time, an "issued_at" property, a claim with an "expires_in"
     * property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject, long expiresIn){
        JwtBuilder builder = getBuilder(subject, expiresIn);
        return builder.compact();
    }

    /**
     * Returns json web token with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject, String authorities){
        JwtBuilder builder = getBuilder(subject, authorities);
        return builder.compact();
    }

    /**
     * Returns json web token with a subject, an authorities, an expiration time, an "issued_at" property, a claim with
     * an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @param expiresIn An expiration time expressed in milliseconds.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject, String authorities, long expiresIn){
        JwtBuilder builder = getBuilder(subject, authorities, expiresIn);
        return builder.compact();
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @param expiresIn An expiration time expressed in milliseconds.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, String authorities, long expiresIn){
        return getBuilder(subject, expiresIn).claim(sc.AUTHORITIES_HOLDER, authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, String authorities){
        return getBuilder(subject).claim(sc.AUTHORITIES_HOLDER, authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time set by default, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject) {
        long expiresIn = sc.EXPIRATION_TIME * 1000; // expiration time should come in seconds
        return getBuilder(subject, expiresIn);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time, an "issued_at" property, a claim with an
     * "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param expiresIn An expiration time expressed in milliseconds.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, long expiresIn) {
        long currentMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(currentMillis + expiresIn))
                .setIssuedAt(new Date(currentMillis))
                .signWith(SignatureAlgorithm.HS512, sc.SECRET.getBytes())
                .claim(sc.EXPIRES_IN_HOLDER, expiresIn);
    }
}
