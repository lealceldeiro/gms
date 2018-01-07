package com.gms.service.db;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * QueryService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 06, 2018
 */
@Data
@Service
@Transactional
public class QueryService {

    @Getter(AccessLevel.NONE)
    private final SessionFactory sessionFactory;

    public Query createQuery(String hql) {
        Session s = sessionFactory.getCurrentSession();
        return s != null ? s.createQuery(hql) : null;
    }
}
