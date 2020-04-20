package com.gms.service.security.ownedentity;

import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.EntityUtil;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.GmsGeneralException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class OwnedEntityServiceTest {

    /**
     * Instance of {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OwnedEntityServiceTest.class);

    /**
     * Instance of {@link OwnedEntityService}.
     */
    @Autowired
    private OwnedEntityService service;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository repository;
    /**
     * Instance of {@link DefaultConstant}.
     */
    @Autowired
    private DefaultConstant defaultConstant;
    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link ConfigurationService}.
     */
    @Autowired
    private ConfigurationService configService;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        assertTrue(appService.isInitialLoadOK());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createDefaultEntity() {
        assertNotNull(repository.findFirstByUsername(defaultConstant.getEntityDefaultUsername())); // username is unique
    }

    /**
     * Test to be executed by JUnit.
     */
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
            LOGGER.error(e.getLocalizedMessage());
            fail("Entity could not be created");
        }
        if (!initialIsM) {
            configService.setIsMultiEntity(false);
        }
    }

}
