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
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
public class ConfigurationService {

    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;

    /**
     * Instance of {@link BConfiguration}.
     */
    private final BConfigurationRepository configurationRepository;
    /**
     * Instance of {@link EUserRepository}.
     */
    private final EUserRepository userRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    private final EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link BRoleRepository}.
     */
    private final BRoleRepository roleRepository;
    /**
     * Instance of {@link BAuthorizationRepository}.
     */
    private final BAuthorizationRepository authRepository;

    /**
     * Indicates whether the app handles multiple owned entities ({@link EOwnedEntity} or not.
     */
    @Getter
    private boolean multiEntity;
    /**
     * Indicates whether the app allows new user ({@link EUser}) registration or not.
     */
    @Getter
    private boolean userRegistrationAllowed;

    /**
     * Configuration keys.
     */
    private static final List<ConfigKey> KEYS = Arrays.asList(ConfigKey.values());
    /**
     * Suffix to store app configuration.
     */
    public static final String IN_SERVER = "_IN_SERVER";

    /**
     * i18n key for the message shown when a configuration is not found.
     */
    public static final String CONFIG_NOT_FOUND = "config.not.found";
    /**
     * i18n key for the message shown when the argument provided to retrieve the user is not valid.
     */
    private static final String CONFIG_USER_PARAM_NUMBER = "config.param.user.not.valid";


    /**
     * Creates a new {@link ConfigurationService} from the given arguments.
     *
     * @param configurationRepository An instance of a {@link BConfigurationRepository}.
     * @param userRepository          An instance of a {@link EUserRepository}.
     * @param entityRepository        An instance of an {@link EOwnedEntityRepository}.
     * @param roleRepository          An instance of a {@link BRoleRepository}.
     * @param authRepository          An instance of an {@link BAuthorizationRepository}.
     * @param defaultConst            An instance of a {@link DefaultConst}.
     */
    @Autowired
    public ConfigurationService(final BConfigurationRepository configurationRepository,
                                final EUserRepository userRepository, final EOwnedEntityRepository entityRepository,
                                final BRoleRepository roleRepository, final BAuthorizationRepository authRepository,
                                final DefaultConst defaultConst) {
        this.configurationRepository = configurationRepository;
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authRepository = authRepository;
        this.dc = defaultConst;

        loadDBConfig();
    }

    //region default config

    /**
     * Returns whether the default configuration was set in place or not, buy counting the entries in the configuration
     * table.
     *
     * @return {@code true} if there is any entry in the configuration table, {@code false} otherwise.
     */
    public boolean isApplicationConfigured() {
        return configurationRepository.count() > 0;
    }

    /**
     * Creates the default system configuration. All not user-specific configurations are set in place after this method
     * is called.
     *
     * @return {@code true} if the configurations are created properly, {@code false} otherwise.
     */
    public boolean isDefaultConfigurationCreated() {
        BConfiguration isMultiEntity = new BConfiguration(
                ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString(),
                String.valueOf(dc.getIsMultiEntity())
        );
        BConfiguration isUserRegistrationAllowed = new BConfiguration(
                String.valueOf(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER),
                String.valueOf(dc.getIsUserRegistrationAllowed())
        );

        configurationRepository.save(isMultiEntity);
        configurationRepository.save(isUserRegistrationAllowed);
        multiEntity = Boolean.parseBoolean(isMultiEntity.getValue());
        userRegistrationAllowed = Boolean.parseBoolean(isUserRegistrationAllowed.getValue());

        return true;
    }

    /**
     * Creates the default configuration related to the default user, owned entity and roles assigned to him/her over
     * that owned entity. The required authorization of the default user is created after this method is called.
     *
     * @return {@code true} if the authorization is created properly, {@code false} otherwise.
     */
    public boolean isDefaultUserAssignedToEntityWithRole() {
        EUser u =
                userRepository.findFirstByUsernameOrEmail(dc.getUserAdminDefaultName(), dc.getUserAdminDefaultEmail());

        if (u != null) { //got default user
            EOwnedEntity e = entityRepository.findFirstByUsername(dc.getEntityDefaultUsername());
            if (e != null) { //got entity
                BRole role = roleRepository.findFirstByLabel(dc.getRoleAdminDefaultLabel());
                if (role != null) {
                    com.gms.domain.security.BAuthorization.BAuthorizationPk pk =
                            new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), role.getId());
                    authRepository.save(new BAuthorization(pk, u, e, role));

                    return true;
                }
            }
        }

        return false;
    }

    //endregion

    /**
     * Gets all configuration that are not "user-specific".
     *
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     */
    public Map<String, String> getConfig() {
        List<BConfiguration> configs = configurationRepository.findAllByKeyEndingWith(IN_SERVER);
        Map<String, String> map = new HashMap<>();
        for (BConfiguration c : configs) {
            map.put(c.getKey(), c.getValue());
        }

        return map;
    }

    /**
     * Gets a configuration which is not "user-specific".
     *
     * @param key String representation under the configuration is saved.
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     * @throws NotFoundEntityException if the configuration key is not valid.
     */
    public Object getConfig(final String key) throws NotFoundEntityException {
        String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        checkKey(uppercaseKey);

        if (uppercaseKey.endsWith(IN_SERVER)) {
            switch (ConfigKey.valueOf(uppercaseKey)) {
                case IS_MULTI_ENTITY_APP_IN_SERVER:
                    return isMultiEntity();
                case IS_USER_REGISTRATION_ALLOWED_IN_SERVER:
                    return isUserRegistrationAllowed();
                default:
                    throw new NotFoundEntityException(CONFIG_NOT_FOUND);
            }
        }

        BConfiguration c = getValue(uppercaseKey);
        if (c != null) {
            return c;
        }
        throw new NotFoundEntityException(CONFIG_NOT_FOUND);
    }

    /**
     * Gets a configuration which is "user-specific".
     *
     * @param key    String representation under the configuration is saved.
     * @param userId User identifier for who the configuration was saved.
     * @return A {@link String} with the configuration value saved under a specific key and for a specific user with
     * {@code id} as the provided under the {@code userId} param. If the key is a valid one but there is
     * not such configuration saved for the user with id as the one provide in {@code userId} param, {@code null}
     * is returned.
     * @throws NotFoundEntityException if the configuration key is not valid.
     */
    public String getConfig(final String key, final long userId) throws NotFoundEntityException {
        String upperCaseKey = key.toUpperCase(Locale.ENGLISH);
        checkKey(upperCaseKey);

        return getValueByUser(upperCaseKey, userId);
    }

    /**
     * Returns all configurations for a specific user.
     *
     * @param userId Identifier of the user whose configuration are going to be retrieved.
     * @return A {@link Map} containing all configurations for a user where every key is the configuration key and the
     * values is the configuration key associated to that value.
     * @see EUser
     * @see BConfiguration
     */
    public Map<String, Object> getConfigByUser(final long userId) {
        final List<BConfiguration> configs = configurationRepository.findAllByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        for (BConfiguration c : configs) {
            map.put(c.getKey(), c.getValue());
        }

        return map;
    }

    /**
     * Saves various configurations at once.
     *
     * @param configs A {@link Map} of configurations. Every key in the Map is the configuration key, and the
     *                corresponding value is the configuration value.
     * @throws NotFoundEntityException if any of the provided keys is not a valid key. Keys must correspond to any of
     *                                 the values in {@link ConfigKey} as String.
     * @see ConfigKey
     */
    public void saveConfig(final Map<String, Object> configs) throws NotFoundEntityException, GmsGeneralException {
        if (configs.get("user") != null) {
            try {
                long id = Long.parseLong(configs.remove("user").toString());
                saveConfig(configs, id);
            } catch (NumberFormatException e) {
                throw GmsGeneralException.builder()
                        .messageI18N(CONFIG_USER_PARAM_NUMBER)
                        .cause(e)
                        .finishedOK(false)
                        .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .build();
            }
        }

        String uppercaseKey;
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            uppercaseKey = entry.getKey().toUpperCase(Locale.ENGLISH);

            if (isValidKey(uppercaseKey) && uppercaseKey.endsWith(IN_SERVER)) {
                switch (ConfigKey.valueOf(uppercaseKey)) {
                    case IS_MULTI_ENTITY_APP_IN_SERVER:
                        setIsMultiEntity(Boolean.parseBoolean(entry.getValue().toString()));
                        break;
                    case IS_USER_REGISTRATION_ALLOWED_IN_SERVER:
                        setUserRegistrationAllowed(Boolean.parseBoolean(entry.getValue().toString()));
                        break;
                    default:
                        throw new NotFoundEntityException(CONFIG_NOT_FOUND);
                }
            }
        }
    }

    /**
     * Saves various configurations at once for a specific user.
     *
     * @param configs A {@link Map} of configurations. Every key in the Map is the configuration key, and the
     *                corresponding value is the configuration value.
     * @param userId  {@link EUser}'s id.
     * @throws NotFoundEntityException if any of the provided keys is not a valid key. Keys must correspond to any of
     *                                 the values in {@link ConfigKey} as String.
     * @see ConfigKey
     */
    public void saveConfig(final Map<String, Object> configs, final long userId) throws NotFoundEntityException {
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            checkKey(entry.getKey().toUpperCase(Locale.ENGLISH));
            insertOrUpdateValue(entry.getKey(), entry.getValue().toString(), userId);
        }
    }

    /**
     * Sets whether the user registration via sign-up is allowed or not.
     *
     * @param userRegistrationAllowed Indicates whether the user registration via sign-up is allowed or not.
     */
    public void setUserRegistrationAllowed(final boolean userRegistrationAllowed) {
        insertOrUpdateValue(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER, userRegistrationAllowed);
        this.userRegistrationAllowed = userRegistrationAllowed;
    }

    /**
     * Sets whether the application will handle multiple entities or not.
     *
     * @param isMultiEntity Indicates whether the application will handle multiple entities or not.
     */
    public void setIsMultiEntity(final boolean isMultiEntity) {
        insertOrUpdateValue(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER, isMultiEntity);
        this.multiEntity = isMultiEntity;
    }

    /**
     * Returns the identifier of the las accessed entity by a given user.
     *
     * @param userId Identifier of the user who the last accessed entity is being looked for.
     * @return The identifier of the last accessed entity or {@code null} if not found any.
     */
    public Long getLastAccessedEntityIdByUser(final long userId) {
        String v = getValueByUser(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        return v != null ? Long.parseLong(v) : null;
    }

    /**
     * Sets the last accessed entity by a user.
     *
     * @param userId   User id
     * @param entityId Entity id
     * @see EUser
     * @see EOwnedEntity
     */
    public void setLastAccessedEntityIdByUser(final long userId, final long entityId) {
        insertOrUpdateValue(ConfigKey.LAST_ACCESSED_ENTITY, entityId, userId);
    }

    //region private stuff
    private boolean isValidKey(final ConfigKey key) {
        return (KEYS.contains(key));
    }

    private boolean isValidKey(final String key) {
        return isValidKey(ConfigKey.valueOf(key));
    }

    private void checkKey(final ConfigKey key) throws NotFoundEntityException {
        if (!isValidKey(key)) {
            throw new NotFoundEntityException(CONFIG_NOT_FOUND);
        }
    }

    private void checkKey(final String key) throws NotFoundEntityException {
        try {
            checkKey(ConfigKey.valueOf(key));
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(CONFIG_NOT_FOUND);
        }
    }

    /**
     * Loads the most frequently queried configuration from database to memory.
     */
    private void loadDBConfig() {
        if (isApplicationConfigured()) {
            final BConfiguration multi =
                    configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString());
            if (multi != null) {
                multiEntity = Boolean.parseBoolean(multi.getValue());
            } else {
                setIsMultiEntity(dc.getIsMultiEntity());
            }
            final BConfiguration registration =
                    configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString());
            if (registration != null) {
                userRegistrationAllowed = Boolean.parseBoolean(registration.getValue());
            } else {
                setUserRegistrationAllowed(dc.getIsUserRegistrationAllowed());
            }
        } else {
            multiEntity = dc.getIsMultiEntity();
            userRegistrationAllowed = dc.getIsUserRegistrationAllowed();
        }
    }

    private void insertOrUpdateValue(final String key, final String value) {
        String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        BConfiguration c = configurationRepository.findFirstByKey(uppercaseKey);
        if (c == null) {
            c = new BConfiguration(uppercaseKey, value);
        } else {
            c.setValue(value);
        }
        configurationRepository.save(c);
    }

    private void insertOrUpdateValue(final String key, final String value, final long userId) {
        String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        BConfiguration c = configurationRepository.findFirstByKeyAndUserId(uppercaseKey, userId);
        if (c == null) {
            c = new BConfiguration(uppercaseKey, value, userId);
        } else {
            c.setValue(value);
        }
        configurationRepository.save(c);
    }

    private void insertOrUpdateValue(final ConfigKey key, final String value) {
        insertOrUpdateValue(key.toString(), value);
    }

    private void insertOrUpdateValue(final ConfigKey key, final String value, final long userId) {
        insertOrUpdateValue(key.toString(), value, userId);
    }

    private void insertOrUpdateValue(final ConfigKey key, final Object value) {
        insertOrUpdateValue(key, String.valueOf(value));
    }

    private void insertOrUpdateValue(final ConfigKey key, final Object value, final long userId) {
        insertOrUpdateValue(key, String.valueOf(value), userId);
    }

    private String getValueByUser(final String key, final long userId) {
        final BConfiguration c =
                configurationRepository.findFirstByKeyAndUserId(key.toUpperCase(Locale.ENGLISH), userId);
        if (c != null) {
            return c.getValue();
        }

        return null;
    }

    private BConfiguration getValue(final String key) {
        return configurationRepository.findFirstByKey(key.toUpperCase(Locale.ENGLISH));
    }
    //endregion
}
