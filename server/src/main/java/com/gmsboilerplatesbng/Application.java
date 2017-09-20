package com.gmsboilerplatesbng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /** Extends SpringBootServletInitializer and overrides the configure method (See:
     * https://stackoverflow.com/questions/39567434/spring-boot-application-gives-404-when-deployed-to-tomcat-but-works-with-embedde)
     * @param builder SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
