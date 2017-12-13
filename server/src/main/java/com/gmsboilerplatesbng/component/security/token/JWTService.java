package com.gmsboilerplatesbng.component.security.token;

import com.gmsboilerplatesbng.util.constant.SecurityConst;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTService
 * Utility service for generating jwt.
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 * @version 0.1
 * Dec 13, 2017
 */
@SuppressWarnings("WeakerAccess")
@RequiredArgsConstructor
@Component
public class JWTService {

    public static final String AUDIENCE = Claims.AUDIENCE;
    public static final String EXPIRATION = Claims.EXPIRATION;
    public static final String ID = Claims.ID;
    public static final String ISSUED_AT = Claims.ISSUED_AT;
    public static final String ISSUER = Claims.ISSUER;
    public static final String NOT_BEFORE = Claims.NOT_BEFORE;
    public static final String SUBJECT = Claims.SUBJECT;

    private final SecurityConst sc;

    /**
     * Returns json web token with a subject, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject) {
        JwtBuilder builder = getBuilder(subject);
        return builder.compact();
    }

    /**
     * Returns json web token with a subject, an expiration time, an "issued_at" property, a claim with an "expires_in"
     * property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createToken(String subject, long expiresIn) {
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
    public String createToken(String subject, String authorities) {
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
    public String createToken(String subject, String authorities, long expiresIn) {
        JwtBuilder builder = getBuilder(subject, authorities, expiresIn);
        return builder.compact();
    }

    /**
     * Returns a {@link Map} which contains, for every key provided (as a <code>key</code> param), an entry with the
     * claim value. In order to get the value of the claims, the body provided as <code>claimsJwt</code> param, is parsed.
     * The value of every claim is retrieved from the claims using the same key in the <code>key</code> param.
     * @param claimsJwt A compact serialized Claim JWT serialized.
     * @param key Key to be used in order to get the claim. More than one key can be provided and all of them will be used
     *            to try to retrieve a new claim under the same key.
     * @return Map with all the required information.
     */
    public Map getClaims(String claimsJwt, String... key) {
        Map<Object, Object> r = new HashMap<>();
        Claims claims = Jwts.parser()
                .setSigningKey(sc.secret.getBytes())
                .parseClaimsJws(claimsJwt)
                .getBody();
        for (String k : key) {
            if(!(k != null && k.trim().equals(""))){
                r.put(k, claims.get(k));
            }
        }
        return r;
    }

    /**
     * Returns a {@link Map} which contains, for every key provided (as a <code>key</code> param), an entry with the
     * claim value. In order to get the value of the claims, the body provided as <code>claimsJwt</code> param, is parsed.
     * The value of every claim is retrieved from the claims using the same key in the <code>key</code> param. Also extra data
     * such as "sub", "aud", etc, included in the jwt is included.
     * @param claimsJwt A compact serialized Claim JWT serialized.
     * @param key Key to be used in order to get the claim. More than one key can be provided and all of them will be used
     *            to try to retrieve a new claim under the same key.
     * @return Map with all the required information.
     * @see Claims
     */

    public Map getClaimsExtended(String claimsJwt, String... key) {
        Map<Object, Object> r = new HashMap<>();
        Claims claims = Jwts.parser()
                .setSigningKey(sc.secret.getBytes())
                .parseClaimsJws(claimsJwt)
                .getBody();
        for (String k : key) {
            if(!(k != null && k.trim().equals(""))){
                r.put(k, claims.get(k));
            }
        }
        r.put(AUDIENCE, claims.getAudience());
        r.put(EXPIRATION, claims.getExpiration());
        r.put(ID, claims.getId());
        r.put(ISSUED_AT, claims.getIssuedAt());
        r.put(ISSUER, claims.getIssuer());
        r.put(NOT_BEFORE, claims.getNotBefore());
        r.put(SUBJECT, claims.getSubject());

        return r;
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @param expiresIn An expiration time expressed in milliseconds.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, String authorities, long expiresIn) {
        return getBuilder(subject, expiresIn).claim(sc.authoritiesHolder, authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, String authorities) {
        return getBuilder(subject).claim(sc.authoritiesHolder, authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time set by default, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject) {
        long expiresIn = sc.expirationTime * 1000; // expiration time should come in seconds
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
                .signWith(SignatureAlgorithm.HS512, sc.secret.getBytes())
                .claim(sc.expiresInHolder, expiresIn);
    }
}
