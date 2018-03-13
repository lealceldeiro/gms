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

/**
 * LocaleConfig
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RequiredArgsConstructor
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    private static final String [] i18NBaseNames = {
            "classpath:/i18n/entity",
            "classpath:/i18n/user",
            "classpath:/i18n/role",
            "classpath:/i18n/configuration",
            "classpath:/i18n/label",
            "classpath:/i18n/messages",
            "classpath:/i18n/frameworkoverride",
            "classpath:/i18n/validations",
            "classpath:/i18n/field",
    };

    private final DefaultConst dc;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(i18NBaseNames);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public MessageResolver messageResolver() {
        return new MessageResolver(messageSource());
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new GmsCLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName(dc.getLanguageHolder());
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}