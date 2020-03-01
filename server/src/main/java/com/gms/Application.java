package com.gms;

import com.gms.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Entry point to the application.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * Application logger.
     */
    private static final Logger GMS_LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Main method from where the application is started.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        GMS_LOGGER.info("Application is running...");
    }

    /**
     * Extends SpringBootServletInitializer and overrides the configure method (See:
     * https://stackoverflow.com/q/39567434/5640649) and officially here:
     * https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/#howto-create-a-deployable-war-file.
     *
     * @param builder SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    //region beans

    //start up runner

    /**
     * Creates a command line runner bean to be provided to the Spring framework. This checks whether the application
     * loaded successfully the initial needed configuration.
     *
     * @param appService An instance of {@link AppService} to check if the application loaded all needed information
     *                   on startup properly.
     * @return a {@link CommandLineRunner} instance.
     */
    @Bean
    public CommandLineRunner commandLineRunner(final AppService appService) {
        return strings -> {
            if (!appService.isInitialLoadOK()) {
                GMS_LOGGER.error("App did not start properly and probably will fail at some point. Restarting it is "
                        + "highly advisable");
            }
        };
    }

    //bCrypt

    /**
     * Creates a {@link BCryptPasswordEncoder} to be provided to the Spring framework.
     *
     * @return A {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a {@link ErrorPageRegistrar} to be provided to the Spring framework.
     *
     * @return An {@link ErrorPageRegistrar}
     */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new IndexRedirectionPageRegistrar();
    }

    // used by bean
    private static class IndexRedirectionPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(final ErrorPageRegistry registry) {
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
        }

    }

    //endregion
}
