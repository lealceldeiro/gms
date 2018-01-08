package com.gms.service.configuration;

import com.gms.domain.configuration.BConfiguration;
import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.db.QueryService;
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    @Getter
    private boolean multiEntity;

    @Getter
    private boolean userRegistrationAllowed;

    private final BConfigurationRepository configurationRepository;

    private final EUserRepository userRepository;

    private final EOwnedEntityRepository entityRepository;

    private final BRoleRepository roleRepository;

    private final BAuthorizationRepository authRepository;

    private final QueryService queryService;

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
        BConfiguration isMultiEntity = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP.toString(),
                dc.getIsMultiEntity().toString());
        BConfiguration isUserRegistrationAllowed = new BConfiguration(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString(),
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
                    com.gms.domain.security.BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();
                    pk.setUserId(u.getId());
                    pk.setEntityId(e.getId());
                    pk.setRoleId(role.getId());

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
     * Loads the most frequently queried configuration from database to memory
     */
    private void loadDBConfig() {
        if (configurationExist()) {
            multiEntity = Boolean.parseBoolean(
                    configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP.toString()).getValue());
            userRegistrationAllowed = Boolean.parseBoolean(
                    configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString()).getValue());
        }
        else {
            multiEntity = dc.getIsMultiEntity();
            userRegistrationAllowed = dc.getIsUserRegistrationAllowed();
        }
    }

    /**
     * Sets whether the user registration via sign-up is allowed or not.
     * @param userRegistrationAllowed Indicates whether the user registration via sign-up is allowed or not.
     * @return <code>true</code> if the configuration was set properly. <code>false</code> if not.
     */
    public boolean setUserRegistrationAllowed(boolean userRegistrationAllowed) {
        String hql = "update BConfiguration set value = " + userRegistrationAllowed +
                " where key = '" + ConfigKey.IS_MULTI_ENTITY_APP + "'";
        int ar = queryService.createQuery(hql).executeUpdate();
        if (ar > 0) {
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
        String hql = "update BConfiguration set value = " + isMultiEntity +
                " where key = '" + ConfigKey.IS_USER_REGISTRATION_ALLOWED + "'";
        int ar = queryService.createQuery(hql).executeUpdate();
        if (ar > 0) {
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
        final BConfiguration c = configurationRepository.findFirstByKeyAndUserId(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        if (c != null) {
            return Long.parseLong(c.getValue());
        }
        return null;
    }

    public boolean setLastAccessedEntityIdByUser(long userId, long entityId) {
        BConfiguration c = configurationRepository.findFirstByKeyAndUserId(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        if (c == null) {
            c = new BConfiguration(ConfigKey.LAST_ACCESSED_ENTITY.toString(), String.valueOf(entityId));
            c.setUserId(userId);
        }
        else {
            c.setValue(String.valueOf(entityId));
        }
        return configurationRepository.save(c) != null;
    }
}
