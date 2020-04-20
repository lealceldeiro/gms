package com.gms.component.security.authentication;

import com.gms.Application;
import com.gms.appconfiguration.security.authentication.AuthenticationFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuthenticationFacadeImplTest {

    /**
     * Instance of {@link AuthenticationFacade}.
     */
    @Autowired
    private AuthenticationFacade authFacade;

    /**
     * {@link List} of {@link SimpleGrantedAuthority}.
     */
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    /**
     * Instance of {@link UsernamePasswordAuthenticationToken}.
     */
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        authorities.add(new SimpleGrantedAuthority("a1"));
        authorities.add(new SimpleGrantedAuthority("a2"));
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("principalTest",
                                                                                      "credentialsTest",
                                                                                      authorities);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getAuthentication() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        final Authentication realAuthInfo = SecurityContextHolder.getContext().getAuthentication();
        final Authentication facadeAuthInfo = authFacade.getAuthentication();

        assertEquals("(getAuthentication)The \"principal\" set in the authentication in the facade context "
                             + "does not match the \"principal\" set in the authentication in the real context",
                     realAuthInfo.getPrincipal(), facadeAuthInfo.getPrincipal());
        assertEquals("(getAuthentication)The \"authorities\" set in the authentication in the facade context"
                             + " does not match the \"authorities\" set in the authentication in the real context",
                     realAuthInfo.getAuthorities(), facadeAuthInfo.getAuthorities());
        assertEquals("(getAuthentication)The \"credentials\" set in the authentication in the facade context"
                             + " does not match the \"credentials\" set in the authentication in the real context",
                     realAuthInfo.getCredentials(), facadeAuthInfo.getCredentials());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setAuthentication() {
        SecurityContextHolder.clearContext();
        authFacade.setAuthentication(usernamePasswordAuthenticationToken);
        final Authentication realAuthInfo = SecurityContextHolder.getContext().getAuthentication();
        final Authentication facadeAuthInfo = authFacade.getAuthentication();

        assertEquals("(setAuthentication)The \"principal\" set in the authentication in the facade context "
                             + "does not match the \"principal\" set in the authentication in the real context",
                     realAuthInfo.getPrincipal(), facadeAuthInfo.getPrincipal());
        assertEquals("(setAuthentication)The \"authorities\" set in the authentication in the facade context "
                             + "does not match the \"authorities\" set in the authentication in the real context",
                     realAuthInfo.getAuthorities(), facadeAuthInfo.getAuthorities());
        assertEquals("(setAuthentication)The \"credentials\" set in the authentication in the facade context "
                             + "does not match the \"credentials\" set in the authentication in the real context",
                     realAuthInfo.getCredentials(), facadeAuthInfo.getCredentials());
    }

}
