package com.gmsboilerplatesbng.service.configuration;

import com.gmsboilerplatesbng.domain.configuration.BConfiguration;
import com.gmsboilerplatesbng.repository.configuration.BConfigurationRepository;
import com.gmsboilerplatesbng.util.configuration.ConfigKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfigurationService {

    @Value("${default.entity.multi-entity}")
    private Boolean isMultiEntity = false;

    @Value("${default.user.registration.allowed}")
    private Boolean isUserRegistrationAllowed = true;

    private final BConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService(BConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;

        loadDBConfig();
    }

    //region default config
    public boolean configurationExist() {
        return configurationRepository.count() > 0;
    }

    public Boolean createDefaultConfig() {
        BConfiguration multiEntity = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP.toString(), this.isMultiEntity.toString()),
                userRegistrationAllowed = new BConfiguration(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString(), this.isUserRegistrationAllowed.toString());

        return
                this.configurationRepository.save(multiEntity) != null &&
                        this.configurationRepository.save(userRegistrationAllowed) != null;
    }
    //endregion

    private void loadDBConfig() {
        if (configurationExist()) {
            this.isMultiEntity = Boolean.valueOf(
                    this.configurationRepository.findFirstByKey(ConfigKey.IS_MULTI_ENTITY_APP.toString()).getValue());
            this.isUserRegistrationAllowed = Boolean.valueOf(
                    this.configurationRepository.findFirstByKey(ConfigKey.IS_USER_REGISTRATION_ALLOWED.toString()).getValue());
        }
    }

    public Boolean isUserUserRegistrationAllowed() {
        return this.isUserRegistrationAllowed;
    }

    public void setUserRegistrationAllowed(Boolean userRegistrationAllowed) {
        this.isUserRegistrationAllowed = userRegistrationAllowed;
    }
    public Boolean isMultiEntity() {
        return this.isMultiEntity;
    }

    public void setIsMultiEntity(Boolean isMultiEntity) {
        this.isMultiEntity = isMultiEntity;
    }
}
