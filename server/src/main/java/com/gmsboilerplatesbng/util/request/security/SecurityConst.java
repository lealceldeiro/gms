package com.gmsboilerplatesbng.util.request.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConst {

    @Value("${gms.security.jwt.secret}")
    public String SECRET = "SecretForGeneratingTheJWTs";

    @Value("${gms.security.jwt.expiration}")
    public long EXPIRATION_TIME = 604800000; // 1 week

    @Value("${gms.security.jwt.token_holder}")
    public String TOKEN_HOLDER = "access_token";

    @Value("${gms.security.jwt.token_type}")
    public String TOKEN_TYPE = "Bearer";

    @Value("${gms.security.jwt.token_type_holder}")
    public String TOKEN_TYPE_HOLDER = "token_type";

    @Value("${gms.security.jwt.header}")
    public String HEADER = "Authorization";

    @Value("${gms.security.jwt.sign_up_url}")
    public String SIGN_UP_URL = "/sign-up";

    @Value("${gms.security.jwt.sign_in_url}")
    public String SIGN_IN_URL = "/login";

    @Value("${gms.security.jwt.authorities_holder}")
    public String AUTHORITIES_HOLDER = "authorities";

    public String EXPIRATION_HOLDER = "expiration_time";

    @Value("${gms.response.auth.username}")
    public String USERNAME_HOLDER = "username";

}
