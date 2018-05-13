package com.gms.service.security.ownedentity;

import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.EntityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OwnedEntityServiceTest {

    @Autowired private OwnedEntityService service;
    @Autowired private EOwnedEntityRepository repository;
    @Autowired private DefaultConst dc;
    @Autowired private AppService appService;
    @Autowired private ConfigurationService configService;

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
        boolean initialIsM = configService.isMultiEntity();
        if (!initialIsM) {
            configService.setIsMultiEntity(true);
        }
        try {
            final EOwnedEntity e = service.create(EntityUtil.getSampleEntity());
            assertNotNull(e);

            Optional<EOwnedEntity> er = repository.findById(e.getId());
            assertTrue(er.isPresent());

            assertEquals(e, er.get());

        } catch (GmsGeneralException e) {
            e.printStackTrace();
            fail("Entity could not be created");
        }
        if (!initialIsM) {
            configService.setIsMultiEntity(false);
        }
    }
}