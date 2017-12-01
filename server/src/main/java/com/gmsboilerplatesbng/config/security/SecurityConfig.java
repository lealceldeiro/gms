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

    private final SecurityConst sc;
    private final DefaultConst c;

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final IAuthenticationFacade authFacade;

    public SecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                          IAuthenticationFacade authenticationFacade, SecurityConst sc, DefaultConst c) {
        this.userDetailsService = userService;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.authFacade = authenticationFacade;
        this.sc = sc;
        this.c = c;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String b = this.c.API_BASE_PATH;
        String[] freePost = {
                b + this.sc.SIGN_IN_URL,
                b + this.sc.SIGN_UP_URL
        };
        String[] freeGet = {
                this.c.API_DOC_PATH
        };
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, freeGet).permitAll()
                .antMatchers(HttpMethod.POST, freePost).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl(this.sc.SIGN_OUT_URL)
                .and()
                .formLogin()
                .loginPage(this.sc.SIGN_IN_URL)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), (UserService) this.userDetailsService, this.sc))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), (UserService)this.userDetailsService,
                        this.authFacade, this.sc))
                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 401 instead of 403
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint(this.sc.HEADER));
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
