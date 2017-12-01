package com.gmsboilerplatesbng.util.request.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConst {

    @Value("${gms.security.jwt.secret:SecretForGeneratingTheJWTs}")
    public String SECRET;

    @Value("${gms.security.jwt.expiration:604800000}")  // 1 week
    public long EXPIRATION_TIME;

    @Value("${gms.security.jwt.token_holder:access_token}")
    public String TOKEN_HOLDER;

    @Value("${gms.security.jwt.token_type:Bearer}")
    public String TOKEN_TYPE;

    @Value("${gms.security.jwt.token_type_holder:token_type}")
    public String TOKEN_TYPE_HOLDER;

    @Value("${gms.security.jwt.header:Authorization}")
    public String HEADER;

    @Value("${gms.security.jwt.sign_up_url}")
    public String SIGN_UP_URL;

    @Value("${gms.security.jwt.sign_in_url}")
    public String SIGN_IN_URL;

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    public String AUTHORITIES_HOLDER;

    @Value("${gms.security.jwt.expiration_time_holder:expiration_time}")
    public String EXPIRATION_HOLDER;

    @Value("${gms.response.auth.username:username}")
    public String USERNAME_HOLDER;

}
