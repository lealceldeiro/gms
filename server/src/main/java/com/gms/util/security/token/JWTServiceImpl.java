package com.gms.util.security.token;

import com.gms.util.GMSRandom;
import com.gms.util.constant.SecurityConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility service for generating jwt.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
@RequiredArgsConstructor
@Service
final class JWTServiceImpl implements JWTService {

    /**
     * Audience key to be used while generating the JWT.
     */
    private static final String AUDIENCE = Claims.AUDIENCE;
    /**
     * Expiration key to be used while generating the JWT.
     */
    private static final String EXPIRATION = Claims.EXPIRATION;
    /**
     * ID key to be used while generating the JWT.
     */
    private static final String ID = Claims.ID;
    /**
     * "Issued at" key to be used while generating the JWT.
     */
    private static final String ISSUED_AT = Claims.ISSUED_AT;
    /**
     * "Issuer" key to be used while generating the JWT.
     */
    private static final String ISSUER = Claims.ISSUER;
    /**
     * "Not before" key to be used while generating the JWT.
     */
    private static final String NOT_BEFORE = Claims.NOT_BEFORE;
    /**
     * "Subject" key to be used while generating the JWT.
     */
    private static final String SUBJECT = Claims.SUBJECT;
    /**
     * Default time to be set for the expiration time of the token.
     */
    private static final long DEFAULT_ACCESS_TOKEN_EXPIRATION_TIME = 86400000L;
    /**
     * Default time to be set for the expiration time of the refresh token.
     */
    private static final long DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L;
    /**
     * Instance of {@link SecurityConst}.
     */
    private final SecurityConst sc;
    /**
     * Instance of {@link GMSRandom}.
     */
    private final GMSRandom random = new GMSRandom(10);
    /**
     * Thousand divider.
     */
    private static final int THOUSAND = 1000;

    public long getATokenExpirationTime() {
        // expiration time should come in seconds, that's why the * 1000 and extra-zeros for 86400 seconds
        return sc.getATokenExpirationTime() > 0
                ? (sc.getATokenExpirationTime() * THOUSAND)
                : DEFAULT_ACCESS_TOKEN_EXPIRATION_TIME; // set 1 day if an invalid value was provided
    }

    public long getRTokenExpirationTime() {
        // expiration time should come in seconds, that's why the * 1000 and extra-zeros for 2592000 seconds
        return sc.getRTokenExpirationTime() > 0
                ? (sc.getRTokenExpirationTime() * THOUSAND)
                : DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME; // set 30 days if an invalid value was provided
    }

    public String createToken(final String subject) {
        return getBuilder(subject).compact();
    }

    public String createToken(final String subject, final long expiresIn) {
        return getBuilder(subject, expiresIn).compact();
    }

    public String createToken(final String subject, final String authorities) {
        return getBuilder(subject, authorities).compact();
    }

    public String createRefreshToken(final String subject, final String authorities) {
        return getBuilder(subject, authorities, getRTokenExpirationTime()).compact();
    }

    public String createToken(final String subject, final String authorities, final long expiresIn) {
        return getBuilder(subject, authorities, expiresIn).compact();
    }

    public Map<String, Object> getClaims(final String claimsJwt, final String... key) {
        Map<String, Object> r = new HashMap<>();
        processClaimsJWT(claimsJwt, r, key);

        return r;
    }

    public Map<String, Object> createLoginData(final String subject, final String accessToken,
                                               final Date issuedAt, final String authoritiesString,
                                               final String refreshToken) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put(SecurityConst.USERNAME_HOLDER, subject);
        returnMap.put(sc.getATokenHolder(), accessToken);
        returnMap.put(sc.getATokenTypeHolder(), sc.getATokenType());
        returnMap.put(sc.getATokenHeaderToBeSentHolder(), sc.getATokenHeader());
        returnMap.put(sc.getATokenExpirationTimeHolder(), issuedAt.getTime() + getATokenExpirationTime());
        returnMap.put(sc.getATokenExpiresInHolder(), getATokenExpirationTime());
        returnMap.put(sc.getIssuedTimeHolder(), issuedAt.getTime());
        returnMap.put(sc.getAuthoritiesHolder(), authoritiesString.split(SecurityConst.AUTHORITIES_SEPARATOR));
        returnMap.put(sc.getRTokenHolder(), refreshToken);
        returnMap.put(sc.getRTokenExpirationTimeHolder(), issuedAt.getTime() + getRTokenExpirationTime());
        returnMap.put(sc.getRTokenExpiresInHolder(), getRTokenExpirationTime());

        return returnMap;
    }

    public Map<String, Object> getClaimsExtended(final String claimsJwt, final String... key) {
        Map<String, Object> r = new HashMap<>();
        Claims claims = processClaimsJWT(claimsJwt, r, key);

        r.put(AUDIENCE, claims.getAudience());
        r.put(EXPIRATION, claims.getExpiration());
        r.put(ID, claims.getId());
        r.put(ISSUED_AT, claims.getIssuedAt());
        r.put(ISSUER, claims.getIssuer());
        r.put(NOT_BEFORE, claims.getNotBefore());
        r.put(SUBJECT, claims.getSubject());

        return r;
    }

    @Override
    public String issuedAtKey() {
        return ISSUED_AT;
    }

    @Override
    public String subjectKey() {
        return SUBJECT;
    }

    @Override
    public String expirationKey() {
        return EXPIRATION;
    }

    private Claims processClaimsJWT(final String claimsJwt, final Map<? super String, Object> claims,
                                    final String... key) {
        Claims pClaims = Jwts.parser()
                .setSigningKey(sc.getSecret().getBytes(StandardCharsets.UTF_8))
                .requireIssuer(sc.getIssuer())
                .parseClaimsJws(Objects.requireNonNull(claimsJwt))
                .getBody();
        if (claims != null) {
            for (String k : key) {
                if (!(k != null && "".equals(k.trim()))) {
                    claims.put(k, pClaims.get(k));
                }
            }
        }

        return pClaims;
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     *
     * @param subject     Token subject.
     * @param authorities Token authorities.
     * @param expiresIn   An expiration time expressed in milliseconds.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(final String subject, final String authorities, final long expiresIn) {
        return getBuilder(subject, expiresIn).claim(sc.getAuthoritiesHolder(), authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an authorities, an expiration time by default, an "issued_at"
     * property, a claim with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     *
     * @param subject     Token subject.
     * @param authorities Token authorities.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(final String subject, final String authorities) {
        return getBuilder(subject).claim(sc.getAuthoritiesHolder(), authorities);
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time set by default, an "issued_at" property, a claim
     * with an "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     *
     * @param subject Token subject.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(final String subject) {
        return getBuilder(subject, getATokenExpirationTime());
    }

    /**
     * Returns a {@link JwtBuilder} with a subject, an expiration time, an "issued_at" property, a claim with an
     * "expires_in" property signed with a {@link SignatureAlgorithm} (HS512).
     *
     * @param subject   Token subject.
     * @param expiresIn An expiration time expressed in milliseconds.
     * @return The {@link JwtBuilder} with the token information received as parameters.
     */
    private JwtBuilder getBuilder(final String subject, final long expiresIn) {
        long currentMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setId(random.nextString() + "_" + System.currentTimeMillis())
                .setSubject(Objects.requireNonNull(subject))
                .setExpiration(new Date(currentMillis + expiresIn))
                .setIssuedAt(new Date(currentMillis))
                .setIssuer(sc.getIssuer())
                .signWith(SignatureAlgorithm.HS512, sc.getSecret().getBytes(StandardCharsets.UTF_8))
                .claim(sc.getATokenExpiresInHolder(), expiresIn);
    }

}
