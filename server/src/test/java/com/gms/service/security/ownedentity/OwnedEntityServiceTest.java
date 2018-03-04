package com.gms.service.security.ownedentity;

import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.service.AppService;
import com.gms.util.EntityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * OwnedEntityServiceTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 04, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OwnedEntityServiceTest {

    @Autowired private OwnedEntityService service;
    @Autowired private EOwnedEntityRepository repository;
    @Autowired private DefaultConst dc;
    @Autowired private AppService appService;

    @Before
    public void setUp () {
        assertTrue(appService.isInitialLoadOK());
    }

    @Test
    public void createDefaultEntity() {
        assertNotNull(repository.findFirstByUsername(dc.getEntityDefaultUsername())); // username is unique
    }

    @Test
    public void create() {
        try {
            final EOwnedEntity e = service.create(EntityUtil.getSampleEntity());
            assertNotNull(e);

            EOwnedEntity er = repository.findOne(e.getId());
            assertNotNull(er);

            assertEquals(e, er);

        } catch (GmsGeneralException e) {
            e.printStackTrace();
            fail("Entity could not be created");
        }
    }
}