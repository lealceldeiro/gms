package com.gmsboilerplatesbng.service.configuration;

import com.gmsboilerplatesbng.domain.configuration.BConfiguration;
import com.gmsboilerplatesbng.domain.security.BAuthorization;
import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.repository.configuration.BConfigurationRepository;
import com.gmsboilerplatesbng.repository.security.BAuthorizationRepository;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.repository.security.user.EUserRepository;
import com.gmsboilerplatesbng.util.configuration.ConfigKey;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfigurationService {

    private final DefaultConst c;

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
        this.c = defaultConst;

        loadDBConfig();
    }

    //region default config
    public boolean configurationExist() {
        return configurationRepository.count() > 0;
    }

    public Boolean createDefaultConfig() {
        BConfiguration multiEntity = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP.toString(), this.c.IS_MULTI_ENTITY.toString()),
                userRegistrationAllowed = new BConfiguration(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString(), this.c.IS_USER_REGISTRATION_ALLOWED.toString());

        return
                this.configurationRepository.save(multiEntity) != null &&
                        this.configurationRepository.save(userRegistrationAllowed) != null;
    }

    public boolean assignDefaultUserToEntityWithRole() {
        EUser u = this.userRepository.findFirstByUsernameOrEmail(this.c.USER_USERNAME, this.c.USER_EMAIL);
        if (u != null) { //got default user
            EOwnedEntity e = this.entityRepository.findFirstByUsername(this.c.ENTITY_USERNAME);
            if (e != null) { //got entity
                BRole role = this.roleRepository.findFirstByLabel(this.c.ROLE_LABEL);
                if (role != null) {
                    com.gmsboilerplatesbng.domain.security.BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();
                    pk.setUserId(u.getId());
                    pk.setEntityId(e.getId());
                    pk.setRoleId(role.getId());

                    BAuthorization auth = new BAuthorization();
                    auth.setBAuthorizationPk(pk);
                    auth.setUser(u);
                    auth.setEntity(e);
                    auth.setRole(role);

                    return this.authRepository.save(auth) != null;
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
            this.isMultiEntity = Boolean.valueOf(
                    this.configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP.toString()).getValue());
            this.isUserRegistrationAllowed = Boolean.valueOf(
                    this.configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString()).getValue());
        }
        else {
            this.isMultiEntity = this.c.IS_MULTI_ENTITY;
            this.isUserRegistrationAllowed = this.c.IS_USER_REGISTRATION_ALLOWED;
        }
    }

    public boolean isUserUserRegistrationAllowed() {
        return this.isUserRegistrationAllowed;
    }

    public void setUserRegistrationAllowed(Boolean userRegistrationAllowed) {
        this.isUserRegistrationAllowed = userRegistrationAllowed;
    }
    public boolean isMultiEntity() {
        return this.isMultiEntity;
    }

    public void setIsMultiEntity(Boolean isMultiEntity) {
        this.isMultiEntity = isMultiEntity;
    }

    public Long getLastAccessedEntityIdByUser(Long userId) {
        final BConfiguration c = this.configurationRepository.findFirstByKeyAndUserId(ConfigKey.LAST_ACCESSED_ENTITY.toString(), userId);
        if (c != null) {
            return Long.valueOf(c.getValue());
        }
        return null;
    }
}
