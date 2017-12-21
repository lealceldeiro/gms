package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.component.security.authentication.IAuthenticationFacade;
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

    private final IAuthenticationFacade authFacade;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, getFreeGet()).permitAll()
                .antMatchers(HttpMethod.POST, getFreePost()).permitAll()
                .antMatchers(getFreeAny()).permitAll()
                .antMatchers(dc.getApiBasePath()).authenticated()
                .antMatchers(dc.getApiBasePath() + "/**").authenticated()
                .antMatchers("/").permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), (UserService) userDetailsService, oMapper, jwtService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), sc, jwtService, authFacade))
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
        return getFreeUrl(sc.getFreeURLsPostRequest(), sc.getSignUpUrl());
    }

    private String[] getFreeGet() {
        return getFreeUrl(sc.getFreeURLsGetRequest());
    }

    private String[] getFreeAny() {
        return getFreeUrl(sc.getFreeURLsAnyRequest(), sc.getSignOutUrl());
    }

    private String[] getFreeUrl(String[] urls, String ... additionalUrls) {
        String b = dc.getApiBasePath();
        ArrayList<String> free = new ArrayList<>(urls.length);
        for (String s: urls) {
            if (s != null && !s.equals("")) {
                free.add(b + (s.startsWith("/") ? s : "/" + s));
            }
        }
        for (String s: additionalUrls) {
            if (s != null && !s.equals("")) {
                free.add(b + (s.startsWith("/") ? s : "/" + s));
            }
        }

        return free.toArray(new String[free.size()]);
    }

}
