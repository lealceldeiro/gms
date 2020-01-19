package com.gms.repository.configuration;

import com.gms.domain.configuration.BConfiguration;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(
        collectionResourceRel = ResourcePath.CONFIGURATION,
        path = ResourcePath.CONFIGURATION,
        exported = false
)
public interface BConfigurationRepository extends CrudRepository<BConfiguration, Long> {

    /**
     * Returns the first configuration found whose key matches a given parameter.
     *
     * @param key Key to match against to.
     * @return The first {@link BConfiguration} found that matches the criteria.
     */
    BConfiguration findFirstByKey(String key);

    /**
     * Returns the first configuration found whose key matches a given parameters.
     *
     * @param key    Key to match against to.
     * @param userId User id to match against to.
     * @return The first {@link BConfiguration} found that matches the criteria.
     */
    BConfiguration findFirstByKeyAndUserId(String key, Long userId);

    /**
     * Returns all configurations whose keys ends a given parameters.
     *
     * @param endingString Key ending to match against to.
     * @return A {@link List} of {@link BConfiguration} found that match the criteria.
     */
    List<BConfiguration> findAllByKeyEndingWith(String endingString);

    /**
     * Returns all configurations whose keys ends a given parameters and are not associated to any user.
     *
     * @param endingString Key ending to match against to.
     * @return An {@link Iterable} of {@link BConfiguration}s which match the criteria.
     */
    Iterable<BConfiguration> findAllByKeyEndingWithAndUserIdIsNull(String endingString);

    /**
     * Returns all {@link BConfiguration} associated to a user.
     *
     * @param id {@link com.gms.domain.security.user.EUser}'s id to find the configuration associated to that user.
     * @return A {@link List} of {@link BConfiguration} associated to a user with id equals to the provided {@code id}.
     */
    List<BConfiguration> findAllByUserId(Long id);

}
