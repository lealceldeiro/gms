package com.gmsboilerplatesbng.repository.security;

import com.gmsboilerplatesbng.domain.security.BAuthorization;
import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BAuthorizationRepository extends CrudRepository<BAuthorization, BAuthorization.BAuthorizationPk> {

    BAuthorization findFirstByUserAndEntity(EUser user, EOwnedEntity entity);

    BAuthorization findFirstByUserAndEntityNotNull(EUser user);
}
