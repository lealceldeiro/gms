package com.gms.service.security.token;

import java.util.Date;
import java.util.Map;

/**
 * Defines how the jwt arte generated.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface JWTService {

    /**
     * Return the expiration time defined for the access token. If an invalid value was provided, the default one
     * (86400s) is used instead.
     *
     * @return long expiration time.
     */
    long getATokenExpirationTime();

    /**
     * Return the expiration time defined for the refresh token. If an invalid value was provided, the default one
     * (2592000s) is used instead.
     *
     * @return long expiration time.
     */
    long getRTokenExpirationTime();

    /**
     * Returns json web token with a subject, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link io.jsonwebtoken.SignatureAlgorithm} (HS512).
     *
     * @param subject Token subject.
     * @return {@link String} The token with the information received as parameters.
     */
    String createToken(String subject);

    /**
     * Returns json web token with a subject, an expiration time, an "issued_at" property, a claim with an "expires_in"
     * property signed with a {@link io.jsonwebtoken.SignatureAlgorithm} (HS512).
     *
     * @param subject   Token subject.
     * @param expiresIn Expiration time.
     * @return {@link String} The token with the information received as parameters.
     */
    String createToken(String subject, long expiresIn);

    /**
     * Returns json web token with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link io.jsonwebtoken.SignatureAlgorithm} (HS512).
     *
     * @param subject     Token subject.
     * @param authorities Token authorities.
     * @return {@link String} The token with the information received as parameters.
     */
    String createToken(String subject, String authorities);

    /**
     * Returns json web token with a subject, an authorities, an expiration time by default, an "issued_at" property,
     * a claim with an "expires_in" property signed with a {@link io.jsonwebtoken.SignatureAlgorithm} (HS512).
     * This is a convenience method for using instead of generating token using
     * {@link JWTService#createToken(String, String, long)} and setting the default expiration time for the refresh
     * token.
     *
     * @param subject     Token subject.
     * @param authorities Token authorities.
     * @return {@link String} The token with the information received as parameters.
     */
    String createRefreshToken(String subject, String authorities);

    /**
     * Returns json web token with a subject, an authorities, an expiration time, an "issued_at" property, a claim with
     * an "expires_in" property signed with a {@link io.jsonwebtoken.SignatureAlgorithm} (HS512).
     *
     * @param subject     Token subject.
     * @param authorities Token authorities.
     * @param expiresIn   An expiration time expressed in milliseconds.
     * @return {@link String} The token with the information received as parameters.
     */
    String createToken(String subject, String authorities, long expiresIn);

    /**
     * Returns a {@link Map} which contains, for every key provided (as a {@code key} param), an entry with the
     * claim value. In order to get the value of the claims, the body provided as {@code claimsJwt} param, is parsed.
     * The value of every claim is retrieved from the claims using the same key in the {@code key} param.
     *
     * @param claimsJwt A compact serialized Claim JWT serialized.
     * @param key       Key to be used in order to get the claim. More than one key can be provided and all of them
     *                  will be used to try to retrieve a new claim under the same key.
     * @return Map with all the required information.
     */
    Map<String, Object> getClaims(String claimsJwt, String... key);

    /**
     * This is a convenience method for generating the map returned when the user logs in or request a new access token.
     *
     * @param subject           The subject set on the jwt.
     * @param accessToken       The access token generated.
     * @param issuedAt          Date when the token was issued.
     * @param authoritiesString The principal authorities in the format of
     *                          {@code AUTHORITY_NAME-separator-AUTHORITY_NAME}
     *                          where {@code -separator-} is defined in
     *                          {@link com.gms.util.constant.SecurityConstant#AUTHORITIES_SEPARATOR}.
     * @param refreshToken      Refresh token generated in this request.
     * @return Map with all this information set under the key defined in {@link com.gms.util.constant.SecurityConstant}
     * intended for that.
     */
    Map<String, Object> createLoginData(String subject, String accessToken, Date issuedAt, String authoritiesString,
                                        String refreshToken);

    /**
     * Returns a {@link Map} which contains, for every key provided (as a {@code key} param), an entry with the
     * claim value. In order to get the value of the claims, the body provided as {@code claimsJwt} param, is parsed.
     * The value of every claim is retrieved from the claims using the same key in the {@code key} param.
     * Also extra data such as "sub", "aud", etc, included in the jwt is included.
     *
     * @param claimsJwt A compact serialized Claim JWT serialized.
     * @param key       Key to be used in order to get the claim. More than one key can be provided and all of them
     *                  will be used to try to retrieve a new claim under the same key.
     * @return Map with all the required information.
     * @see io.jsonwebtoken.Claims
     */
    Map<String, Object> getClaimsExtended(String claimsJwt, String... key);

    /**
     * Returns the key used to specify the value where the token was issued at.
     *
     * @return String with the corresponding key.
     */
    String issuedAtKey();

    /**
     * Returns the key used to specify the value of the token subject.
     *
     * @return String with the corresponding key.
     */
    String subjectKey();

    /**
     * Returns the key used to specify the expiration.
     *
     * @return String with the corresponding key.
     */
    String expirationKey();

}
