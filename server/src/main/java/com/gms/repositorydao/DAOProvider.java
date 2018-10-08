package com.gms.repositorydao;

import com.gms.service.db.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Component
public class DAOProvider {

    @Value("${spring.datasource.url:null}")
    private String url;

    private final QueryService queryService;

    private static final String NOT_IMPLEMENTED_YET = "Whether the `spring.datasource.url` property has not been" +
            "specified or support for the specified one has not been implemented yet";

    @Autowired
    public DAOProvider(QueryService queryService) {
        this.queryService = Objects.requireNonNull(queryService);
    }

    /**
     * Returns a proper instance of a {@link BAuthorizationDAO} according to the current database management system.
     * @return An extended-from-{@link BAuthorizationDAO} class.
     * @throws NullPointerException if the database driver property specified in application.properties under the key
     * "spring.datasource.driver-class-name" corresponds to one of the following database management systems: MySQL,
     * Oracle.
     * @throws NullPointerException if the property "spring.datasource.driver-class-name" is not specified in the
     * application.properties.
     */
    public BAuthorizationDAO getBAuthorizationDAO () {
        switch (DatabaseDriver.fromJdbcUrl(url)) {
            case POSTGRESQL:
                return new PostgreSQLBAuthorizationDAO(queryService);
            default:
                throw new NullPointerException(NOT_IMPLEMENTED_YET);
        }
    }

    /**
     * Returns a proper instance of a {@link BPermissionDAO} according to the current database management system.
     * @return An extended-from-{@link BPermissionDAO} class.
     * @throws NullPointerException if the database driver property specified in application.properties under the key
     * "spring.datasource.driver-class-name" corresponds to one of the following database management systems: MySQL,
     * Oracle.
     * @throws NullPointerException if the property "spring.datasource.driver-class-name" is not specified in the
     * application.properties.
     */
    public BPermissionDAO getBPermissionDAO () {
        switch (DatabaseDriver.fromJdbcUrl(url)) {
            case POSTGRESQL:
                return new PostgreSQLBPermissionDAO(queryService);
            default:
                throw new NullPointerException(NOT_IMPLEMENTED_YET);
        }
    }

}
