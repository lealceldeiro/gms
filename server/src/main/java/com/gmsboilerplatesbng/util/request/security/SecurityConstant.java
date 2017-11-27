package com.gmsboilerplatesbng.util.request.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstant {

    @Value("${security.gms.jwt.secret}")
    public static String SECRET = "SecretForGeneratingTheJWTs";

    @Value("${security.gms.jwt.expiration}")
    public static long EXPIRATION_TIME = 864_000_000; // 10 days

    @Value("${security.gms.jwt.token_holder}")
    public static final String TOKEN_HOLDER = "access_token";

    @Value("${security.gms.jwt.token_type}")
    public static final String TOKEN_TYPE = "Bearer";

    @Value("${security.gms.jwt.token_type_holder}")
    public static final String TOKEN_TYPE_HOLDER = "token_type";

    @Value("${security.gms.jwt.header}")
    public static final String HEADER = "Authorization";

    @Value("${security.gms.jwt.sign_up_url}")
    public static final String SIGN_UP_URL = "/user/sign-up";

    @Value("${security.gms.jwt.sign_in_url}")
    public static final String SIGN_IN_URL = "/login";

    @Value("${security.gms.jwt.authorities_holder}")
    public static final String AUTHORITIES_HOLDER = "authorities";

    public static final String EXPIRATION_HOLDER = "expiration_time";

    @Value("${response.auth.username}")
    public static String USERNAME_HOLDER = "username";

}
