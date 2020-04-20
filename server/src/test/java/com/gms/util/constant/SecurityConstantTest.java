package com.gms.util.constant;

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
 * @version 0.2
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityConstant.class)
public class SecurityConstantTest {

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.request.auth.username:username}")
    private String reqUsernameHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.request.auth.password:password}")
    private String reqPasswordHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.secret:OIwOG02p4f8UyfqAwEAHnKaEjpwQMyBqO9cmvp70d6P9nbuNbF6c0WQwlYBjWjb}")
    private String secretBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.issued_time_holder:issued_at}")
    private String issuedTimeHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.authorities_holder:authorities}")
    private String authoritiesHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.issuer:www.gms.com}")
    private String issuerBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_holder:access_token}")
    private String aTokenHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_type:Bearer}")
    private String aTokenTypeBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_type_holder:token_type}")
    private String aTokenTypeHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_header:Authorization}")
    private String aTokenHeaderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_header_to_be_sent_holder:header_to_be_sent}")
    private String aTokenHeaderToBeSentHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_expiration:86400}")
    private long aTokenExpirationTimeBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_expiration_time_holder:token_expiration_time}")
    private String aTokenExpirationTimeHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.token_expires_in_holder:token_expires_in}")
    private String aTokenExpiresInHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.refresh_token_holder:refresh_token}")
    private String rTokenHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.refresh_token_expiration:2592000}") // 30 days in seconds
    private long rTokenExpirationTimeBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.refresh_token_expiration_time_holder:refresh_token_expiration_time}")
    private String rTokenExpirationTimeHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.jwt.refresh_token_expires_in_holder:refresh_token_expires_in}")
    private String rTokenExpiresInHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.sign_up_url}")
    private String signUpUrlBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.sign_in_url:/login}")
    private String signInUrlBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.free_url_any:}")
    private String freeURLsAnyRequestBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.free_url_post:}")
    private String freeURLsPostRequestBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.security.free_url_get:}")
    private String freeURLsGetRequestBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.response.auth.username:username}")
    private String usernameHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Autowired
    private SecurityConstant autowiredSc;
    /**
     * Instance of {@link SecurityConstant}.
     */
    private SecurityConstant entity;

    /**
     * Semicolon ({@code ;}) separator.
     */
    private static final String SEPARATOR = "0x3B (3b)";

    /**
     * Test to be executed by JUnit.
     */
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


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getFreeURLsAnyRequest() {
        assertArrayEquals(autowiredSc.getFreeURLsAnyRequest(), freeURLsAnyRequestBind.split(SEPARATOR));
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getFreeURLsGetRequest() {
        assertArrayEquals(autowiredSc.getFreeURLsGetRequest(), freeURLsGetRequestBind.split(SEPARATOR));
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getFreeURLsPostRequest() {
        assertArrayEquals(autowiredSc.getFreeURLsPostRequest(), freeURLsPostRequestBind.split(SEPARATOR));
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getReqUsernameHolder() {
        cleanEntity();
        String reqUsernameHolder = "reqUsernameHolder";
        ReflectionTestUtils.setField(entity, "reqUsernameHolder", reqUsernameHolder);
        assertEquals(reqUsernameHolder, entity.getReqUsernameHolder());

    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getReqPasswordHolder() {
        cleanEntity();
        String reqPasswordHolder = "reqPasswordHolder";
        ReflectionTestUtils.setField(entity, "reqPasswordHolder", reqPasswordHolder);
        assertEquals(reqPasswordHolder, entity.getReqPasswordHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getSecret() {
        cleanEntity();
        String secret = "secret";
        ReflectionTestUtils.setField(entity, "secret", secret);
        assertEquals(secret, entity.getSecret());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getIssuedTimeHolder() {
        cleanEntity();
        String issuedTimeHolder = "issuedTimeHolder";
        ReflectionTestUtils.setField(entity, "issuedTimeHolder", issuedTimeHolder);
        assertEquals(issuedTimeHolder, entity.getIssuedTimeHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getAuthoritiesHolder() {
        cleanEntity();
        String authoritiesHolder = "authoritiesHolder";
        ReflectionTestUtils.setField(entity, "authoritiesHolder", authoritiesHolder);
        assertEquals(authoritiesHolder, entity.getAuthoritiesHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getIssuer() {
        cleanEntity();
        String issuer = "issuer";
        ReflectionTestUtils.setField(entity, "issuer", issuer);
        assertEquals(issuer, entity.getIssuer());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenHolder() {
        cleanEntity();
        String aTokenHolder = "aTokenHolder";
        ReflectionTestUtils.setField(entity, "aTokenHolder", aTokenHolder);
        assertEquals(aTokenHolder, entity.getATokenHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenType() {
        cleanEntity();
        String aTokenType = "aTokenType";
        ReflectionTestUtils.setField(entity, "aTokenType", aTokenType);
        assertEquals(aTokenType, entity.getATokenType());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenTypeHolder() {
        cleanEntity();
        String aTokenTypeHolder = "aTokenTypeHolder";
        ReflectionTestUtils.setField(entity, "aTokenTypeHolder", aTokenTypeHolder);
        assertEquals(aTokenTypeHolder, entity.getATokenTypeHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenHeader() {
        cleanEntity();
        String aTokenHeader = "aTokenHeader";
        ReflectionTestUtils.setField(entity, "aTokenHeader", aTokenHeader);
        assertEquals(aTokenHeader, entity.getATokenHeader());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenHeaderToBeSentHolder() {
        cleanEntity();
        String aTokenHeaderToBeSentHolder = "aTokenHeaderToBeSentHolder";
        ReflectionTestUtils.setField(entity, "aTokenHeaderToBeSentHolder", aTokenHeaderToBeSentHolder);
        assertEquals(aTokenHeaderToBeSentHolder, entity.getATokenHeaderToBeSentHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenExpirationTime() {
        cleanEntity();
        final long aTokenExpirationTime = 9999999;
        ReflectionTestUtils.setField(entity, "aTokenExpirationTime", aTokenExpirationTime);
        assertEquals(aTokenExpirationTime, entity.getATokenExpirationTime());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenExpirationTimeHolder() {
        cleanEntity();
        String aTokenExpirationTimeHolder = "aTokenExpirationTimeHolder";
        ReflectionTestUtils.setField(entity, "aTokenExpirationTimeHolder", aTokenExpirationTimeHolder);
        assertEquals(aTokenExpirationTimeHolder, entity.getATokenExpirationTimeHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenExpiresInHolder() {
        cleanEntity();
        String aTokenExpiresInHolder = "aTokenExpiresInHolder";
        ReflectionTestUtils.setField(entity, "aTokenExpiresInHolder", aTokenExpiresInHolder);
        assertEquals(aTokenExpiresInHolder, entity.getATokenExpiresInHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenHolder() {
        cleanEntity();
        String rTokenHolder = "rTokenHolder";
        ReflectionTestUtils.setField(entity, "rTokenHolder", rTokenHolder);
        assertEquals(rTokenHolder, entity.getRTokenHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenExpirationTime() {
        cleanEntity();
        final long rTokenExpirationTime = 999912;
        ReflectionTestUtils.setField(entity, "rTokenExpirationTime", rTokenExpirationTime);
        assertEquals(rTokenExpirationTime, entity.getRTokenExpirationTime());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenExpirationTimeHolder() {
        cleanEntity();
        String rTokenExpirationTimeHolder = "rTokenExpirationTimeHolder";
        ReflectionTestUtils.setField(entity, "rTokenExpirationTimeHolder", rTokenExpirationTimeHolder);
        assertEquals(rTokenExpirationTimeHolder, entity.getRTokenExpirationTimeHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenExpiresInHolder() {
        cleanEntity();
        String rTokenExpiresInHolder = "rTokenExpiresInHolder";
        ReflectionTestUtils.setField(entity, "rTokenExpiresInHolder", rTokenExpiresInHolder);
        assertEquals(rTokenExpiresInHolder, entity.getRTokenExpiresInHolder());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getSignUpUrl() {
        cleanEntity();
        String signUpUrl = "signUpUrl";
        ReflectionTestUtils.setField(entity, "signUpUrl", signUpUrl);
        assertEquals(signUpUrl, entity.getSignUpUrl());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getSignInUrl() {
        cleanEntity();
        String signInUrl = "signInUrl";
        ReflectionTestUtils.setField(entity, "signInUrl", signInUrl);
        assertEquals(signInUrl, entity.getSignInUrl());
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUsernameHolder() {
        cleanEntity();
        String usernameHolder = "usernameHolder";
        ReflectionTestUtils.setField(entity, "usernameHolder", usernameHolder);
        assertEquals(usernameHolder, entity.getUsernameHolder());
    }

    private void cleanEntity() {
        entity = new SecurityConstant();
    }

}
