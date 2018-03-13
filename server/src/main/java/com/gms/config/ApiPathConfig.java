package com.gms.config;

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
 * ApiPathConfig (borrowed from https://stackoverflow.com/questions/32927937/how-to-set-base-url-for-rest-in-spring-boot/39907655#39907655)
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@PropertySource("classpath:application.properties")
@Configuration
public class ApiPathConfig {

    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Value("${spring.data.rest.basePath:/api}")
                    private String apiBasePath;

                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        PatternsRequestCondition apiPattern = new PatternsRequestCondition(apiBasePath)
                                .combine(mapping.getPatternsCondition());

                        mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
                                mapping.getMethodsCondition(), mapping.getParamsCondition(),
                                mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                                mapping.getProducesCondition(), mapping.getCustomCondition());

                        super.registerHandlerMethod(handler, method, mapping);
                    }
                };
            }
        };
    }

}