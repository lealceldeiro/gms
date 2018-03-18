package com.gms.repository.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = ResourcePath.OWNED_ENTITY, path = ResourcePath.OWNED_ENTITY)
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {

    EOwnedEntity findFirstByUsername(String username);

}
