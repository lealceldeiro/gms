package com.gms.config;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import java.util.Set;

/**
 * Configures the repositories for exposing the {@code id} property of the domain entities.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param config Instance of {@link RepositoryRestConfiguration}.
     */
    @Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config) {
        Reflections reflections = new Reflections("com.gms.domain", new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        for (Class<?> iClass : classes) {
            config.exposeIdsFor(iClass);
        }
    }

}
