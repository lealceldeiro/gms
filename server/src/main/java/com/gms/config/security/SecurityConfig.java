package com.gms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.component.security.authentication.AuthenticationFacade;
import com.gms.component.security.token.JWTService;
import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * URL separator.
     */
    private static final String URL_SEPARATOR = "/";
    /**
     * Instance of {@link SecurityConst}.
     */
    private final SecurityConst sc;
    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * Instance of {@link MessageResolver}.
     */
    private final MessageResolver msg;
    /**
     * Instance of {@link UserDetailsService}.
     */
    private final UserDetailsService userDetailsService;
    /**
     * Bean {@link BCryptPasswordEncoder}.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * Instance of {@link ObjectMapper}.
     */
    private final ObjectMapper oMapper;
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
        final JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(
                authenticationManager(), (UserService) userDetailsService, oMapper, jwtService, sc, dc, msg
        );
        authFilter.setAllowSessionCreation(false);
        authFilter.setFilterProcessesUrl(dc.getApiBasePath() + sc.getSignInUrl());
        authFilter.setPostOnly(true);
        authFilter.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler(dc, sc, msg));
        http
                .cors().and().formLogin().disable().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, getFreeGet()).permitAll()
                .antMatchers(HttpMethod.POST, getFreePost()).permitAll()
                .antMatchers(getFreeAny()).permitAll()
                // needs to be authenticated by default to access anything within the scope of the API path
                .antMatchers(dc.getApiBasePath()).authenticated()
                // needs to be authenticated by default to access anything beyond the base ("/") path
                .antMatchers(dc.getApiBasePath() + URL_SEPARATOR + "**").authenticated()
                // permit request to base url, not request to API
                .antMatchers(URL_SEPARATOR).permitAll()
                .and()
                .addFilter(authFilter)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), sc, jwtService, authFacade))
                // disable session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 401 instead as "unauthorized" response HttpStatus
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
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
        return getFreeUrl(sc.getFreeURLsPostRequest(), getAdditionalFreePostUrls());
    }

    private String[] getFreeGet() {
        return getFreeUrl(sc.getFreeURLsGetRequest(), getAdditionalFreeGetUrls());
    }

    private String[] getFreeAny() {
        return getFreeUrl(sc.getFreeURLsAnyRequest(), getAdditionalFreeAnyUrls());
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
        String b = dc.getApiBasePath();
        if (url != null && !"".equals(url)) {
            urlCollection.add(b + (url.startsWith(URL_SEPARATOR) ? url : URL_SEPARATOR + url));
        }
    }

    private String[] getAdditionalFreePostUrls() {
        return new String[]{
                sc.getSignUpUrl(),
                SecurityConst.ACCESS_TOKEN_URL,
                sc.getSignInUrl()
                //every time this list is updated, so it must be updated the method getListOfParametersForFreePostUrl
        };
    }

    /**
     * For JUnit Tests purposes only!
     *
     * @return Array of {@link Map}. Each Map contains the required parameters for executing the post request.
     */
    @SuppressWarnings("unused")
    private Map<String, String>[] getListOfParametersForFreePostUrl() {
        final int numberOfFixedFreeUrl = 3;
        @SuppressWarnings("unchecked") final Map<String, String>[] r = new HashMap[numberOfFixedFreeUrl];
        Field[] fields;

        // region sign-up
        r[0] = new HashMap<>();
        final Class<EUser> eUserClass = EUser.class;
        fields = eUserClass.getDeclaredFields();
        for (Field f : fields) {
            r[0].put(f.getName(), "1");
        }
        // endregion

        // region access token
        r[1] = new HashMap<>();
        final Class<RefreshTokenPayload> rTPayloadClass = RefreshTokenPayload.class;
        fields = rTPayloadClass.getDeclaredFields();
        for (Field f : fields) {
            if ("refreshToken".equals(f.getName())) {
                r[1].put(f.getName(), jwtService.createRefreshToken("sub", "auth"));
            } else {
                r[1].put(f.getName(), "1");
            }
        }
        // endregion

        // region sign-in
        r[2] = new HashMap<>();
        r[2].put(sc.getReqUsernameHolder(), dc.getUserAdminDefaultUsername());
        r[2].put(sc.getReqPasswordHolder(), dc.getUserAdminDefaultPassword());
        // endregion

        return r;
    }

    private String[] getAdditionalFreeGetUrls() {
        return new String[]{};
    }

    private String[] getAdditionalFreeAnyUrls() {
        return new String[]{};
    }

}
