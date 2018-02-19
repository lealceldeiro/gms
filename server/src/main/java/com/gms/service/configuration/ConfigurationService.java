package com.gms.service.configuration;

import com.gms.domain.configuration.BConfiguration;
import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.db.QueryService;
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConfigurationService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
public class ConfigurationService {

    private final DefaultConst dc;

    private final BConfigurationRepository configurationRepository;
    private final EUserRepository userRepository;
    private final EOwnedEntityRepository entityRepository;
    private final BRoleRepository roleRepository;
    private final BAuthorizationRepository authRepository;
    private final QueryService queryService;

    @Getter private boolean multiEntity;
    @Getter private boolean userRegistrationAllowed;

    private static final List<ConfigKey> KEYS = Arrays.asList(ConfigKey.values());
    private static final String IN_SERVER = "_IN_SERVER";
    private static final String CONFIG_NOT_FOUND = "config.not.found";


    @Autowired
    public ConfigurationService(BConfigurationRepository configurationRepository, EUserRepository userRepository,
                                EOwnedEntityRepository entityRepository, BRoleRepository roleRepository,
                                BAuthorizationRepository authRepository, DefaultConst defaultConst, QueryService queryService) {
        this.configurationRepository = configurationRepository;
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authRepository = authRepository;
        this.dc = defaultConst;
        this.queryService = queryService;

        loadDBConfig();
    }

    //region default config
    public boolean configurationExist() {
        return configurationRepository.count() > 0;
    }

    public boolean createDefaultConfig() {
        BConfiguration isMultiEntity = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString(),
                dc.getIsMultiEntity().toString());
        BConfiguration isUserRegistrationAllowed = new BConfiguration(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString(),
                dc.getIsUserRegistrationAllowed().toString());

        return
                configurationRepository.save(isMultiEntity) != null &&
                        configurationRepository.save(isUserRegistrationAllowed) != null;
    }

    public boolean assignDefaultUserToEntityWithRole() {
        EUser u = userRepository.findFirstByUsernameOrEmail(dc.getUserAdminDefaultName(), dc.getUserAdminDefaultEmail());
        if (u != null) { //got default user
            EOwnedEntity e = entityRepository.findFirstByUsername(dc.getEntityDefaultUsername());
            if (e != null) { //got entity
                BRole role = roleRepository.findFirstByLabel(dc.getRoleAdminDefaultLabel());
                if (role != null) {
                    com.gms.domain.security.BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), role.getId());

                    BAuthorization auth = new BAuthorization();
                    auth.setBAuthorizationPk(pk);
                    auth.setUser(u);
                    auth.setEntity(e);
                    auth.setRole(role);

                    return authRepository.save(auth) != null;
                }
            }
        }
        return false;
    }

    //endregion

    /**
     * Gets all configuration that are not "user-specific"
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     */
    public Map getConfig() {
        List<BConfiguration> configs = configurationRepository.findAllByKeyEndingWith(IN_SERVER);
        Map<String, String> map = new HashMap<>();
        for (BConfiguration c: configs) {
            map.put(c.getKey(), c.getValue());
        }
        return map;
    }

    /**
     * Gets a configuration which is not "user-specific"
     * @param key String representation under the configuration is saved.
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     * @throws NotFoundEntityException if the configuration key is not valid.
     */
    public Object getConfig(String key) throws NotFoundEntityException {
        key = key.toUpperCase();
        checkKey(key);

        if (key.endsWith(IN_SERVER)) {
            switch (ConfigKey.valueOf(key)) {
                case IS_MULTI_ENTITY_APP_IN_SERVER:
                    return isMultiEntity();
                case IS_USER_REGISTRATION_ALLOWED_IN_SERVER:
                    return isUserRegistrationAllowed();
                default: throw new NotFoundEntityException(CONFIG_NOT_FOUND);
            }
        }
        throw new NotFoundEntityException(CONFIG_NOT_FOUND);
    }

    /**
     * Gets a configuration which is "user-specific"
     * @param key String representation under the configuration is saved.
     * @param userId User identifier for who the configuration was saved.
     * @return A {@link String} with the configuration value saved under a specific key and for a specific user with
     * <code>id</code> as the provided under the <code>userId</code> param. If the key is a valid one but there is
     * not such configuration saved for the user with id as the one provide in <code>userId</code> param, <code>null</code>
     * is returned.
     * @throws NotFoundEntityException if the configuration key is not valid.
     */
    public String getConfig(String key, long userId) throws NotFoundEntityException {
        key = key.toUpperCase();
        checkKey(key);
        return getValueByUser(key, userId);
    }

    public Map getConfigByUser(long userId) {
        final List<BConfiguration> configs = configurationRepository.findAllByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        for (BConfiguration c: configs) {
            map.put(c.getKey(), c.getValue());
        }
        return map;
    }

    public boolean saveConfig(Map<String, Object> configs) throws NotFoundEntityException {
        if (configs.get("user") != null) {
            long id = Long.parseLong(configs.remove("user").toString());
            return saveConfig(configs, id);
        }
        boolean allOK = true;
        String kU;
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            kU = entry.getKey().toUpperCase();
            if (isValidKey(kU)) {
                if (kU.endsWith(IN_SERVER)) {
                    switch (ConfigKey.valueOf(kU)) {
                        case IS_MULTI_ENTITY_APP_IN_SERVER:
                            setIsMultiEntity(Boolean.parseBoolean(entry.getValue().toString()));
                            break;
                        case IS_USER_REGISTRATION_ALLOWED_IN_SERVER:
                            setUserRegistrationAllowed(Boolean.parseBoolean(entry.getValue().toString()));
                            break;
                        default: throw new NotFoundEntityException(CONFIG_NOT_FOUND);
                    }
                }
                else allOK = false;
            }
            else allOK = false;
        }
        return allOK;
    }

    public boolean saveConfig(Map<String, Object> configs, long userId) {
        boolean allOK = true;
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            if (!insertOrUpdateValue(entry.getKey(), entry.getValue().toString(), userId)) {
                allOK = false;
            }
        }
        return allOK;
    }

    /**
     * Sets whether the user registration via sign-up is allowed or not.
     * @param userRegistrationAllowed Indicates whether the user registration via sign-up is allowed or not.
     * @return <code>true</code> if the configuration was set properly. <code>false</code> if not.
     */
    public boolean setUserRegistrationAllowed(boolean userRegistrationAllowed) {
        if (updateValue(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER, userRegistrationAllowed)) {
            this.userRegistrationAllowed = userRegistrationAllowed;
            return true;
        }
        return false;
    }

    /**
     * Sets whether the application will handle multiple entities or not.
     * @param isMultiEntity Indicates whether the application will handle multiple entities or not.
     * @return <code>true</code> if the configuration was set properly. <code>false</code> if not.
     */
    public boolean setIsMultiEntity(boolean isMultiEntity) {
        if (updateValue(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER, isMultiEntity)) {
            this.multiEntity = isMultiEntity;
            return true;
        }
        return false;
    }

    /**
     * Returns the identifier of the las accessed entity by a given user.
     * @param userId Identifier of the user who the last accessed entity is being looked for.
     * @return The identifier of the last accessed entity or <code>null</code> if not found any.
     */
    public Long getLastAccessedEntityIdByUser(long userId) {
        String v = getValueByUser(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        return v != null ? Long.parseLong(v) : null;
    }

    public boolean setLastAccessedEntityIdByUser(long userId, long entityId) {
        return insertOrUpdateValue(ConfigKey.LAST_ACCESSED_ENTITY, entityId, userId);
    }

    //region private stuff
    private boolean isValidKey(ConfigKey key) {
        return (KEYS.contains(key));
    }

    private boolean isValidKey(String key) {
        return isValidKey(ConfigKey.valueOf(key));
    }

    private void checkKey(ConfigKey key) throws NotFoundEntityException {
        if (!isValidKey(key)) throw new NotFoundEntityException(CONFIG_NOT_FOUND);
    }

    private void checkKey(String key) throws NotFoundEntityException {
        try {
            checkKey(ConfigKey.valueOf(key));
        }
        catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(CONFIG_NOT_FOUND);
        }
    }

    /**
     * Loads the most frequently queried configuration from database to memory
     */
    private void loadDBConfig() {
        if (configurationExist()) {
            final BConfiguration multi = configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString());
            if (multi != null) {
                multiEntity = Boolean.parseBoolean(multi.getValue());
            }
            else {
                setIsMultiEntity(dc.getIsMultiEntity());
            }
            final BConfiguration registration = configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString());
            if (registration != null) {
                userRegistrationAllowed = Boolean.parseBoolean(registration.getValue());
            }
            else {
                setUserRegistrationAllowed(dc.getIsUserRegistrationAllowed());
            }
        }
        else {
            multiEntity = dc.getIsMultiEntity();
            userRegistrationAllowed = dc.getIsUserRegistrationAllowed();
        }
    }

    private boolean updateValue(String key, String value) {
        return queryService.createQuery("update BConfiguration set value = :value where key = :key")
                .setParameter("key", key)
                .setParameter("value", value)
                .executeUpdate() > 0;
    }

    private boolean updateValue(String key, String value, long userId) {
        return queryService.createQuery("update BConfiguration set value = :value where key = :key and userId = :userId")
                .setParameter("key", key)
                .setParameter("value", value)
                .setParameter("userId", userId)
                .executeUpdate() > 0;
    }

    private boolean updateValue(ConfigKey key, String value) {
        return updateValue(String.valueOf(key), value);
    }

    private boolean updateValue(ConfigKey key, String value, long userId) {
        return updateValue(String.valueOf(key), value, userId);
    }

    private boolean updateValue(ConfigKey key, Object value) {
        return updateValue(String.valueOf(key), String.valueOf(value));
    }

    private boolean updateValue(ConfigKey key, Object value, long userId) {
        return updateValue(String.valueOf(key), String.valueOf(value), userId);
    }

    private boolean insertOrUpdateValue(String key, String value) {
        BConfiguration c = configurationRepository.findFirstByKey(key);
        if (c == null) {
            c = new BConfiguration(key, value);
        }
        return configurationRepository.save(c) != null;
    }

    private boolean insertOrUpdateValue(String key, String value, long userId) {
        BConfiguration c = configurationRepository.findFirstByKeyAndUserId(key, userId);
        if (c == null) {
            c = new BConfiguration(key, value, userId);
        }
        else {
            c.setValue(value);
        }
        return configurationRepository.save(c) != null;
    }

    private boolean insertOrUpdateValue(ConfigKey key, String value) {
        return insertOrUpdateValue(key.toString(), value);
    }

    private boolean insertOrUpdateValue(ConfigKey key, String value, long userId) {
        return insertOrUpdateValue(key.toString(), value, userId);
    }

    private boolean insertOrUpdateValue(ConfigKey key, Object value) {
        return insertOrUpdateValue(key, String.valueOf(value));
    }

    private boolean insertOrUpdateValue(ConfigKey key, Object value, long userId) {
        return insertOrUpdateValue(key, String.valueOf(value), userId);
    }

    private String getValueByUser(String key, long userId) {
        final BConfiguration c = configurationRepository.findFirstByKeyAndUserId(key, userId);
        if (c != null) {
            return c.getValue();
        }
        return null;
    }
    //endregion
}
