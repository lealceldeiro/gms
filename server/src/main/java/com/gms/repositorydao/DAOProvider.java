package com.gms.repositorydao;

import com.gms.service.db.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Component
public class DAOProvider {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    private static final String POSTGRESQL = "org.postgresql.Driver";
    private static final String MYSQL = "<todo-mysql>";
    private static final String ORACLE = "<todo-oracle>";

    private final QueryService queryService;

    @Autowired
    public DAOProvider(QueryService queryService) {
        this.queryService = queryService;
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
        checkQueryService();
        switch (driver) {
            case POSTGRESQL:
                return new PostgreSQLBAuthorizationDAO(queryService);
            case MYSQL:
                throw new NullPointerException("Support for DBMS MySQL driver has not been implemented yet");
            case ORACLE:
                throw new NullPointerException("Support for DBMS Oracle driver has not been implemented yet");
            default:
                throw new NullPointerException("DBMS Driver has not been specified");
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
        checkQueryService();
        switch (driver) {
            case POSTGRESQL:
                return new PostgreSQLBPermissionDAO(queryService);
            case MYSQL:
                throw new NullPointerException("Support for DBMS MySQL driver has not been implemented yet");
            case ORACLE:
                throw new NullPointerException("Support for DBMS Oracle driver has not been implemented yet");
            default:
                throw new NullPointerException("DBMS Driver has not been specified");
        }
    }

    private void checkQueryService() {
        if (queryService == null) {
            throw new NullPointerException("QueryService not found");
        }
    }
}
