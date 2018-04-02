package com.gms.repository.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.QUERY_VALUE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@SuppressWarnings("unused")
@RepositoryRestResource(collectionResourceRel = ResourcePath.OWNED_ENTITY, path = ResourcePath.OWNED_ENTITY)
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {

    @RestResource(exported = false)
    EOwnedEntity findFirstByUsername(String username);

    @RestResource(
            path = ResourcePath.OWNED_ENTITY_SEARCH_NAME_USERNAME_LIKE,
            rel = ResourcePath.OWNED_ENTITY_SEARCH_NAME_USERNAME_LIKE
    )
    Page<EOwnedEntity> findByNameContainsIgnoreCaseOrUsernameContainsIgnoreCase(
            @Param(QUERY_VALUE) String name, @Param(QUERY_VALUE) String username, Pageable pageable
    );

    @RestResource(path = ResourcePath.OWNED_ENTITY_SEARCH_NAME_LIKE, rel = ResourcePath.OWNED_ENTITY_SEARCH_NAME_LIKE)
    Page<EOwnedEntity> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    @RestResource(path = ResourcePath.OWNED_ENTITY_SEARCH_NAME, rel = ResourcePath.OWNED_ENTITY_SEARCH_NAME)
    Page<EOwnedEntity> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = ResourcePath.OWNED_ENTITY_SEARCH_USERNAME_LIKE, rel = ResourcePath.OWNED_ENTITY_SEARCH_USERNAME_LIKE)
    Page<EOwnedEntity> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    @RestResource(path = ResourcePath.OWNED_ENTITY_SEARCH_USERNAME, rel = ResourcePath.OWNED_ENTITY_SEARCH_USERNAME)
    Page<EOwnedEntity> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

}
