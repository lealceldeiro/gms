package com.gmsboilerplatesbng.util.constant;

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

    //region token
    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secret;

    //region access token
    /**
     * Expiration of the access token. Time in which the token will not longer be valid.
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
    //endregion

    //region sign in/out URLs
    @Value("${gms.security.sign_up_url}")
    private String signUpUrl;

    @Value("${gms.security.sign_in_url:/login}")
    private String signInUrl;

    @Value("${gms.security.sign_out_url:/logout}")
    private String signOutUrl;
    //endregion

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolder;

    @Value("${gms.security.jwt.issuer:www.gms.com}")
    private String issuer;

    public static final String AUTHORITIES_SEPARATOR = ";";

    @Value("${gms.response.auth.username:username}")
    private String usernameHolder;

    public static final String PASS_HOLDER = "password";

}
