package com.gms.repository.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * EOwnedEntityRepository
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = "entity", path = "entity")
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {

    EOwnedEntity findFirstByUsername(String username);

}
