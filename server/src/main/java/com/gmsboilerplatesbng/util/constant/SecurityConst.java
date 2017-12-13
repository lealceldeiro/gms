package com.gmsboilerplatesbng.util.constant;

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
public class SecurityConst {

    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    public String secret;

    @Value("${gms.security.jwt.expiration:86400}")  // 1 day (in seconds)
    public long expirationTime;

    @Value("${gms.security.jwt.token_holder:access_token}")
    public String tokenHolder;

    @Value("${gms.security.jwt.token_type:Bearer}")
    public String tokenType;

    @Value("${gms.security.jwt.token_type_holder:token_type}")
    public String tokenTypeHolder;

    @Value("${gms.security.jwt.header:Authorization}")
    public String header;

    @Value("${gms.security.jwt.header_to_be_sent_holder:header_to_be_sent}")
    public String headerToBeSentHolder;

    @Value("${gms.security.jwt.sign_up_url}")
    public String signUpUrl;

    @Value("${gms.security.jwt.sign_in_url:/login}")
    public String signInUrl;

    @Value("${gms.security.jwt.sign_out_url:/logout}")
    public String signOutUrl;

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    public String authoritiesHolder;

    public static final String AUTHORITIES_SEPARATOR = ";";

    @Value("${gms.security.jwt.expiration_time_holder:expiration_time}")
    public String expirationHolder;

    @Value("${gms.security.jwt.expires_in_holder:expires_in}")
    public String expiresInHolder;

    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    public String issuedTimeHolder;

    @Value("${gms.response.auth.username:username}")
    public String usernameHolder;

    public static final String PASS_HOLDER = "password";

}
