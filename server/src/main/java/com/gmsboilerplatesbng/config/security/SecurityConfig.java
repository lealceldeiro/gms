package com.gmsboilerplatesbng.config.security;

import com.gmsboilerplatesbng.component.security.IAuthenticationFacade;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.request.security.SecurityConst;
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

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final IAuthenticationFacade authFacade;

    public SecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                          IAuthenticationFacade authenticationFacade) {
        this.userDetailsService = userService;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.authFacade = authenticationFacade;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, DefaultConst.API_DOC_PATH).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), (UserService) this.userDetailsService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), (UserService)this.userDetailsService, this.authFacade))
                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint(SecurityConst.HEADER));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
