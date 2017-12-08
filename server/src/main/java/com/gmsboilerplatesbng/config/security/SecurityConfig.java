package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.component.security.IAuthenticationFacade;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.request.security.SecurityConst;
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

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityConst sc;
    private final DefaultConst dc;

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final IAuthenticationFacade authFacade;

    private final ObjectMapper oMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String b = dc.API_BASE_PATH;
        String[] freePost = {
                b + sc.SIGN_IN_URL,
                b + sc.SIGN_UP_URL
        };
        String[] freeGet = {
                dc.API_DOC_PATH
        };
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, freeGet).permitAll()
                .antMatchers(HttpMethod.POST, freePost).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl(sc.SIGN_OUT_URL)
                .and()
                .formLogin()
                .loginPage(sc.SIGN_IN_URL)
                .and()
                .addFilter(new JWTAuthenticationFilter(sc, authenticationManager(), (UserService) userDetailsService, oMapper))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), sc))
                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 401 instead of 403
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint(sc.HEADER));
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

}
