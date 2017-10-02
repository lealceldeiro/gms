package com.gmsboilerplatesbng.repository.security.ownedEntity;

import com.gmsboilerplatesbng.domain.secuirty.ownedEntity.EOwnedEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "entity", path = "entity")
public interface EOwnedEntityRepository extends PagingAndSortingRepository<EOwnedEntity, Long> {
}
