package com.gms.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * QueryService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 06, 2018
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QueryService {

    private final EntityManager entityManager;

    public Query createQuery(String qls) {
        return entityManager.createQuery(qls);
    }

    public Query createNativeQuery(String sql) {
        return entityManager.createNativeQuery(sql);
    }

    public Query createNativeQuery(String sql, Class clazz) {
        return entityManager.createNativeQuery(sql, clazz);
    }
}
