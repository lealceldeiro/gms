package com.gmsboilerplatesbng;

import com.gmsboilerplatesbng.service.configuration.ConfigurationService;
import com.gmsboilerplatesbng.service.security.permission.PermissionService;
import com.gmsboilerplatesbng.service.security.role.RoleService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.service.security.ownedEntity.OwnedEntityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Application
 * Entry point to the application.
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
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
    public CommandLineRunner commandLineRunner(ConfigurationService configurationService, UserService userService,
                                               OwnedEntityService oeService, PermissionService permissionService,
                                               RoleService roleService) {
        return strings -> {
            if (!configurationService.configurationExist()) { //first app start up
                boolean ok = configurationService.createDefaultConfig();
                ok = ok && permissionService.createDefaultPermissions();
                ok = ok && roleService.createDefaultRole() != null;
                ok = ok && userService.createDefaultUser() != null;
                ok = ok && oeService.createDefaultEntity() != null;
                ok = ok && configurationService.assignDefaultUserToEntityWithRole();
                if (!ok) {
                    System.exit(1);
                }
            }
        };
    }

    //bCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //endregion
}
