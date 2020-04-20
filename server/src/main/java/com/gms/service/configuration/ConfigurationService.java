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
import com.gms.util.configuration.BusinessConfigurationKey;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gms.domain.security.BAuthorization.BAuthorizationPk;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
public class ConfigurationService {

    /**
     * Instance of {@link DefaultConstant}.
     */
    private final DefaultConstant defaultConstant;

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
    private static final List<BusinessConfigurationKey> KEYS = Arrays.asList(BusinessConfigurationKey.values());
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
     * @param defaultConstant         An instance of a {@link DefaultConstant}.
     */
    @Autowired
    public ConfigurationService(final BConfigurationRepository configurationRepository,
                                final EUserRepository userRepository, final EOwnedEntityRepository entityRepository,
                                final BRoleRepository roleRepository, final BAuthorizationRepository authRepository,
                                final DefaultConstant defaultConstant) {
        this.configurationRepository = configurationRepository;
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authRepository = authRepository;
        this.defaultConstant = defaultConstant;

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
        final BConfiguration isMultiEntity =
                new BConfiguration(BusinessConfigurationKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString(),
                                   String.valueOf(defaultConstant.getIsMultiEntity()));
        final BConfiguration isUserRegistrationAllowed = new BConfiguration(
                String.valueOf(BusinessConfigurationKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER),
                String.valueOf(defaultConstant.getIsUserRegistrationAllowed())
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
        final EUser user = userRepository.findFirstByUsernameOrEmail(defaultConstant.getUserAdminDefaultName(),
                                                                     defaultConstant.getUserAdminDefaultEmail());
        if (user == null) {
            return false;
        }

        final EOwnedEntity entity = entityRepository.findFirstByUsername(defaultConstant.getEntityDefaultUsername());
        if (entity == null) {
            return false;
        }

        final BRole role = roleRepository.findFirstByLabel(defaultConstant.getRoleAdminDefaultLabel());
        if (role == null) {
            return false;
        }

        final BAuthorizationPk primaryKey = new BAuthorizationPk(user.getId(), entity.getId(), role.getId());
        authRepository.save(new BAuthorization(primaryKey, user, entity, role));

        return true;
    }

    //endregion

    /**
     * Returns all configuration that are not "user-specific".
     *
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     */
    public Map<String, String> getNonUserSpecificConfigurations() {
        return configurationRepository
                .findAllByKeyEndingWith(IN_SERVER)
                .stream()
                .collect(Collectors.toMap(BConfiguration::getKey, BConfiguration::getValue));
    }

    /**
     * Gets a configuration which is not "user-specific".
     *
     * @param key String representation under the configuration is saved.
     * @return A {@link Map} with all the configuration where every key is the configuration key and every value is the
     * configuration value.
     * @throws NotFoundEntityException if the configuration key is not valid.
     */
    public Object getNonUserSpecificConfigurations(final String key) throws NotFoundEntityException {
        final String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        checkKey(uppercaseKey);

        if (uppercaseKey.endsWith(IN_SERVER)) {
            switch (BusinessConfigurationKey.valueOf(uppercaseKey)) {
                case IS_MULTI_ENTITY_APP_IN_SERVER:
                    return isMultiEntity();
                case IS_USER_REGISTRATION_ALLOWED_IN_SERVER:
                    return isUserRegistrationAllowed();
                default:
                    throw new NotFoundEntityException(CONFIG_NOT_FOUND);
            }
        }

        final BConfiguration configuration = getValueByKey(uppercaseKey);
        if (configuration != null) {
            return configuration;
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
    public String getNonUserSpecificConfigurations(final String key, final long userId) throws NotFoundEntityException {
        final String upperCaseKey = key.toUpperCase(Locale.ENGLISH);
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
    public Map<String, Object> getConfigurationsByUser(final long userId) {
        return configurationRepository
                .findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(BConfiguration::getKey, BConfiguration::getValue));
    }

    /**
     * Saves various configurations at once.
     *
     * @param configurations A {@link Map} of configurations. Every key in the Map is the configuration key, and the
     *                       corresponding value is the configuration value.
     * @throws NotFoundEntityException if any of the provided keys is not a valid key. Keys must correspond to any of
     *                                 the values in {@link BusinessConfigurationKey} as String.
     * @see BusinessConfigurationKey
     */
    public void saveConfigurations(final Map<String, Object> configurations) throws NotFoundEntityException,
            GmsGeneralException {

        if (configurations.get("user") != null) {
            try {
                long id = Long.parseLong(configurations.remove("user").toString());
                saveConfigurations(configurations, id);
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
        for (Map.Entry<String, Object> entry : configurations.entrySet()) {
            uppercaseKey = entry.getKey().toUpperCase(Locale.ENGLISH);

            if (isValidKey(uppercaseKey) && uppercaseKey.endsWith(IN_SERVER)) {
                switch (BusinessConfigurationKey.valueOf(uppercaseKey)) {
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
     * @param configurations A {@link Map} of configurations. Every key in the Map is the configuration key, and the
     *                       corresponding value is the configuration value.
     * @param userId         {@link EUser}'s id.
     * @throws NotFoundEntityException if any of the provided keys is not a valid key. Keys must correspond to any of
     *                                 the values in {@link BusinessConfigurationKey} as String.
     * @see BusinessConfigurationKey
     */
    public void saveConfigurations(final Map<String, Object> configurations,
                                   final long userId) throws NotFoundEntityException {
        for (Map.Entry<String, Object> configuration : configurations.entrySet()) {
            checkKey(configuration.getKey().toUpperCase(Locale.ENGLISH));
            insertOrUpdateValue(configuration.getKey(), configuration.getValue().toString(), userId);
        }
    }

    /**
     * Sets whether the user registration via sign-up is allowed or not.
     *
     * @param userRegistrationAllowed Indicates whether the user registration via sign-up is allowed or not.
     */
    public void setUserRegistrationAllowed(final boolean userRegistrationAllowed) {
        insertOrUpdateValue(BusinessConfigurationKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER, userRegistrationAllowed);
        this.userRegistrationAllowed = userRegistrationAllowed;
    }

    /**
     * Sets whether the application will handle multiple entities or not.
     *
     * @param isMultiEntity Indicates whether the application will handle multiple entities or not.
     */
    public void setIsMultiEntity(final boolean isMultiEntity) {
        insertOrUpdateValue(BusinessConfigurationKey.IS_MULTI_ENTITY_APP_IN_SERVER, isMultiEntity);
        this.multiEntity = isMultiEntity;
    }

    /**
     * Returns the identifier of the las accessed entity by a given user.
     *
     * @param userId Identifier of the user who the last accessed entity is being looked for.
     * @return The identifier of the last accessed entity or {@code null} if not found any.
     */
    public Long getLastAccessedEntityIdByUser(final long userId) {
        String lastAccessedEntityId = getValueByUser(BusinessConfigurationKey.LAST_ACCESSED_ENTITY.toString(), userId);

        return lastAccessedEntityId != null ? Long.parseLong(lastAccessedEntityId) : null;
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
        insertOrUpdateValue(BusinessConfigurationKey.LAST_ACCESSED_ENTITY, entityId, userId);
    }

    //region private stuff
    private boolean isValidKey(final BusinessConfigurationKey key) {
        return (KEYS.contains(key));
    }

    private boolean isValidKey(final String key) {
        return isValidKey(BusinessConfigurationKey.valueOf(key));
    }

    private void checkKey(final BusinessConfigurationKey key) throws NotFoundEntityException {
        if (!isValidKey(key)) {
            throw new NotFoundEntityException(CONFIG_NOT_FOUND);
        }
    }

    private void checkKey(final String key) throws NotFoundEntityException {
        try {
            checkKey(BusinessConfigurationKey.valueOf(key));
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(CONFIG_NOT_FOUND);
        }
    }

    /**
     * Loads the most frequently queried configuration from database to memory.
     */
    private void loadDBConfig() {
        if (isApplicationConfigured()) {
            final BConfiguration isMultiEntity =
                    configurationRepository
                            .findFirstByKey(BusinessConfigurationKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString());

            if (isMultiEntity != null) {
                multiEntity = Boolean.parseBoolean(isMultiEntity.getValue());
            } else {
                setIsMultiEntity(defaultConstant.getIsMultiEntity());
            }

            final BConfiguration isUserRegistrationAllowedConfiguration =
                    configurationRepository
                            .findFirstByKey(BusinessConfigurationKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString());

            if (isUserRegistrationAllowedConfiguration != null) {
                userRegistrationAllowed = Boolean.parseBoolean(isUserRegistrationAllowedConfiguration.getValue());
            } else {
                setUserRegistrationAllowed(defaultConstant.getIsUserRegistrationAllowed());
            }
        } else {
            multiEntity = defaultConstant.getIsMultiEntity();
            userRegistrationAllowed = defaultConstant.getIsUserRegistrationAllowed();
        }
    }

    private void insertOrUpdateValue(final String key, final String value) {
        final String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        BConfiguration configuration = configurationRepository.findFirstByKey(uppercaseKey);

        if (configuration == null) {
            configuration = new BConfiguration(uppercaseKey, value);
        } else {
            configuration.setValue(value);
        }
        configurationRepository.save(configuration);
    }

    private void insertOrUpdateValue(final String key, final String value, final long userId) {
        final String uppercaseKey = key.toUpperCase(Locale.ENGLISH);
        BConfiguration configuration = configurationRepository.findFirstByKeyAndUserId(uppercaseKey, userId);

        if (configuration == null) {
            configuration = new BConfiguration(uppercaseKey, value, userId);
        } else {
            configuration.setValue(value);
        }
        configurationRepository.save(configuration);
    }

    private void insertOrUpdateValue(final BusinessConfigurationKey key, final String value) {
        insertOrUpdateValue(key.toString(), value);
    }

    private void insertOrUpdateValue(final BusinessConfigurationKey key, final String value, final long userId) {
        insertOrUpdateValue(key.toString(), value, userId);
    }

    private void insertOrUpdateValue(final BusinessConfigurationKey key, final Object value) {
        insertOrUpdateValue(key, String.valueOf(value));
    }

    private void insertOrUpdateValue(final BusinessConfigurationKey key, final Object value, final long userId) {
        insertOrUpdateValue(key, String.valueOf(value), userId);
    }

    private String getValueByUser(final String key, final long userId) {
        final BConfiguration configuration =
                configurationRepository.findFirstByKeyAndUserId(key.toUpperCase(Locale.ENGLISH), userId);

        return configuration != null ? configuration.getValue() : null;
    }

    private BConfiguration getValueByKey(final String key) {
        return configurationRepository.findFirstByKey(key.toUpperCase(Locale.ENGLISH));
    }
    //endregion
}
