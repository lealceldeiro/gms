package com.gmsboilerplatesbng.repository.security;

import com.gmsboilerplatesbng.domain.security.BAuthorization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BAuthorizationRepository extends CrudRepository<BAuthorization, BAuthorization.BAuthorizationPk> {

    BAuthorization findFirstByUserAndEntity(Long userId, Long entityId);

    BAuthorization findFirstByUserAndEntityNotNull(Long userId);
}
