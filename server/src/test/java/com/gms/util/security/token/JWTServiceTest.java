package com.gms.util.security.token;

import com.gms.Application;
import com.gms.util.constant.SecurityConst;
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
     * Instance of {@link SecurityConst}.
     */
    @Autowired
    private SecurityConst sc;

    /**
     * Instance of {@link JWTService}.
     */
    private JWTService jwtService;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        jwtService = new JWTService(sc);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getATokenExpirationTime() {
        final Object aTokenExpirationTime = ReflectionTestUtils.getField(sc, "aTokenExpirationTime");
        long prev = aTokenExpirationTime != null ? Long.parseLong(aTokenExpirationTime.toString()) : -1;
        final long expTime = -10;
        ReflectionTestUtils.setField(sc, "aTokenExpirationTime", expTime);
        assertTrue("Expiration time for access token must be greater than zero",
                jwtService.getATokenExpirationTime() > 0);
        ReflectionTestUtils.setField(sc, "aTokenExpirationTime", prev);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRTokenExpirationTime() {
        long prev = Long.parseLong(String.valueOf(ReflectionTestUtils.getField(sc, "rTokenExpirationTime")));
        final long expTime = -10;
        ReflectionTestUtils.setField(sc, "rTokenExpirationTime", expTime);
        assertTrue("Expiration time for refresh token must be greater than zero",
                jwtService.getRTokenExpirationTime() > 0);
        // restore value
        ReflectionTestUtils.setField(sc, "rTokenExpirationTime", prev);
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
        assertTrue(message, tokenClaims.get(JWTService.EXPIRATION) instanceof Date);
        assertTrue(message, rTokenClaims.get(JWTService.EXPIRATION) instanceof Date);

        if (tokenClaims.get(JWTService.EXPIRATION) instanceof Date
                && rTokenClaims.get(JWTService.EXPIRATION) instanceof Date) {
            long rTokenTime = ((Date) rTokenClaims.get(JWTService.EXPIRATION)).getTime();
            long tokenTime = ((Date) tokenClaims.get(JWTService.EXPIRATION)).getTime();

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
                jwtService.getClaims(token, JWTService.SUBJECT, sc.getAuthoritiesHolder(), JWTService.EXPIRATION);

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
        final Map<String, Object> claims = jwtService.getClaimsExtended(token, randomKey, sc.getAuthoritiesHolder());

        assertClaimsState(claims, sub, auth);

        assertNull("Claims contains an incorrect pair key-value", claims.get(randomKey));
        assertNotNull("Expiration time is null", claims.get(JWTService.EXPIRATION));
        assertTrue("Expiration time is not an instance of Date",
                claims.get(JWTService.EXPIRATION) instanceof Date);

        if (claims.get(JWTService.EXPIRATION) != null && claims.get(JWTService.EXPIRATION) instanceof Date) {
            assertTrue("Expiration time is not greater than the creation time of the token",
                    ((Date) claims.get(JWTService.EXPIRATION)).getTime() > expiresIn);
        }
    }

    private void assertClaimsState(final Map<String, Object> claims, final String subject, final String authorities) {
        assertFalse("Claims map is empty", claims.isEmpty());

        assertNotNull("Claims map is null", claims);
        assertNotNull("Expiration time claims is null", claims.get(JWTService.EXPIRATION));
        assertNotNull("Subject claims is null", claims.get(JWTService.SUBJECT));
        assertNotNull("Authorities claims is null", claims.get(sc.getAuthoritiesHolder()));

        if (claims.get(JWTService.SUBJECT) != null) {
            assertEquals("Subjects are not equals", subject, claims.get(JWTService.SUBJECT).toString());
        }
        if (claims.get(sc.getAuthoritiesHolder()) != null) {
            assertEquals(
                    "Authorities are not equals",
                    authorities,
                    claims.get(sc.getAuthoritiesHolder()).toString()
            );
        }
    }

}
