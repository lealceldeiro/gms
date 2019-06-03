package com.gms.repository.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = OWNED_ENTITY, path = OWNED_ENTITY)
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {

    @RestResource(exported = false)
    EOwnedEntity findFirstByUsername(String username);

    @RestResource(path = ResourcePath.MULTI_LIKE, rel = ResourcePath.MULTI_LIKE)
    Page<EOwnedEntity> findByNameContainsIgnoreCaseOrUsernameContainsIgnoreCase(
            @Param(ResourcePath.NAME) String name, @Param(ResourcePath.USERNAME) String username, Pageable pageable
    );

    @RestResource(path = ResourcePath.MULTI, rel = ResourcePath.MULTI)
    Page<EOwnedEntity> findByNameEqualsOrUsernameEquals(
            @Param(ResourcePath.NAME) String name, @Param(ResourcePath.USERNAME) String username, Pageable pageable
    );

    @RestResource(path = ResourcePath.NAME_LIKE, rel = ResourcePath.NAME_LIKE)
    Page<EOwnedEntity> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = ResourcePath.NAME, rel = ResourcePath.NAME)
    Page<EOwnedEntity> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = USERNAME_LIKE, rel = USERNAME_LIKE)
    Page<EOwnedEntity> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String username, Pageable pageable);

    @RestResource(path = USERNAME, rel = USERNAME)
    Page<EOwnedEntity> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

}
