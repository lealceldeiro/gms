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

    /**
     * Database url connection.
     */
    @Value("${spring.datasource.url:null}")
    private String url;

    /**
     * Instance of {@link QueryService}.
     */
    private final QueryService queryService;

    /**
     * Message: Not implemented yet.
     */
    private static final String NOT_IMPLEMENTED_YET = "Either the `spring.datasource.url` property has not been"
            + " specified or support for the specified one has not been implemented yet";

    /**
     * Creates a new {@link DAOProvider} from the given arguments.
     *
     * @param queryService A {@link QueryService} instance to execute sql queries.
     */
    @Autowired
    public DAOProvider(final QueryService queryService) {
        this.queryService = Objects.requireNonNull(queryService);
    }

    /**
     * Returns a proper instance of a {@link BAuthorizationDAO} according to the current database management system.
     *
     * @return An extended-from-{@link BAuthorizationDAO} class.
     * @throws NullPointerException if the database driver property specified in application.properties under the key
     *                              "spring.datasource.driver-class-name" corresponds to one of the following database
     *                              management systems: i.e.: MySQL, Oracle.
     * @throws NullPointerException if the property "spring.datasource.driver-class-name" is not specified in the
     *                              application.properties.
     */
    public BAuthorizationDAO getBAuthorizationDAO() {
        if (DatabaseDriver.fromJdbcUrl(url) == DatabaseDriver.POSTGRESQL) {
            return new PostgreSQLBAuthorizationDAO(queryService);
        }
        throw new NullPointerException(NOT_IMPLEMENTED_YET);
    }

    /**
     * Returns a proper instance of a {@link BPermissionDAO} according to the current database management system.
     *
     * @return An extended-from-{@link BPermissionDAO} class.
     * @throws NullPointerException if the database driver property specified in application.properties under the key
     *                              "spring.datasource.driver-class-name" corresponds to one of the following database
     *                              management systems: i.e.: MySQL, Oracle.
     * @throws NullPointerException if the property "spring.datasource.driver-class-name" is not specified in the
     *                              application.properties.
     */
    public BPermissionDAO getBPermissionDAO() {
        if (DatabaseDriver.fromJdbcUrl(url) == DatabaseDriver.POSTGRESQL) {
            return new PostgreSQLBPermissionDAO(queryService);
        }
        throw new NullPointerException(NOT_IMPLEMENTED_YET);
    }

}
