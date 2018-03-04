package com.gms.repository.configuration;

import com.gms.domain.configuration.BConfiguration;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * BConfigurationRepository
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = ResourcePath.CONFIGURATION, path = ResourcePath.CONFIGURATION, exported = false)
public interface BConfigurationRepository extends CrudRepository<BConfiguration, Long> {

    BConfiguration findFirstByKey (String key);
    BConfiguration findFirstByKeyAndUserId (String key, Long userId);
    List<BConfiguration> findAllByKeyEndingWith(String endingString);
    Iterable<BConfiguration> findAllByKeyEndingWithAndUserIdIsNull(String endingString);
    List<BConfiguration> findAllByUserId(Long id);
}
