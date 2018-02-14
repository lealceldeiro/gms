package com.gms.component.security.token;

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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JWTServiceTest {

    @Autowired private SecurityConst sc;

    private JWTService jwtService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(sc, "aTokenExpirationTime", -10);
        ReflectionTestUtils.setField(sc, "rTokenExpirationTime", -10);

        jwtService = new JWTService(sc);
    }

    @Test
    public void getATokenExpirationTime() {
        assert jwtService.getATokenExpirationTime() > 0 : "Expiration time for access token must be greater than zero";
    }

    @Test
    public void getRTokenExpirationTime() {
        assert jwtService.getRTokenExpirationTime() > 0 : "Expiration time for refresh token must be greater than zero";
    }

    @Test
    public void createTwoTokensWithSubjectAreNotEqual() {
        final String sub = "TestSubject";
        final String token1 = jwtService.createToken(sub);
        final String token2 = jwtService.createToken(sub);
        assertNotEquals("The JWTService#createToken(String subject) method never should create two tokens which are equals",
                token1, token2);
    }

    @Test
    public void createTwoTokensWithSubjectAndExpAreNotEqual() {
        final String sub = "TestSubject";
        long exp = 123;
        final String token1 = jwtService.createToken(sub, exp);
        final String token2 = jwtService.createToken(sub, exp);
        assertNotEquals("The JWTService#createToken(String subject, long expiresIn) method never should create two tokens which are equals",
                token1, token2);

    }

    @Test
    public void createTwoTokensWithSubjectAndAuthAreNotEqual() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        final String token1 = jwtService.createToken(sub, auth);
        final String token2 = jwtService.createToken(sub, auth);
        assertNotEquals("The JWTService#createToken(String subject, String authorities) method never should create two tokens which are equals",
                token1, token2);
    }

    @Test
    public void createTwoTokensWithSubjectAuthAndExpAreNotEqual() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        long exp = 123;
        final String token1 = jwtService.createToken(sub, auth, exp);
        final String token2 = jwtService.createToken(sub, auth, exp);
        assertNotEquals("The JWTService#createToken(String subject, String authorities, long expiresIn) method never should create two tokens which are equals",
                token1, token2);
    }

    @Test
    public void createTwoRefreshTokensWithSubjectAndAuthAreNotEqual() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        final String token1 = jwtService.createRefreshToken(sub, auth);
        final String token2 = jwtService.createRefreshToken(sub, auth);
        assertNotEquals("The JWTService#createRefreshToken(String subject, String authorities) method never should create two tokens which are equals",
                token1, token2);
    }

    @Test
    public void expTimeOfRefreshTokenGreaterThanExpAccessToken() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        final String accessToken = jwtService.createToken(sub, auth);
        final String refreshToken = jwtService.createRefreshToken(sub, auth);

        Map tokenClaims = jwtService.getClaimsExtended(accessToken);
        Map rTokenClaims = jwtService.getClaimsExtended(refreshToken);
        assertTrue("Expiration time is not an instance of Date", tokenClaims.get(JWTService.EXPIRATION) instanceof Date);
        assertTrue("Expiration time is not an instance of Date", rTokenClaims.get(JWTService.EXPIRATION) instanceof Date);

        if (tokenClaims.get(JWTService.EXPIRATION) instanceof Date && rTokenClaims.get(JWTService.EXPIRATION) instanceof Date) {
            assertTrue("Expiration time for refresh token must be greater than expiration time for access token when default values are used",
                    ((Date)rTokenClaims.get(JWTService.EXPIRATION)).getTime() > ((Date)tokenClaims.get(JWTService.EXPIRATION)).getTime());
        }
    }

    @Test
    public void getClaims() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        final String token = jwtService.createToken(sub, auth);
        final Map claims = jwtService.getClaims(token, JWTService.SUBJECT, sc.getAuthoritiesHolder(), JWTService.EXPIRATION);

        assertClaimsState(claims, sub, auth);
    }

    @Test
    public void getClaimsExtended() {
        final String sub = "TestSubject";
        final String auth = "ROLE_1;ROLE_2";
        final long expiresIn = 99999999;
        final String token = jwtService.createToken(sub, auth, expiresIn);
        final String randomKey = "randomKey";
        final Map claims = jwtService.getClaimsExtended(token, randomKey, sc.getAuthoritiesHolder());

        assertClaimsState(claims, sub, auth);

        assertNull("Claims contains an incorrect pair key-value", claims.get(randomKey));
        assertNotNull("Expiration time is null", claims.get(JWTService.EXPIRATION));
        assertTrue("Expiration time is not an instance of Date", claims.get(JWTService.EXPIRATION) instanceof Date);
        if (claims.get(JWTService.EXPIRATION) != null && claims.get(JWTService.EXPIRATION) instanceof Date) {
            assertTrue("Expiration time is not greater than the creation time of the token",
                    ((Date)claims.get(JWTService.EXPIRATION)).getTime() > expiresIn);
        }
    }

    private void assertClaimsState(Map claims, String subject, String authorities) {
        assertTrue("Claims map is empty", !claims.isEmpty());

        assertNotNull("Claims map is null", claims);
        assertNotNull("Expiration time claims is null", claims.get(JWTService.EXPIRATION));
        assertNotNull("Subject claims is null", claims.get(JWTService.SUBJECT));
        assertNotNull("Authorities claims is null", claims.get(sc.getAuthoritiesHolder()));

        if (claims.get(JWTService.SUBJECT) != null) {
            assertEquals("Subjects are not equals", subject, claims.get(JWTService.SUBJECT).toString());
        }
        if (claims.get(sc.getAuthoritiesHolder()) != null) {
            assertEquals("Authorities are not equals", authorities, claims.get(sc.getAuthoritiesHolder()).toString());
        }
    }
}