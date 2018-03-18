package com.gms.service.db;

import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.util.EntityUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QueryServiceTest {

    @Autowired private EntityManager entityManager;

    private QueryService queryService;

    @Autowired private BPermissionRepository repository;

    @Before
    public void setUp() {
        queryService = new QueryService(entityManager);
    }

    @Test
    public void createQuery() {
        assertNotNull(repository.save(EntityUtil.getSamplePermission()));

        final Query query = queryService.createQuery("select e from BPermission e");
        assertNotNull(query);
        final List resultList = query.getResultList();
        assertNotNull(resultList);
        assertTrue(!resultList.isEmpty());
    }

    @Test
    public void createQueryWithResultClass() {
        BPermission p = repository.save(EntityUtil.getSamplePermission());
        assertNotNull(p);

        final Query query = queryService.createQuery("select e from BPermission e", BPermission.class);
        assertNotNull(query);
        final List resultList = query.getResultList();
        assertNotNull(resultList);
        assertTrue(!resultList.isEmpty());
        assertTrue(resultList.contains(p));
    }

    @Test
    public void createNativeQuery() {
        assertNotNull(repository.save(EntityUtil.getSamplePermission()));

        final Query nativeQuery = queryService.createNativeQuery("SELECT * FROM bpermission");
        assertNotNull(nativeQuery);

        final List resultList = nativeQuery.getResultList();
        assertNotNull(resultList);
        assertTrue(!resultList.isEmpty());
    }

    @Test
    public void createNativeQueryWithResultClass() {
        BPermission p = repository.save(EntityUtil.getSamplePermission());
        assertNotNull(p);

        final Query nativeQuery = queryService.createNativeQuery("SELECT * FROM bpermission", BPermission.class);
        assertNotNull(nativeQuery);

        final List resultList = nativeQuery.getResultList();
        assertNotNull(resultList);
        assertTrue(resultList.contains(p));
    }
}