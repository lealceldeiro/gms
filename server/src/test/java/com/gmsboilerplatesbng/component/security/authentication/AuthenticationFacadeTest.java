package com.gmsboilerplatesbng.component.security.authentication;

import com.gmsboilerplatesbng.Application;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuthenticationFacadeTest {

    @Autowired IAuthenticationFacade authFacade;

    private final ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
    private UsernamePasswordAuthenticationToken auth;

    @Before
    public void setUp() {
        authorities.add(new SimpleGrantedAuthority("a1"));
        authorities.add(new SimpleGrantedAuthority("a2"));
        auth = new UsernamePasswordAuthenticationToken("principalTest", "credentialsTest", authorities);
    }

    @Test
    public void getAuthentication() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(auth);
        final Authentication realAuthInfo = SecurityContextHolder.getContext().getAuthentication();
        final Authentication facadeAuthInfo = authFacade.getAuthentication();

        assertEquals("(getAuthentication)The \"principal\" set in the authentication in the facade context does not match the \"principal\" set in the authentication in the real context",
                realAuthInfo.getPrincipal(), facadeAuthInfo.getPrincipal());
        assertEquals("(getAuthentication)The \"authorities\" set in the authentication in the facade context does not match the \"authorities\" set in the authentication in the real context",
                realAuthInfo.getAuthorities(), facadeAuthInfo.getAuthorities());
        assertEquals("(getAuthentication)The \"credentials\" set in the authentication in the facade context does not match the \"credentials\" set in the authentication in the real context",
                realAuthInfo.getCredentials(), facadeAuthInfo.getCredentials());
    }

    @Test
    public void setAuthentication() {
        SecurityContextHolder.clearContext();
        authFacade.setAuthentication(auth);
        final Authentication realAuthInfo = SecurityContextHolder.getContext().getAuthentication();
        final Authentication facadeAuthInfo = authFacade.getAuthentication();

        assertEquals("(setAuthentication)The \"principal\" set in the authentication in the facade context does not match the \"principal\" set in the authentication in the real context",
                realAuthInfo.getPrincipal(), facadeAuthInfo.getPrincipal());
        assertEquals("(setAuthentication)The \"authorities\" set in the authentication in the facade context does not match the \"authorities\" set in the authentication in the real context",
                realAuthInfo.getAuthorities(), facadeAuthInfo.getAuthorities());
        assertEquals("(setAuthentication)The \"credentials\" set in the authentication in the facade context does not match the \"credentials\" set in the authentication in the real context",
                realAuthInfo.getCredentials(), facadeAuthInfo.getCredentials());
    }
}