package com.gms.service.security.token;

import com.gms.Application;
import com.gms.util.constant.SecurityConstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JWTServiceTest {

    /**
     * Instance of {@link SecurityConstant}.
     */
    @Autowired
    private SecurityConstant securityConstant;

    /**
     * Instance of {@link JWTService}.
     */
    private JWTService jwtService;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        jwtService = new JWTServiceImpl(securityConstant);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenExpirationTime() {
        final Object aTokenExpirationTime = ReflectionTestUtils.getField(securityConstant, "aTokenExpirationTime");
        long prev = aTokenExpirationTime != null ? Long.parseLong(aTokenExpirationTime.toString()) : -1;
        final long expTime = -10;
        ReflectionTestUtils.setField(securityConstant, "aTokenExpirationTime", expTime);
        assertTrue("Expiration time for access token must be greater than zero",
                   jwtService.getATokenExpirationTime() > 0);
        ReflectionTestUtils.setField(securityConstant, "aTokenExpirationTime", prev);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenExpirationTime() {
        final Object awValue = ReflectionTestUtils.getField(securityConstant, "rTokenExpirationTime");
        final long prev = Long.parseLong(String.valueOf(awValue));
        final long expTime = -10;
        ReflectionTestUtils.setField(securityConstant, "rTokenExpirationTime", expTime);
        assertTrue("Expiration time for refresh token must be greater than zero",
                   jwtService.getRTokenExpirationTime() > 0);
        // restore value
        ReflectionTestUtils.setField(securityConstant, "rTokenExpirationTime", prev);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createTwoTokensWithSubjectAreNotEqual() {
        final String sub = "TestSubjectBN";
        final String token1 = jwtService.createToken(sub);
        final String token2 = jwtService.createToken(sub);
        assertNotEquals("The JWTService#createToken(String subject) method never should create two tokens "
                                + "which are equals", token1, token2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createTwoTokensWithSubjectAndExpAreNotEqual() {
        final String sub = "TestSubjectCV";
        final long exp = 123;
        final String token1 = jwtService.createToken(sub, exp);
        final String token2 = jwtService.createToken(sub, exp);
        assertNotEquals("The JWTService#createToken(String subject, long expiresIn) method never should "
                                + "create two tokens which are equals", token1, token2);

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createTwoTokensWithSubjectAndAuthAreNotEqual() {
        final String sub = "TestSubjectRF";
        final String auth = "ROLE_WS;ROLE_RF";
        final String token1 = jwtService.createToken(sub, auth);
        final String token2 = jwtService.createToken(sub, auth);
        assertNotEquals("The JWTService#createToken(String subject, String authorities) method never should "
                                + "create two tokens which are equals", token1, token2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createTwoTokensWithSubjectAuthAndExpAreNotEqual() {
        final String sub = "TestSubjectBN";
        final String auth = "ROLE_GT;ROLE_DE";
        final long exp = 123;
        final String token1 = jwtService.createToken(sub, auth, exp);
        final String token2 = jwtService.createToken(sub, auth, exp);
        assertNotEquals("The JWTService#createToken(String subject, String authorities, long expiresIn) "
                                + "method never should create two tokens which are equals", token1, token2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createTwoRefreshTokensWithSubjectAndAuthAreNotEqual() {
        final String sub = "TestSubjectG";
        final String auth = "ROLE_N;ROLE_C";
        final String token1 = jwtService.createRefreshToken(sub, auth);
        final String token2 = jwtService.createRefreshToken(sub, auth);
        assertNotEquals("The JWTService#createRefreshToken(String subject, String authorities) method never "
                                + "should create two tokens which are equals", token1, token2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void expTimeOfRefreshTokenGreaterThanExpAccessToken() {
        final String sub = "TestSubject1";
        final String auth = "ROLE_1;ROLE_2";
        final String accessToken = jwtService.createToken(sub, auth);
        final String refreshToken = jwtService.createRefreshToken(sub, auth);
        final String message = "Expiration time is not an instance of Date";

        Map<String, Object> tokenClaims = jwtService.getClaimsExtended(accessToken);
        Map<String, Object> rTokenClaims = jwtService.getClaimsExtended(refreshToken);
        assertTrue(message, tokenClaims.get(jwtService.expirationKey()) instanceof Date);
        assertTrue(message, rTokenClaims.get(jwtService.expirationKey()) instanceof Date);

        if (tokenClaims.get(jwtService.expirationKey()) instanceof Date
                && rTokenClaims.get(jwtService.expirationKey()) instanceof Date) {
            long rTokenTime = ((Date) rTokenClaims.get(jwtService.expirationKey())).getTime();
            long tokenTime = ((Date) tokenClaims.get(jwtService.expirationKey())).getTime();

            assertTrue("Expiration time for refresh token must be greater than expiration time for access "
                               + "token when default values are used", rTokenTime > tokenTime);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getClaims() {
        final String sub = "TestSubjectX";
        final String auth = "ROLE_1;ROLE_2";
        final String token = jwtService.createToken(sub, auth);
        final Map<String, Object> claims =
                jwtService.getClaims(token, jwtService.subjectKey(), securityConstant.getAuthoritiesHolder(),
                                     jwtService.expirationKey());

        assertClaimsState(claims, sub, auth);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getClaimsExtended() {
        final String sub = "TestSubjectY";
        final String auth = "ROLE_X;ROLE_Y";
        final long expiresIn = 99999999;
        final String token = jwtService.createToken(sub, auth, expiresIn);
        final String randomKey = "randomKey";
        final Map<String, Object> claims = jwtService.getClaimsExtended(token,
                                                                        randomKey,
                                                                        securityConstant.getAuthoritiesHolder());

        assertClaimsState(claims, sub, auth);

        assertNull("Claims contains an incorrect pair key-value", claims.get(randomKey));
        assertNotNull("Expiration time is null", claims.get(jwtService.expirationKey()));
        assertTrue("Expiration time is not an instance of Date",
                   claims.get(jwtService.expirationKey()) instanceof Date);

        if (claims.get(jwtService.expirationKey()) != null && claims.get(jwtService.expirationKey()) instanceof Date) {
            assertTrue("Expiration time is not greater than the creation time of the token",
                       ((Date) claims.get(jwtService.expirationKey())).getTime() > expiresIn);
        }
    }

    private void assertClaimsState(final Map<String, Object> claims, final String subject, final String authorities) {
        assertFalse("Claims map is empty", claims.isEmpty());

        assertNotNull("Claims map is null", claims);
        assertNotNull("Expiration time claims is null", claims.get(jwtService.expirationKey()));
        assertNotNull("Subject claims is null", claims.get(jwtService.subjectKey()));
        assertNotNull("Authorities claims is null", claims.get(securityConstant.getAuthoritiesHolder()));

        if (claims.get(jwtService.subjectKey()) != null) {
            assertEquals("Subjects are not equals", subject, claims.get(jwtService.subjectKey()).toString());
        }
        if (claims.get(securityConstant.getAuthoritiesHolder()) != null) {
            assertEquals(
                    "Authorities are not equals",
                    authorities,
                    claims.get(securityConstant.getAuthoritiesHolder()).toString()
            );
        }
    }

}
