package com.gms.config.locale;

import com.gms.util.constant.DefaultConst;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
@RequiredArgsConstructor
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;

    /**
     * Strategy interface for resolving messages, with support for the parameterization and internationalization of
     * such messages.
     */
    private final MessageSource messageSource;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A {@link MessageResolver} for some {@code messageSource}.
     */
    @Bean
    public MessageResolver gmsMessageResolver() {
        return new MessageResolver(messageSource);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A {@link LocaleResolver}.
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new GmsCLocaleResolver();
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A {@link LocaleChangeInterceptor}.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName(dc.getLanguageHolder());

        return lci;
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param registry An {@link InterceptorRegistry} to which some custom interceptor will be added.
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
