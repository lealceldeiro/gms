package com.gms.util.constant;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Component
@Getter
public class SecurityConst {

    //region auth request
    /**
     * Variable in which the username (or any other user identifier) is sent to the login request.
     */
    @Value("${gms.request.auth.username:username}")
    private String reqUsernameHolder;

    /**
     * Variable in which the User's password is sent to the login request.
     */
    @Value("${gms.request.auth.password:password}")
    private String reqPasswordHolder;
    //endregion

    //region token
    /**
     * Secret for signing and verifying the jwt.
     */
    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secret;

    /**
     * Variable in which the time where the token was issued will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    private String issuedTimeHolder;

    /**
     * Variable which will contain the authorities granted to the logged user.
     */
    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolder;

    /**
     * Jwt issuer.
     */
    @Value("${gms.security.jwt.issuer:www.gms.com}")
    private String issuer;

    //region access token

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

    /**
     * Expiration of the access token. Time in which the token will not longer be valid in seconds.
     */
    @Value("${gms.security.jwt.token_expiration:86400}")  // 1 day (in seconds)
    private long aTokenExpirationTime;

    /**
     * Variable in which the access token expiration time will be sent in the login response.
     */
    @Value("${gms.security.jwt.token_expiration_time_holder:token_expiration_time}")
    private String aTokenExpirationTimeHolder;

    /**
     * Variable in which the time of validity of the access token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.token_expires_in_holder:token_expires_in}")
    private String aTokenExpiresInHolder;
    //endregion

    //region refresh token
    /**
     * Variable in which the refresh token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.refresh_token_holder:refresh_token}")
    private String rTokenHolder;

    /**
     * Expiration of the refresh token. Time in which the token will not longer be valid in seconds.
     */
    @Value("${gms.security.jwt.refresh_token_expiration:2592000}") // 30 days in seconds
    private long rTokenExpirationTime;

    /**
     * Variable in which the refresh token expiration time will be sent in the login response.
     */
    @Value("${gms.security.jwt.refresh_token_expiration_time_holder:refresh_token_expiration_time}")
    private String rTokenExpirationTimeHolder;

    /**
     * Variable in which the time of validity of the access token will be sent in the response in the login information to the client.
     */
    @Value("${gms.security.jwt.refresh_token_expires_in_holder:refresh_token_expires_in}")
    private String rTokenExpiresInHolder;
    //endregion

    //endregion

    //region sign in/up/out URLs
    /**
     * Url for accepting new user via sign-up.
     */
    @Value("${gms.security.sign_up_url}")
    private String signUpUrl;

    /**
     * Url for processing the login request.
     */
    @Value("${gms.security.sign_in_url:/login}")
    private String signInUrl;
    //endregion

    /**
     * Url which are not checked by the security mechanism.
     */
    @Getter(AccessLevel.NONE)
    @Value("${gms.security.free_url_any:}")
    private String freeURLsAnyRequest;

    /**
     * Url which are not checked by the security mechanism when the HttpRequestMethod is POST.
     */
    @Getter(AccessLevel.NONE)
    @Value("${gms.security.free_url_post:}")
    private String freeURLsPostRequest;

    /**
     * Url which are not checked by the security mechanism when the HttpRequestMethod is GET.
     */
    @Getter(AccessLevel.NONE)
    @Value("${gms.security.free_url_get:}")
    private String freeURLsGetRequest;

    /**
     * String separator used for concatenating the user authorities in the login response.
     */
    public static final String AUTHORITIES_SEPARATOR = ";";

    /**
     * URL (relative to base api path) for getting a new access token from a previously gotten refresh token.
     */
    public static final String ACCESS_TOKEN_URL = "access_token";

    /**
     * Variable in which the {@link com.gms.domain.security.user.EUser}'s username will be sent in the response of the
     * login request.
     */
    @Value("${gms.response.auth.username:username}")
    private String usernameHolder;

    /**
     * Password holder for storing the username in a token's claims
     */
    public static final String USERNAME_HOLDER = "username";

    /**
     * No more than 255 characters and no less than 1
     * No special characters except underscore and hyphen ("_" and "-"). Those two characters cannot be the one after the
     * other and the username can't start or end with them.
     */
    public static final String USERNAME_REGEXP = "^(?!.*[-_]{2,})(?=^[^-_].*[^-_]$)[\\w-]{1,255}$";

    public String[] getFreeURLsAnyRequest() {
        return freeURLsAnyRequest.split(";");
    }

    public String[] getFreeURLsGetRequest() {
        return freeURLsGetRequest.split(";");
    }

    public String[] getFreeURLsPostRequest() {
        return freeURLsPostRequest.split(";");
    }

}
