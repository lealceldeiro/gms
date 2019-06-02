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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigurationServiceTest {

    @Autowired ConfigurationService configurationService;
    @Autowired BConfigurationRepository configurationRepository;
    @Autowired EOwnedEntityRepository entityRepository;
    @Autowired EUserRepository userRepository;
    @Autowired BAuthorizationRepository authRepository;
    @Autowired DefaultConst dc;

    private final String keyUserRegistrationAllowed = ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString();
    private final String keyMultiEntityApp = ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString();
    private final String keyLang = ConfigKey.LANGUAGE.toString();
    private final String keyLastAccessedEntity = ConfigKey.LAST_ACCESSED_ENTITY.toString();

    private final GMSRandom random = new GMSRandom();

    @Test
    public void configurationExist() {
        assertTrue(configurationService.configurationExist() == configurationRepository.count() > 0);
    }

    @Test
    public void createDefaultConfig() {
        configurationRepository.deleteAll();
        boolean ok = configurationService.createDefaultConfig();
        assertTrue("Create default config failed", ok);

        String msg = "Default configuration was not created";
        BConfiguration c = configurationRepository.findFirstByKey(keyMultiEntityApp);
        assertNotNull(msg, c);

        c = configurationRepository.findFirstByKey(keyUserRegistrationAllowed);
        assertNotNull(msg, c);

    }

    @Test
    public void assignDefaultUserToEntityWithRole() {
        final String msg = "Assign default user to entity with role failed";
        final boolean ok = configurationService.assignDefaultUserToEntityWithRole();
        assertTrue(msg, ok);

        final EUser u = userRepository.findFirstByUsername(dc.getUserAdminDefaultUsername());
        final EOwnedEntity e = entityRepository.findFirstByUsername(dc.getEntityDefaultUsername());
        assertNotNull(u);
        assertNotNull(e);
        final List<BRole> roles = authRepository.getRolesForUserOverEntity(u.getId(), e.getId());
        assertTrue(msg, roles != null && !roles.isEmpty());
    }

    @Test
    public void getConfig() {
        String key;
        String value;
        List<String> keys = new LinkedList<>();
        List<String> values = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            key = random.nextString() + "_IN_SERVER"; // all not "user-specific" configurations must have the "_IN_SERVER" suffix!
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
            assertTrue("Configuration value returned by server (" + config.toString() +
                    ") does not match the expected", config.equals(value));
        }

    }

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
            assertTrue("Value gotten from service is not equals to the previously saved configuration",
                    value.toString().equals(c.getValue()));
        } catch (NotFoundEntityException e) {
            e.printStackTrace();
            fail("Configuration with key " + c.getKey() + " not found despite it was saved via repository." );
        }

        configurationRepository.deleteById(c.getId());
        // re-set config values
        restoreAllServerConfig(list);
    }

    @Test
    public void getConfigByKeyAndUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);

        BConfiguration c = configurationRepository.save(new BConfiguration(keyLang, "en", u.getId()));
        assertNotNull(c);

        try {
            String value = configurationService.getConfig(keyLang, u.getId());
            assertNotNull("Configuration value is null despite it was saved via repository.", value);
            assertTrue("Value gotten from service is not equals to the previously saved configuration",
                    value.equals(c.getValue()));
        } catch (NotFoundEntityException e) {
            e.printStackTrace();
            fail("Configuration with key " + c.getKey() + " for user with id" + u.getId() +
                    " not found despite it was saved via repository." );
        }
    }

    @Test
    public void getConfigByUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        BConfiguration c = configurationRepository.save(new BConfiguration(keyLang, "es", u.getId()));
        assertNotNull(c);
        BConfiguration c2 = configurationRepository.save(
                new BConfiguration(keyLastAccessedEntity, e.getId().toString(), u.getId())
        );
        assertNotNull(c2);
        Map<String, Object> configs = configurationService.getConfigByUser(u.getId());

        assertNotNull(configs.get(keyLang));
        assertNotNull(configs.get(keyLastAccessedEntity));

        assertTrue("Configuration values (language) do not match", configs.get(keyLang).equals("es" ));
        assertTrue("Configuration values (last accessed entity) do not match",
                configs.get(keyLastAccessedEntity).equals(e.getId().toString()));
    }

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
            assertTrue(cR.getValue().equals(Boolean.toString(false)));
            cR = configurationRepository.findFirstByKey(keyMultiEntityApp);
            assertNotNull(cR);
            assertTrue(cR.getValue().equals(Boolean.toString(true)));
        } catch (NotFoundEntityException e) {
            e.printStackTrace();
            fail("At least one of the keys was not found." );
        } catch (GmsGeneralException e) {
            e.printStackTrace();
            fail("The provided user key was not valid" );
        }

        // re-set config values
        assertTrue(restoreAllServerConfig(list));
    }

    @Test
    public void saveConfigForUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(u);
        Map<String, Object> configs = new HashMap<>();
        configs.put(keyLang, "fr" );
        configs.put(keyLastAccessedEntity, e.getId());
        try {
            configurationService.saveConfig(configs, u.getId());
        } catch (NotFoundEntityException e1) {
            e1.printStackTrace();
            fail("At least one of the keys was not found." );
        }
    }

    @Test
    public void setUserRegistrationAllowed() {
        final List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        configurationService.setUserRegistrationAllowed(true);
        BConfiguration c = configurationRepository.findFirstByKey(keyUserRegistrationAllowed);
        assertNotNull(c);
        assertTrue(c.getValue().equals(Boolean.toString(true)));
        assertTrue(ReflectionTestUtils.getField(configurationService, "userRegistrationAllowed" ).toString().equals(Boolean.toString(true)));

        restoreAllServerConfig(list);
    }

    @Test
    public void setIsMultiEntity() {
        final List<Iterable<BConfiguration>> list = deleteAllServerConfig();

        configurationService.setIsMultiEntity(true);
        BConfiguration c = configurationRepository.findFirstByKey(keyMultiEntityApp);
        assertNotNull(c);
        assertTrue(c.getValue().equals(Boolean.toString(true)));
        assertTrue(ReflectionTestUtils.getField(configurationService, "multiEntity" ).toString().equals(Boolean.toString(true)));

        restoreAllServerConfig(list);
    }

    @Test
    public void getLastAccessedEntityIdByUser() {
        EUser user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(user);
        EOwnedEntity entity = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(entity);
        BConfiguration c = configurationRepository.save(new BConfiguration(keyLastAccessedEntity, entity.getId().toString(), user.getId()));
        assertNotNull(c);
        assertEquals(Long.valueOf(c.getValue()), configurationService.getLastAccessedEntityIdByUser(user.getId()));
    }

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

    @Test
    public void isMultiEntity() {
        //getter method
        String multiEntityString = ReflectionTestUtils.getField(configurationService, "multiEntity" ).toString();
        assertTrue(configurationService.isMultiEntity() == Boolean.parseBoolean(multiEntityString));
    }

    @Test
    public void isUserRegistrationAllowed() {
        //getter method
        String userRegistrationAllowedString = ReflectionTestUtils.getField(configurationService, "userRegistrationAllowed" ).toString();
        assertTrue(configurationService.isUserRegistrationAllowed() == Boolean.parseBoolean(userRegistrationAllowedString));
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

    private boolean restoreAllServerConfig(List<Iterable<BConfiguration>> list) {
        for (Iterable<BConfiguration> it : list) {
            for (BConfiguration iConf : it) {
                configurationRepository.save(new BConfiguration(iConf.getKey(), iConf.getValue()));
            }
        }
        return true;
    }

    @Test
    public void getConfigInServerNotFound() {
        boolean success = false;
        try {
            configurationService.getConfig(random.nextString() + ConfigurationService.IN_SERVER);
        } catch (NotFoundEntityException e) {
            assertTrue(e.getMessage().equals(ConfigurationService.CONFIG_NOT_FOUND));
            success = true;
        }
        assertTrue(success);
    }

    @Test
    public void getConfigNotFound() {
        boolean success = false;
        try {
            configurationService.getConfig(random.nextString().replace(ConfigurationService.IN_SERVER, "" ));
        } catch (NotFoundEntityException e) {
            assertTrue(e.getMessage().equals(ConfigurationService.CONFIG_NOT_FOUND));
            success = true;
        }
        assertTrue(success);
    }


    @Test
    public void saveConfigKeyNotFound() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(random.nextString(), random.nextString());
        boolean success = false;
        try {
            configurationService.saveConfig(configs);
        } catch (GmsGeneralException e) {
            e.printStackTrace();
            fail("There was not provided user and the test still failed because of it" );
        } catch (Exception e) {
            success = true;
        }
        assertTrue(success);
    }

    @Transactional
    @Test
    public void assignDefaultUserToEntityWithRoleDefaultUserNotFound() {
        // Must return false when no default user is found
        EUser defaultUser = userRepository.findFirstByUsernameOrEmail(dc.getUserAdminDefaultName(),
                dc.getUserAdminDefaultEmail());
        authRepository.delete(authRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(defaultUser, true));
        userRepository.delete(defaultUser);
        assertTrue(!configurationService.assignDefaultUserToEntityWithRole());
    }
}