package com.gms.service.configuration;

import com.gms.Application;
import com.gms.domain.configuration.BConfiguration;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.testutil.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.domain.NotFoundEntityException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigurationServiceTest {

    /**
     * Instance of {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationServiceTest.class);

    /**
     * Instance of {@link ConfigurationService}.
     */
    @Autowired
    private ConfigurationService configurationService;
    /**
     * Instance of {@link BConfiguration}.
     */
    @Autowired
    private BConfigurationRepository configurationRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;
    /**
     * Instance of {@link BAuthorizationRepository}.
     */
    @Autowired
    private BAuthorizationRepository authRepository;
    /**
     * Instance of {@link DefaultConst}.
     */
    @Autowired
    private DefaultConst dc;

    /**
     * Key for argument "userRegistrationAllowed".
     */
    private final String keyUserRegistrationAllowed = ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString();
    /**
     * Key for argument "isMultientityAPp".
     */
    private final String keyMultiEntityApp = ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString();
    /**
     * Key for argument "language".
     */
    private final String keyLang = ConfigKey.LANGUAGE.toString();
    /**
     * Key for argument "lastAccessedEntity".
     */
    private final String keyLastAccessedEntity = ConfigKey.LAST_ACCESSED_ENTITY.toString();

    /**
     * Instance of {@link GMSRandom}.
     */
    private final GMSRandom random = new GMSRandom();

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void configurationExist() {
        assertTrue(configurationService.isApplicationConfigured() && configurationRepository.count() > 0);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createDefaultConfig() {
        configurationRepository.deleteAll();
        boolean ok = configurationService.isDefaultConfigurationCreated();
        assertTrue("Create default config failed", ok);

        String msg = "Default configuration was not created";
        BConfiguration c = configurationRepository.findFirstByKey(keyMultiEntityApp);
        assertNotNull(msg, c);

        c = configurationRepository.findFirstByKey(keyUserRegistrationAllowed);
        assertNotNull(msg, c);

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void assignDefaultUserToEntityWithRole() {
        final String msg = "Assign default user to entity with role failed";
        final boolean ok = configurationService.isDefaultUserAssignedToEntityWithRole();
        assertTrue(msg, ok);

        final EUser u = userRepository.findFirstByUsername(dc.getUserAdminDefaultUsername());
        final EOwnedEntity e = entityRepository.findFirstByUsername(dc.getEntityDefaultUsername());
        assertNotNull(u);
        assertNotNull(e);
        final List<BRole> roles = authRepository.getRolesForUserOverEntity(u.getId(), e.getId());
        assertTrue(msg, roles != null && !roles.isEmpty());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfig() {
        String key;
        String value;
        List<String> keys = new LinkedList<>();
        List<String> values = new LinkedList<>();
        final int top = 10;
        for (int i = 0; i < top; i++) {
            // all not "user-specific" configurations must have the "_IN_SERVER" suffix!
            key = random.nextString() + "_IN_SERVER";
            value = random.nextString();
            keys.add(key);
            values.add(value);
            assertNotNull(configurationRepository.save(new BConfiguration(key, value)));
        }

        Map<String, String> configs = configurationService.getConfig();
        Object config;
        for (int i = 0; i < keys.size(); i++) {
            config = configs.get(keys.get(i));
            assertNotNull("Configuration found if null", config);
            value = values.get(i);
            assertEquals("Configuration value returned by server (" + config
                    + ") does not match the expected", config, value);
        }

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfigByKey() {
        // make pristine
        final List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        // a configuration not "user-specific"
        BConfiguration c = configurationRepository.save(
                new BConfiguration(keyUserRegistrationAllowed, Boolean.toString(true))
        );
        ReflectionTestUtils.setField(configurationService, "userRegistrationAllowed", true);
        assertNotNull(c);
        try {
            Object value = configurationService.getConfig(c.getKey());
            assertNotNull("Configuration value is null despite it was saved via repository.", value);
            assertEquals("Value gotten from service is not equals to the previously saved configuration",
                    value.toString(), c.getValue());
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("Configuration with key " + c.getKey() + " not found despite it was saved via repository.");
        }

        configurationRepository.deleteById(c.getId());
        // re-set config values
        hasRestoredAllServerConfig(list);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfigByKeyAndUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);

        BConfiguration c = configurationRepository.save(new BConfiguration(keyLang, "en", u.getId()));
        assertNotNull(c);

        try {
            String value = configurationService.getConfig(keyLang, u.getId());
            assertNotNull("Configuration value is null despite it was saved via repository.", value);
            assertEquals("Value gotten from service is not equals to the previously saved configuration",
                    value, c.getValue());
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("Configuration with key " + c.getKey() + " for user with id " + u.getId()
                    + " not found despite it was saved via repository.");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfigByUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        BConfiguration c = configurationRepository.save(new BConfiguration(keyLang, "es", u.getId()));
        assertNotNull(c);
        BConfiguration c2 = configurationRepository.save(
                new BConfiguration(keyLastAccessedEntity, String.valueOf(e.getId()), u.getId())
        );
        assertNotNull(c2);
        Map<String, Object> configs = configurationService.getConfigByUser(u.getId());

        assertNotNull(configs.get(keyLang));
        assertNotNull(configs.get(keyLastAccessedEntity));

        assertEquals("Configuration values (language) do not match", "es", configs.get(keyLang));
        assertEquals("Configuration values (last accessed entity) do not match",
                configs.get(keyLastAccessedEntity), String.valueOf(e.getId()));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveConfig() {
        // make pristine
        List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        Map<String, Object> configs = new HashMap<>();
        // put sample values (opposite ones to the defined one in application-initial.properties
        configs.put(keyUserRegistrationAllowed, false);
        configs.put(keyMultiEntityApp, true);

        try {
            configurationService.saveConfig(configs);
            BConfiguration cR = configurationRepository.findFirstByKey(keyUserRegistrationAllowed);
            assertNotNull(cR);
            assertEquals(Boolean.toString(false), cR.getValue());
            cR = configurationRepository.findFirstByKey(keyMultiEntityApp);
            assertNotNull(cR);
            assertEquals(Boolean.toString(true), cR.getValue());
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("At least one of the keys was not found.");
        } catch (GmsGeneralException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("The provided user key was not valid");
        }

        // re-set config values
        assertTrue(hasRestoredAllServerConfig(list));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveConfigForUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(u);
        Map<String, Object> configs = new HashMap<>();
        configs.put(keyLang, "fr");
        configs.put(keyLastAccessedEntity, e.getId());
        try {
            configurationService.saveConfig(configs, u.getId());
        } catch (NotFoundEntityException e1) {
            LOGGER.error(e1.getLocalizedMessage());
            fail("At least one of the keys was not found.");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setUserRegistrationAllowed() {
        final List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        configurationService.setUserRegistrationAllowed(true);
        BConfiguration c = configurationRepository.findFirstByKey(keyUserRegistrationAllowed);
        assertNotNull(c);
        assertEquals(Boolean.toString(true), c.getValue());
        final Object allowed = ReflectionTestUtils.getField(configurationService, "userRegistrationAllowed");
        assertNotNull(allowed);
        assertEquals(Boolean.toString(true), allowed.toString());

        hasRestoredAllServerConfig(list);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setIsMultiEntity() {
        final List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        configurationService.setIsMultiEntity(true);
        BConfiguration c = configurationRepository.findFirstByKey(keyMultiEntityApp);
        assertNotNull(c);
        assertEquals(Boolean.toString(true), c.getValue());
        final Object multiEntity = ReflectionTestUtils.getField(configurationService, "multiEntity");
        assertNotNull(multiEntity);
        assertEquals(Boolean.toString(true), multiEntity.toString());

        hasRestoredAllServerConfig(list);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getLastAccessedEntityIdByUser() {
        EUser user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(user);
        EOwnedEntity entity = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(entity);
        BConfiguration c = configurationRepository.save(new BConfiguration(
                keyLastAccessedEntity, String.valueOf(entity.getId()), user.getId()
        ));
        assertNotNull(c);
        assertEquals(Long.valueOf(c.getValue()), configurationService.getLastAccessedEntityIdByUser(user.getId()));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setLastAccessedEntityIdByUser() {
        EUser user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(user);
        EOwnedEntity entity = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(entity);

        configurationService.setLastAccessedEntityIdByUser(user.getId(), entity.getId());

        BConfiguration c = configurationRepository.findFirstByKeyAndUserId(keyLastAccessedEntity, user.getId());
        assertNotNull(c);
        assertEquals(Long.valueOf(c.getValue()), entity.getId());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isMultiEntity() {
        //getter method
        Object multiEntityString = ReflectionTestUtils.getField(configurationService, "multiEntity");
        assertNotNull(multiEntityString);
        assertEquals(configurationService.isMultiEntity(), Boolean.parseBoolean(multiEntityString.toString()));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isUserRegistrationAllowed() {
        //getter method
        Object userRegistrationAllowedString = ReflectionTestUtils.getField(configurationService,
                "userRegistrationAllowed");
        assertNotNull(userRegistrationAllowedString);
        assertEquals(configurationService.isUserRegistrationAllowed(),
                Boolean.parseBoolean(userRegistrationAllowedString.toString()));
    }

    private List<Iterable<BConfiguration>> deleteAllServerConfig() {
        List<Iterable<BConfiguration>> list = new LinkedList<>();
        // make pristine
        Iterable<BConfiguration> it = configurationRepository
                .findAllByKeyEndingWithAndUserIdIsNull(keyUserRegistrationAllowed);
        list.add(it);
        Iterable<BConfiguration> it2 = configurationRepository
                .findAllByKeyEndingWithAndUserIdIsNull(keyMultiEntityApp);
        list.add(it2);
        configurationRepository.deleteAll(it);
        configurationRepository.deleteAll(it2);
        return list;
    }

    private boolean hasRestoredAllServerConfig(final Iterable<? extends Iterable<BConfiguration>> list) {
        for (Iterable<BConfiguration> it : list) {
            for (BConfiguration iConf : it) {
                configurationRepository.save(new BConfiguration(iConf.getKey(), iConf.getValue()));
            }
        }
        return true;
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfigInServerNotFound() {
        boolean success = false;
        try {
            configurationService.getConfig(random.nextString() + ConfigurationService.IN_SERVER);
        } catch (NotFoundEntityException e) {
            assertEquals(ConfigurationService.CONFIG_NOT_FOUND, e.getMessage());
            success = true;
        }
        assertTrue(success);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getConfigNotFound() {
        boolean success = false;
        try {
            configurationService.getConfig(random.nextString().replace(ConfigurationService.IN_SERVER, ""));
        } catch (NotFoundEntityException e) {
            assertEquals(ConfigurationService.CONFIG_NOT_FOUND, e.getMessage());
            success = true;
        }
        assertTrue(success);
    }


    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveConfigKeyNotFound() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(random.nextString(), random.nextString());
        boolean success = false;
        try {
            configurationService.saveConfig(configs);
        } catch (GmsGeneralException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("There was not provided user and the test still failed because of it");
        } catch (Exception e) {
            success = true;
        }
        assertTrue(success);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Transactional
    @Test
    public void assignDefaultUserToEntityWithRoleDefaultUserNotFound() {
        // Must return false when no default user is found
        EUser defaultUser = userRepository.findFirstByUsernameOrEmail(dc.getUserAdminDefaultName(),
                dc.getUserAdminDefaultEmail());
        authRepository.delete(authRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(defaultUser, true));
        userRepository.delete(defaultUser);
        assertFalse(configurationService.isDefaultUserAssignedToEntityWithRole());
    }

}
