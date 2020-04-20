package com.gms.appconfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Based on content borrowed from https://stackoverflow.com/a/39907655/5640649 .
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@PropertySource("classpath:application.properties")
@Configuration
public class ApiPathConfig {

    /**
     * this method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return a {@link WebMvcRegistrations}
     */
    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Value("${spring.data.rest.basePath:/api}")
                    private String apiBasePath;

                    @Override
                    protected void registerHandlerMethod(final Object handler, final Method method,
                                                         final RequestMappingInfo mapping) {
                        PatternsRequestCondition apiPattern = new PatternsRequestCondition(apiBasePath)
                                .combine(mapping.getPatternsCondition());

                        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(
                                mapping.getName(),
                                apiPattern,
                                mapping.getMethodsCondition(),
                                mapping.getParamsCondition(),
                                mapping.getHeadersCondition(),
                                mapping.getConsumesCondition(),
                                mapping.getProducesCondition(),
                                mapping.getCustomCondition()
                        );

                        super.registerHandlerMethod(handler, method, requestMappingInfo);
                    }
                };
            }
        };
    }

}
