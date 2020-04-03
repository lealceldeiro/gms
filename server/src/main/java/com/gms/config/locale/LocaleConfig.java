package com.gms.config.locale;

import com.gms.util.constant.DefaultConst;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * File separator.
     */
    private static final String SEP = File.pathSeparator;
    /**
     * Classpath for the resources.
     */
    private static final String CLASSPATH = "classpath:";
    /**
     * i18n path for the resources.
     */
    private static final String I18N = "i18n";
    /**
     * Base path for each of the i18n resources.
     */
    private static final String BASE_PATH = CLASSPATH + SEP + I18N + SEP;
    /**
     * Base paths for the i18n resources.
     */
    private static final String[] I_18_N_BASE_NAMES = {
            BASE_PATH + "user",
            BASE_PATH + "role",
            BASE_PATH + "configuration",
            BASE_PATH + "label",
            BASE_PATH + "messages",
            BASE_PATH + "frameworkoverride",
            BASE_PATH + "validations",
            BASE_PATH + "field",
    };
    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A {@link MessageSource} for the {@link #I_18_N_BASE_NAMES} paths.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(I_18_N_BASE_NAMES);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return messageSource;
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return A {@link MessageResolver} for some {@code messageSource}.
     */
    @Bean
    public MessageResolver gmsMessageResolver() {
        return new MessageResolver(messageSource());
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
