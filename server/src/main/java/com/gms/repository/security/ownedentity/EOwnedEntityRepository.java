package com.gms.repository.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.MULTI;
import static com.gms.util.constant.ResourcePath.MULTI_LIKE;
import static com.gms.util.constant.ResourcePath.NAME;
import static com.gms.util.constant.ResourcePath.NAME_LIKE;
import static com.gms.util.constant.ResourcePath.OWNED_ENTITY;
import static com.gms.util.constant.ResourcePath.QUERY_VALUE;
import static com.gms.util.constant.ResourcePath.USERNAME;
import static com.gms.util.constant.ResourcePath.USERNAME_LIKE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = OWNED_ENTITY, path = OWNED_ENTITY)
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {

    /**
     * Returns the first {@link EOwnedEntity} with a username equals to the provided argument. This method is guaranteed
     * to return always the same element since the entity's username is unique.
     *
     * @param username {@link EOwnedEntity]}'s username.
     * @return A {@link EOwnedEntity}
     */
    @RestResource(exported = false)
    EOwnedEntity findFirstByUsername(String username);

    /**
     * Returns all {@link EOwnedEntity}s with a name or username which are like the provided arguments.
     *
     * @param name     Name to be matched against to.
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = MULTI_LIKE, rel = MULTI_LIKE)
    Page<EOwnedEntity> findByNameContainsIgnoreCaseOrUsernameContainsIgnoreCase(
            @Param(NAME) String name, @Param(USERNAME) String username, Pageable pageable
    );

    /**
     * Returns all {@link EOwnedEntity}s with a name or username equals to the provided arguments.
     *
     * @param name     Name to be matched against to.
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = MULTI, rel = MULTI)
    Page<EOwnedEntity> findByNameEqualsOrUsernameEquals(
            @Param(NAME) String name, @Param(USERNAME) String username, Pageable pageable
    );

    /**
     * Returns all {@link EOwnedEntity}s with a name like the provided argument.
     *
     * @param name     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = NAME_LIKE, rel = NAME_LIKE)
    Page<EOwnedEntity> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String name, Pageable pageable);

    /**
     * Returns all {@link EOwnedEntity}s with a name equals to the provided argument.
     *
     * @param name     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = NAME, rel = NAME)
    Page<EOwnedEntity> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    /**
     * Returns all {@link EOwnedEntity}s with a username like the provided argument.
     *
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = USERNAME_LIKE, rel = USERNAME_LIKE)
    Page<EOwnedEntity> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String username, Pageable pageable);

    /**
     * Returns all {@link EOwnedEntity}s with a username equals to the provided argument.
     *
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EOwnedEntity}.
     */
    @RestResource(path = USERNAME, rel = USERNAME)
    Page<EOwnedEntity> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

}
