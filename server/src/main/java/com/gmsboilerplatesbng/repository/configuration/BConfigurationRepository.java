package com.gmsboilerplatesbng.repository.configuration;

import com.gmsboilerplatesbng.domain.configuration.BConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * BConfigurationRepository
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource
public interface BConfigurationRepository extends CrudRepository<BConfiguration, Long> {

    BConfiguration findFirstByKey (String key);
    BConfiguration findFirstByKeyAndUserId (String key, Long userId);
}
