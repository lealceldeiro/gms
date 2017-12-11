package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConst {

    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    public String SECRET;

    @Value("${gms.security.jwt.expiration:86400}")  // 1 day (in seconds)
    public long EXPIRATION_TIME;

    @Value("${gms.security.jwt.token_holder:access_token}")
    public String TOKEN_HOLDER;

    @Value("${gms.security.jwt.token_type:Bearer}")
    public String TOKEN_TYPE;

    @Value("${gms.security.jwt.token_type_holder:token_type}")
    public String TOKEN_TYPE_HOLDER;

    @Value("${gms.security.jwt.header:Authorization}")
    public String HEADER;

    @Value("${gms.security.jwt.header_to_be_sent_holder:header_to_be_sent}")
    public String HEADER_TO_BE_SENT_HOLDER;

    @Value("${gms.security.jwt.sign_up_url}")
    public String SIGN_UP_URL;

    @Value("${gms.security.jwt.sign_in_url:/login}")
    public String SIGN_IN_URL;

    @Value("${gms.security.jwt.sign_out_url:/logout}")
    public String SIGN_OUT_URL;

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    public String AUTHORITIES_HOLDER;

    public String AUTHORITIES_SEPARATOR = ";";

    @Value("${gms.security.jwt.expiration_time_holder:expiration_time}")
    public String EXPIRATION_HOLDER;

    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    public String ISSUED_TIME_HOLDER;

    @Value("${gms.response.auth.username:username}")
    public String USERNAME_HOLDER;

    public String PASSWORD_HOLDER = "password";

}
