package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.component.security.token.JWTService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.constant.SecurityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;

/**
 * SecurityConfig
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityConst sc;
    private final DefaultConst dc;

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ObjectMapper oMapper;

    private final JWTService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, getFreeGet()).permitAll()
                .antMatchers(HttpMethod.POST, getFreePost()).permitAll()
                .antMatchers(getFreeAny()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), (UserService) userDetailsService, oMapper, jwtService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), sc, jwtService))
                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 401 instead of 403
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint(sc.getATokenHeader()));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    private String[] getFreePost() {
        String b = dc.getApiBasePath();
        String [] freePostSC = sc.getFreeURLsPostRequest();

        ArrayList<String> freePost = new ArrayList<>(freePostSC.length + 2);
        freePost.add(b + sc.getSignInUrl());
        freePost.add(b + sc.getSignUpUrl());

        for (String s: freePostSC) {
            if (s != null && !s.equals("")) {
                freePost.add(s.startsWith("/") ? s.substring(1) : b + "/" + s);
            }
        }

        return freePost.toArray(new String[freePost.size()]);
    }

    private String[] getFreeGet() {
        String b = dc.getApiBasePath();
        String [] freeGetSC = sc.getFreeURLsGetRequest();

        ArrayList<String> freeGet = new ArrayList<>(freeGetSC.length + 1);
        freeGet.add(dc.getApiDocPath());

        for (String s: freeGetSC) {
            if (s != null && !s.equals("")) {
                freeGet.add(s.startsWith("/") ? s.substring(1) : b + "/" + s);
            }
        }

        return freeGet.toArray(new String[freeGet.size()]);
    }

    private String[] getFreeAny() {
        String b = dc.getApiBasePath();
        String [] freeAnySC = sc.getFreeURLsAnyRequest();

        ArrayList<String> freeAny = new ArrayList<>(freeAnySC.length + 1);
        freeAny.add(b + sc.getSignOutUrl());

        for (String s: freeAnySC) {
            if (s != null && !s.equals("")) {
                freeAny.add(s.startsWith("/") ? s.substring(1) : b + "/" + s);
            }
        }

        return freeAny.toArray(new String[freeAny.size()]);
    }

}
