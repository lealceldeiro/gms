package com.gms.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QueryService {

    private final EntityManager entityManager;

    /**
     * Creates a query.
     * @param qls Hql or Sql string.
     * @return An executable {@link Query}
     */
    public Query createQuery(String qls) {
        return entityManager.createQuery(qls);
    }

    /**
     * Creates a query and map the result to a domain class.
     * @param qls Hql or Sql string.
     * @param clazz Domain class to which the result will be mapped to.
     * @return An executable {@link Query}
     */
    public Query createQuery(String qls, Class<?> clazz) {
        return entityManager.createQuery(qls, clazz);
    }

    /**
     * Creates a query.
     * @param sql Sql string.
     * @return An executable {@link Query}
     */
    public Query createNativeQuery(String sql) {
        return entityManager.createNativeQuery(sql);
    }

    /**
     * Creates a query and map the result to a domain class.
     * @param sql Sql string.
     * @param clazz Domain class to which the result will be mapped to.
     * @return An executable {@link Query}
     */
    public Query createNativeQuery(String sql, Class clazz) {
        return entityManager.createNativeQuery(sql, clazz);
    }

}
