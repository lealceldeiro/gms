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
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * ConfigurationService
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
public class ConfigurationService {

    private final DefaultConst dc;

    private Boolean isMultiEntity;

    private Boolean isUserRegistrationAllowed;

    private final BConfigurationRepository configurationRepository;

    private final EUserRepository userRepository;

    private final EOwnedEntityRepository entityRepository;

    private final BRoleRepository roleRepository;

    private final BAuthorizationRepository authRepository;

    @Autowired
    public ConfigurationService(BConfigurationRepository configurationRepository, EUserRepository userRepository,
                                EOwnedEntityRepository entityRepository, BRoleRepository roleRepository,
                                BAuthorizationRepository authRepository, DefaultConst defaultConst) {
        this.configurationRepository = configurationRepository;
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authRepository = authRepository;
        this.dc = defaultConst;

        loadDBConfig();
    }

    //region default config
    public boolean configurationExist() {
        return configurationRepository.count() > 0;
    }

    public Boolean createDefaultConfig() {
        BConfiguration multiEntity = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP.toString(),
                dc.getIsMultiEntity().toString());
        BConfiguration userRegistrationAllowed = new BConfiguration(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString(),
                dc.getIsUserRegistrationAllowed().toString());

        return
                configurationRepository.save(multiEntity) != null &&
                        configurationRepository.save(userRegistrationAllowed) != null;
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
            isMultiEntity = Boolean.valueOf(
                    configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP.toString()).getValue());
            isUserRegistrationAllowed = Boolean.valueOf(
                    configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString()).getValue());
        }
        else {
            isMultiEntity = dc.getIsMultiEntity();
            isUserRegistrationAllowed = dc.getIsUserRegistrationAllowed();
        }
    }

    public boolean isUserUserRegistrationAllowed() {
        return isUserRegistrationAllowed;
    }

    public void setUserRegistrationAllowed(Boolean userRegistrationAllowed) {
        isUserRegistrationAllowed = userRegistrationAllowed;
    }
    public boolean isMultiEntity() {
        return isMultiEntity;
    }

    public void setIsMultiEntity(Boolean isMultiEntity) {
        this.isMultiEntity = isMultiEntity;
    }

    public Long getLastAccessedEntityIdByUser(Long userId) {
        final BConfiguration c = configurationRepository.findFirstByKeyAndUserId(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        if (c != null) {
            return Long.valueOf(c.getValue());
        }
        return null;
    }
}
