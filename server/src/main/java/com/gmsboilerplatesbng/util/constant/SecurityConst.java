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

    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secret;

    @Value("${gms.security.jwt.expiration:86400}")  // 1 day (in seconds)
    private long expirationTime;

    @Value("${gms.security.jwt.token_holder:access_token}")
    private String tokenHolder;

    @Value("${gms.security.jwt.token_type:Bearer}")
    private String tokenType;

    @Value("${gms.security.jwt.token_type_holder:token_type}")
    private String tokenTypeHolder;

    @Value("${gms.security.jwt.header:Authorization}")
    private String header;

    @Value("${gms.security.jwt.header_to_be_sent_holder:header_to_be_sent}")
    private String headerToBeSentHolder;

    @Value("${gms.security.jwt.sign_up_url}")
    private String signUpUrl;

    @Value("${gms.security.jwt.sign_in_url:/login}")
    private String signInUrl;

    @Value("${gms.security.jwt.sign_out_url:/logout}")
    private String signOutUrl;

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolder;

    public static final String AUTHORITIES_SEPARATOR = ";";

    @Value("${gms.security.jwt.expiration_time_holder:expiration_time}")
    private String expirationHolder;

    @Value("${gms.security.jwt.expires_in_holder:expires_in}")
    private String expiresInHolder;

    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    private String issuedTimeHolder;

    @Value("${gms.response.auth.username:username}")
    private String usernameHolder;

    public static final String PASS_HOLDER = "password";

}
