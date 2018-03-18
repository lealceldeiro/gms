package com.gms.repositorydao;

import com.gms.service.db.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * DAOProvider
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 18, 2018
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
