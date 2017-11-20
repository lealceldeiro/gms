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

    @Value("${default.entity.multientity}")
    private String multiEntity = "false";

    private final BConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService(BConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    //region default config
    public boolean configurationExist() {
        return configurationRepository.count() > 0;
    }

    public BConfiguration createDefaultConfig() {
        BConfiguration c = new BConfiguration(ConfigKey.IS_MULTI_ENTITY_APP.toString(), this.multiEntity);
        if(this.configurationRepository.save(c) != null) {
            return c;
        }
        return null;
    }
    //endregion
}
