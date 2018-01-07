package com.gms;

import com.gms.service.AppService;
import org.hibernate.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManagerFactory;


/**
 * Application
 * Entry point to the application.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /** Extends SpringBootServletInitializer and overrides the configure method (See:
     * https://stackoverflow.com/questions/39567434/spring-boot-application-gives-404-when-deployed-to-tomcat-but-works-with-embedde)
     * and officially here: https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/#howto-create-a-deployable-war-file
     * @param builder SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    //region beans

    //start up runner
    @Bean
    public CommandLineRunner commandLineRunner(AppService appService) {
        return strings -> {
            if (!appService.isInitialLoadOK()) {
                System.exit(1);
            }
        };
    }

    //bCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionFactory getSessionFactory(EntityManagerFactory factory) {
        final SessionFactory sf = factory.unwrap(SessionFactory.class);
        if (sf == null) {
            throw new NullPointerException("Factory is not an hibernate factory");
        }
        return sf;
    }

    //endregion
}
