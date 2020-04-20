package com.gms.appconfiguration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.appconfiguration.security.authentication.AuthenticationFacade;
import com.gms.appconfiguration.security.authentication.JWTAuthenticationFailureHandler;
import com.gms.appconfiguration.security.authentication.JWTAuthenticationFilter;
import com.gms.appconfiguration.security.authentication.entrypoint.GmsHttpStatusAndBodyAuthenticationEntryPoint;
import com.gms.appconfiguration.security.authorization.GmsAccessDeniedHandler;
import com.gms.appconfiguration.security.authorization.JWTAuthorizationFilter;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.SecurityConstant;
import com.gms.util.i18n.MessageResolver;
import com.gms.service.security.token.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class GmsSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    /**
     * URL separator.
     */
    private static final String URL_SEPARATOR = "/";
    /**
     * Instance of {@link SecurityConstant}.
     */
    private final SecurityConstant securityConstant;
    /**
     * Instance of {@link DefaultConstant}.
     */
    private final DefaultConstant defaultConstant;
    /**
     * Instance of {@link MessageResolver}.
     */
    private final MessageResolver messageResolver;
    /**
     * Instance of {@link UserDetailsService}.
     */
    private final UserDetailsService userDetailsService;
    /**
     * Bean {@link PasswordEncoder}.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Instance of {@link ObjectMapper}.
     */
    private final ObjectMapper objectMapper;
    /**
     * Instance of {@link JWTService}.
     */
    private final JWTService jwtService;
    /**
     * Instance of {@link AuthenticationFacade}.
     */
    private final AuthenticationFacade authFacade;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param http {@link HttpSecurity} instnace injected by the Spring framework.
     * @throws Exception if there is any issue while configuring the security mechanisms.
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final JWTAuthenticationFilter authenticationFilter =
                new JWTAuthenticationFilter(authenticationManager(),
                                            (UserService) userDetailsService,
                                            objectMapper,
                                            jwtService,
                                            securityConstant,
                                            defaultConstant,
                                            messageResolver);
        final JWTAuthorizationFilter authorizationFilter =
                new JWTAuthorizationFilter(authenticationManager(), securityConstant, jwtService, authFacade);

        final AuthenticationFailureHandler authenticationFailureHandler =
                new JWTAuthenticationFailureHandler(defaultConstant, securityConstant, messageResolver);

        final AuthenticationEntryPoint authenticationEntryPoint =
                new GmsHttpStatusAndBodyAuthenticationEntryPoint(UNAUTHORIZED, defaultConstant, messageResolver);

        authenticationFilter.setAllowSessionCreation(false);
        authenticationFilter.setFilterProcessesUrl(defaultConstant.getApiBasePath() + securityConstant.getSignInUrl());
        authenticationFilter.setPostOnly(true);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        http
                .cors().and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()

                .authorizeRequests()

                .antMatchers(HttpMethod.GET, getFreeGet()).permitAll()
                .antMatchers(HttpMethod.POST, getFreePost()).permitAll()
                .antMatchers(getFreeAny()).permitAll()

                // needs to be authenticated by default to access anything within the scope of the API path
                .antMatchers(defaultConstant.getApiBasePath()).authenticated()

                // needs to be authenticated by default to access anything beyond the base ("/") path
                .antMatchers(defaultConstant.getApiBasePath() + URL_SEPARATOR + "**").authenticated()

                // permit request to base url, not request to API
                .antMatchers(URL_SEPARATOR).permitAll()

                .and()
                .addFilter(authenticationFilter)
                .addFilter(authorizationFilter)

                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                // 401 instead as "unauthorized" response HttpStatus
                .exceptionHandling()
                .accessDeniedHandler(new GmsAccessDeniedHandler(defaultConstant, messageResolver))

                .authenticationEntryPoint(authenticationEntryPoint);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param auth {@link AuthenticationManagerBuilder} instance.
     * @throws Exception if there is any issue while configuring the security mechanisms.
     */
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A bean for the {@link CorsConfigurationSource} type.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(URL_SEPARATOR + "**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

    private String[] getFreePost() {
        return getFreeUrl(securityConstant.getFreeURLsPostRequest(), getAdditionalFreePostUrls());
    }

    private String[] getFreeGet() {
        return getFreeUrl(securityConstant.getFreeURLsGetRequest(), getAdditionalFreeGetUrls());
    }

    private String[] getFreeAny() {
        return getFreeUrl(securityConstant.getFreeURLsAnyRequest(), getAdditionalFreeAnyUrls());
    }

    private String[] getFreeUrl(final String[] urls, final String... additionalUrls) {

        ArrayList<String> free = new ArrayList<>(urls.length);
        for (String s : urls) {
            addUrl(free, s);
        }
        for (String s : additionalUrls) {
            addUrl(free, s);
        }

        return free.toArray(new String[0]);
    }

    private void addUrl(final Collection<? super String> urlCollection, final String url) {
        String b = defaultConstant.getApiBasePath();
        if (url != null && !"".equals(url)) {
            urlCollection.add(b + (url.startsWith(URL_SEPARATOR) ? url : URL_SEPARATOR + url));
        }
    }

    private String[] getAdditionalFreePostUrls() {
        return new String[]{
                securityConstant.getSignUpUrl(),
                SecurityConstant.ACCESS_TOKEN_URL,
                securityConstant.getSignInUrl()
        };
    }

    private String[] getAdditionalFreeGetUrls() {
        return new String[]{};
    }

    private String[] getAdditionalFreeAnyUrls() {
        return new String[]{};
    }

}
