package com.gms.util.constant;

import com.gms.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecurityConstTest {

    @Value("${gms.request.auth.username:username}")
    private String reqUsernameHolderBind;

    @Value("${gms.request.auth.password:password}")
    private String reqPasswordHolderBind;

    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secretBind;

    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    private String issuedTimeHolderBind;

    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolderBind;

    @Value("${gms.security.jwt.issuer:www.gms.com}")
    private String issuerBind;

    @Value("${gms.security.jwt.token_holder:access_token}")
    private String aTokenHolderBind;

    @Value("${gms.security.jwt.token_type:Bearer}")
    private String aTokenTypeBind;

    @Value("${gms.security.jwt.token_type_holder:token_type}")
    private String aTokenTypeHolderBind;

    @Value("${gms.security.jwt.token_header:Authorization}")
    private String aTokenHeaderBind;

    @Value("${gms.security.jwt.token_header_to_be_sent_holder:header_to_be_sent}")
    private String aTokenHeaderToBeSentHolderBind;

    @Value("${gms.security.jwt.token_expiration:86400}")
    private long aTokenExpirationTimeBind;

    @Value("${gms.security.jwt.token_expiration_time_holder:token_expiration_time}")
    private String aTokenExpirationTimeHolderBind;

    @Value("${gms.security.jwt.token_expires_in_holder:token_expires_in}")
    private String aTokenExpiresInHolderBind;

    @Value("${gms.security.jwt.refresh_token_holder:refresh_token}")
    private String rTokenHolderBind;

    @Value("${gms.security.jwt.refresh_token_expiration:2592000}") // 30 days in seconds
    private long rTokenExpirationTimeBind;

    @Value("${gms.security.jwt.refresh_token_expiration_time_holder:refresh_token_expiration_time}")
    private String rTokenExpirationTimeHolderBind;

    @Value("${gms.security.jwt.refresh_token_expires_in_holder:refresh_token_expires_in}")
    private String rTokenExpiresInHolderBind;

    @Value("${gms.security.sign_up_url}")
    private String signUpUrlBind;

    @Value("${gms.security.sign_in_url:/login}")
    private String signInUrlBind;

    @Value("${gms.security.free_url_any:}")
    private String freeURLsAnyRequestBind;

    @Value("${gms.security.free_url_post:}")
    private String freeURLsPostRequestBind;

    @Value("${gms.security.free_url_get:}")
    private String freeURLsGetRequestBind;

    @Value("${gms.response.auth.username:username}")
    private String usernameHolderBind;

    @Autowired private SecurityConst autowiredSc;
    private SecurityConst entity;

    @Test
    public void defaultValuesLoadedProperly() {
        assertEquals(reqUsernameHolderBind, autowiredSc.getReqUsernameHolder());
        assertEquals(reqPasswordHolderBind, autowiredSc.getReqPasswordHolder());
        assertEquals(secretBind, autowiredSc.getSecret());
        assertEquals(issuedTimeHolderBind, autowiredSc.getIssuedTimeHolder());
        assertEquals(authoritiesHolderBind, autowiredSc.getAuthoritiesHolder());
        assertEquals(issuerBind, autowiredSc.getIssuer());
        assertEquals(aTokenHolderBind, autowiredSc.getATokenHolder());
        assertEquals(aTokenTypeBind, autowiredSc.getATokenType());
        assertEquals(aTokenTypeHolderBind, autowiredSc.getATokenTypeHolder());
        assertEquals(aTokenHeaderBind, autowiredSc.getATokenHeader());
        assertEquals(aTokenHeaderToBeSentHolderBind, autowiredSc.getATokenHeaderToBeSentHolder());
        assertEquals(aTokenExpirationTimeBind, autowiredSc.getATokenExpirationTime());
        assertEquals(aTokenExpirationTimeHolderBind, autowiredSc.getATokenExpirationTimeHolder());
        assertEquals(aTokenExpiresInHolderBind, autowiredSc.getATokenExpiresInHolder());
        assertEquals(rTokenHolderBind, autowiredSc.getRTokenHolder());
        assertEquals(rTokenExpirationTimeBind, autowiredSc.getRTokenExpirationTime());
        assertEquals(rTokenExpirationTimeHolderBind, autowiredSc.getRTokenExpirationTimeHolder());
        assertEquals(rTokenExpiresInHolderBind, autowiredSc.getRTokenExpiresInHolder());
        assertEquals(signUpUrlBind, autowiredSc.getSignUpUrl());
        assertEquals(signInUrlBind, autowiredSc.getSignInUrl());
        assertEquals(usernameHolderBind, autowiredSc.getUsernameHolder());
    }

    @Test
    public void getFreeURLsAnyRequest() {
        assertArrayEquals(autowiredSc.getFreeURLsAnyRequest(), freeURLsAnyRequestBind.split(";"));
    }

    @Test
    public void getFreeURLsGetRequest() {
        Assert.assertArrayEquals(autowiredSc.getFreeURLsGetRequest(), freeURLsGetRequestBind.split(";"));
    }

    @Test
    public void getFreeURLsPostRequest() {
        Assert.assertArrayEquals(autowiredSc.getFreeURLsPostRequest(), freeURLsPostRequestBind.split(";"));
    }

    @Test
    public void getReqUsernameHolder() {
        cleanEntity();
        String reqUsernameHolder = "reqUsernameHolder";
        ReflectionTestUtils.setField(entity, "reqUsernameHolder", reqUsernameHolder);
        assertEquals(reqUsernameHolder, entity.getReqUsernameHolder());

    }

    @Test
    public void getReqPasswordHolder() {
        cleanEntity();
        String reqPasswordHolder = "reqPasswordHolder";
        ReflectionTestUtils.setField(entity, "reqPasswordHolder", reqPasswordHolder);
        assertEquals(reqPasswordHolder, entity.getReqPasswordHolder());
    }

    @Test
    public void getSecret() {
        cleanEntity();
        String secret = "secret";
        ReflectionTestUtils.setField(entity, "secret", secret);
        assertEquals(secret, entity.getSecret());
    }

    @Test
    public void getIssuedTimeHolder() {
        cleanEntity();
        String issuedTimeHolder = "issuedTimeHolder";
        ReflectionTestUtils.setField(entity, "issuedTimeHolder", issuedTimeHolder);
        assertEquals(issuedTimeHolder, entity.getIssuedTimeHolder());
    }

    @Test
    public void getAuthoritiesHolder() {
        cleanEntity();
        String authoritiesHolder = "authoritiesHolder";
        ReflectionTestUtils.setField(entity, "authoritiesHolder", authoritiesHolder);
        assertEquals(authoritiesHolder, entity.getAuthoritiesHolder());
    }

    @Test
    public void getIssuer() {
        cleanEntity();
        String issuer = "issuer";
        ReflectionTestUtils.setField(entity, "issuer", issuer);
        assertEquals(issuer, entity.getIssuer());
    }

    @Test
    public void getATokenHolder() {
        cleanEntity();
        String aTokenHolder = "aTokenHolder";
        ReflectionTestUtils.setField(entity, "aTokenHolder", aTokenHolder);
        assertEquals(aTokenHolder, entity.getATokenHolder());
    }

    @Test
    public void getATokenType() {
        cleanEntity();
        String aTokenType = "aTokenType";
        ReflectionTestUtils.setField(entity, "aTokenType", aTokenType);
        assertEquals(aTokenType, entity.getATokenType());
    }

    @Test
    public void getATokenTypeHolder() {
        cleanEntity();
        String aTokenTypeHolder = "aTokenTypeHolder";
        ReflectionTestUtils.setField(entity, "aTokenTypeHolder", aTokenTypeHolder);
        assertEquals(aTokenTypeHolder, entity.getATokenTypeHolder());
    }

    @Test
    public void getATokenHeader() {
        cleanEntity();
        String aTokenHeader = "aTokenHeader";
        ReflectionTestUtils.setField(entity, "aTokenHeader", aTokenHeader);
        assertEquals(aTokenHeader, entity.getATokenHeader());
    }

    @Test
    public void getATokenHeaderToBeSentHolder() {
        cleanEntity();
        String aTokenHeaderToBeSentHolder = "aTokenHeaderToBeSentHolder";
        ReflectionTestUtils.setField(entity, "aTokenHeaderToBeSentHolder", aTokenHeaderToBeSentHolder);
        assertEquals(aTokenHeaderToBeSentHolder, entity.getATokenHeaderToBeSentHolder());
    }

    @Test
    public void getATokenExpirationTime() {
        cleanEntity();
        long aTokenExpirationTime = 9999999;
        ReflectionTestUtils.setField(entity, "aTokenExpirationTime", aTokenExpirationTime);
        assertEquals(aTokenExpirationTime, entity.getATokenExpirationTime());
    }

    @Test
    public void getATokenExpirationTimeHolder() {
        cleanEntity();
        String aTokenExpirationTimeHolder = "aTokenExpirationTimeHolder";
        ReflectionTestUtils.setField(entity, "aTokenExpirationTimeHolder", aTokenExpirationTimeHolder);
        assertEquals(aTokenExpirationTimeHolder, entity.getATokenExpirationTimeHolder());
    }

    @Test
    public void getATokenExpiresInHolder() {
        cleanEntity();
        String aTokenExpiresInHolder = "aTokenExpiresInHolder";
        ReflectionTestUtils.setField(entity, "aTokenExpiresInHolder", aTokenExpiresInHolder);
        assertEquals(aTokenExpiresInHolder, entity.getATokenExpiresInHolder());
    }

    @Test
    public void getRTokenHolder() {
        cleanEntity();
        String rTokenHolder = "rTokenHolder";
        ReflectionTestUtils.setField(entity, "rTokenHolder", rTokenHolder);
        assertEquals(rTokenHolder, entity.getRTokenHolder());
    }

    @Test
    public void getRTokenExpirationTime() {
        cleanEntity();
        long rTokenExpirationTime = 999912;
        ReflectionTestUtils.setField(entity, "rTokenExpirationTime", rTokenExpirationTime);
        assertEquals(rTokenExpirationTime, entity.getRTokenExpirationTime());
    }

    @Test
    public void getRTokenExpirationTimeHolder() {
        cleanEntity();
        String rTokenExpirationTimeHolder = "rTokenExpirationTimeHolder";
        ReflectionTestUtils.setField(entity, "rTokenExpirationTimeHolder", rTokenExpirationTimeHolder);
        assertEquals(rTokenExpirationTimeHolder, entity.getRTokenExpirationTimeHolder());
    }

    @Test
    public void getRTokenExpiresInHolder() {
        cleanEntity();
        String rTokenExpiresInHolder = "rTokenExpiresInHolder";
        ReflectionTestUtils.setField(entity, "rTokenExpiresInHolder", rTokenExpiresInHolder);
        assertEquals(rTokenExpiresInHolder, entity.getRTokenExpiresInHolder());
    }

    @Test
    public void getSignUpUrl() {
        cleanEntity();
        String signUpUrl = "signUpUrl";
        ReflectionTestUtils.setField(entity, "signUpUrl", signUpUrl);
        assertEquals(signUpUrl, entity.getSignUpUrl());
    }

    @Test
    public void getSignInUrl() {
        cleanEntity();
        String signInUrl = "signInUrl";
        ReflectionTestUtils.setField(entity, "signInUrl", signInUrl);
        assertEquals(signInUrl, entity.getSignInUrl());
    }

    @Test
    public void getUsernameHolder() {
        cleanEntity();
        String usernameHolder = "usernameHolder";
        ReflectionTestUtils.setField(entity, "usernameHolder", usernameHolder);
        assertEquals(usernameHolder, entity.getUsernameHolder());
    }

    private void cleanEntity() {
        entity = new SecurityConst();
    }
}