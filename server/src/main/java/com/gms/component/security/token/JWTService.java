package com.gms.component.security.token;

import com.gms.util.GMSRandom;
import com.gms.util.constant.SecurityConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * JWTService
 * Utility service for generating jwt.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
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

    public static final long DEFAULT_ACCESS_TOKEN_EXPIRATION_TIME = 86400000L;
    public static final long DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L;

    private final SecurityConst sc;

    private final GMSRandom random = new GMSRandom(10);

    /**
     * Return the expiration time defined for the access token. If an invalid value was provided, the default one (86400s)
     * is used instead.
     * @return long expiration time.
     */
    public long getATokenExpirationTime() {
        // expiration time should come in seconds, that's why the * 1000 and extra-zeros for 86400 seconds
        return sc.getATokenExpirationTime() > 0 ? (sc.getATokenExpirationTime() * 1000) : DEFAULT_ACCESS_TOKEN_EXPIRATION_TIME; // set 1 day if an invalid value was provided
    }

    /**
     * Return the expiration time defined for the refresh token. If an invalid value was provided, the default one (2592000s)
     * is used instead.
     * @return long expiration time.
     */
    public long getRTokenExpirationTime() {
        // expiration time should come in seconds, that's why the * 1000 and extra-zeros for 2592000 seconds
        return sc.getRTokenExpirationTime() > 0 ? (sc.getRTokenExpirationTime() * 1000) : DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME; // set 30 days if an invalid value was provided
    }

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
     * Returns json web token with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * This is a convenience method for using instead of generating token using {@link JWTService#createToken(String, String, long)}
     * and setting the default expiration time for the refresh token.
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @return {@link String} The token with the information received as parameters.
     */
    public String createRefreshToken(String subject, String authorities) {
        JwtBuilder builder = getBuilder(subject, authorities, getRTokenExpirationTime());
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
        return getBuilder(subject, authorities, expiresIn).compact();
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
                .setSigningKey(sc.getSecret().getBytes())
                .requireIssuer(sc.getIssuer())
                .parseClaimsJws(claimsJwt)
                .getBody();
        for (String k : key) {
            if (!(k != null && k.trim().equals(""))){
                r.put(k, claims.get(k));
            }
        }
        return r;
    }

    /**
     * This is a convenience method for generating the map returned when the user logs in or request a new access token.
     * @param subject The subject set on the jwt.
     * @param accessToken The access token generated.
     * @param issuedAt Date when the token was issued.
     * @param authoritiesString The principal authorities in the format of "AUTHORITY_NAME<separator>AUTHORITY_NAME" where
     * <separator> is defined in {@link SecurityConst#AUTHORITIES_SEPARATOR}.
     * @param refreshToken Refresh token generated in this request.
     * @return Map with all this information set under the key defined in {@link SecurityConst} intended for that.
     */
    public Map createLoginData(String subject, String accessToken, Date issuedAt,
                               String authoritiesString, String refreshToken) {
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put(SecurityConst.USERNAME_HOLDER, subject);
        returnMap.put(sc.getATokenHolder(), accessToken);
        returnMap.put(sc.getATokenTypeHolder(), sc.getATokenType());
        returnMap.put(sc.getATokenHeaderToBeSentHolder(), sc.getATokenHeader());
        returnMap.put(sc.getExpirationHolder(), issuedAt.getTime() + getATokenExpirationTime());
        returnMap.put(sc.getExpiresInHolder(), getATokenExpirationTime());
        returnMap.put(sc.getIssuedTimeHolder(), issuedAt.getTime());
        returnMap.put(sc.getAuthoritiesHolder(), authoritiesString.split(SecurityConst.AUTHORITIES_SEPARATOR));
        returnMap.put(sc.getRTokenHolder(), refreshToken);

        return returnMap;
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
                .setSigningKey(sc.getSecret().getBytes())
                .requireIssuer(sc.getIssuer())
                .parseClaimsJws(claimsJwt)
                .getBody();
        for (String k : key) {
            if (!(k != null && k.trim().equals(""))){
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
        return getBuilder(subject, expiresIn).claim(sc.getAuthoritiesHolder(), authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @param authorities Token authorities.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject, String authorities) {
        return getBuilder(subject).claim(sc.getAuthoritiesHolder(), authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time set by default, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     * @param subject Token subject.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(String subject) {
        return getBuilder(subject, getATokenExpirationTime());
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
                .setId(random.nextString() + "_" + valueOf(System.currentTimeMillis()))
                .setSubject(subject)
                .setExpiration(new Date(currentMillis + expiresIn))
                .setIssuedAt(new Date(currentMillis))
                .setIssuer(sc.getIssuer())
                .signWith(SignatureAlgorithm.HS512, sc.getSecret().getBytes())
                .claim(sc.getExpiresInHolder(), expiresIn);
    }
}
