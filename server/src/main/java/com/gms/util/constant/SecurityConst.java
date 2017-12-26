package com.gms.util.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SecurityConst
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Component
@Getter
public class SecurityConst {

    //region auth request
    @Value("${gms.request.auth.username:username}")
    private String reqUsernameHolder;

    @Value("${gms.request.auth.password:password}")
    private String reqPasswordHolder;
    //endregion

    //region token
    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secret;

    //region access token
    /**
     * Expiration of the access token. Time in which the token will not longer be valid in seconds.
     */
    @Value("${gms.security.jwt.token_expiration:86400}")  // 1 day (in seconds)
    private long aTokenExpirationTime;

    /**
     * Variable in which the access token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.token_holder:access_token}")
    private String aTokenHolder;

    /**
     * Token scheme to be used. "Bearer" by default.
     */
    @Value("${gms.security.jwt.token_type:Bearer}")
    private String aTokenType;

    /**
     * Variable in which the {@link SecurityConst#aTokenType} will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.token_type_holder:token_type}")
    private String aTokenTypeHolder;

    /**
     * Header which needs to be sent by the clients specifying the access token.
     */
    @Value("${gms.security.jwt.token_header:Authorization}")
    private String aTokenHeader;

    /**
     * Variable in which the {@link SecurityConst#aTokenHeader} will be sent  in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.token_header_to_be_sent_holder:header_to_be_sent}")
    private String aTokenHeaderToBeSentHolder;
    //endregion

    //region refresh token
    /**
     * Expiration of the refresh token. Time in which the token will not longer be valid in seconds.
     */
    @Value("${gms.security.jwt.refresh_token_expiration:2592000}")
    private long rTokenExpirationTime;

    /**
     * Variable in which the access token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.refresh_token_holder:refresh_token}")
    private String rTokenHolder;
    //endregion

    /**
     * Variable in which the time of validity of the token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.expires_in_holder:expires_in}")
    private String expiresInHolder;

    /**
     * Variable in which the time where the token was issued will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    private String issuedTimeHolder;

    @Value("${gms.security.jwt.expiration_time_holder:expiration_time}")
    private String expirationHolder;

    @Value("${gms.security.jwt.issuer:www.gms.com}")
    private String issuer;

    //endregion

    //region sign in/up/out URLs
    @Value("${gms.security.sign_up_url}")
    private String signUpUrl;

    @Value("${gms.security.sign_in_url:/login}")
    private String signInUrl;
    //endregion

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolder;

    @Value("${gms.security.free_url_any:}")
    private String freeURLsAnyRequest;

    @Value("${gms.security.free_url_post:}")
    private String freeURLsPostRequest;

    @Value("${gms.security.free_url_get:}")
    private String freeURLsGetRequest;

    public static final String AUTHORITIES_SEPARATOR = ";";

    /**
     * URL (relative to base api path) for getting a new access token from a previously gotten refresh token.
     */
    public static final String ACCESS_TOKEN_URL = "access_token";

    @Value("${gms.response.auth.username:username}")
    private String usernameHolder;

    public static final String PASS_HOLDER = "password";

    public String [] getFreeURLsAnyRequest() {
        return freeURLsAnyRequest.split(";");
    }

    public String [] getFreeURLsGetRequest() {
        return freeURLsGetRequest.split(";");
    }

    public String [] getFreeURLsPostRequest() {
        return freeURLsPostRequest.split(";");
    }

}
