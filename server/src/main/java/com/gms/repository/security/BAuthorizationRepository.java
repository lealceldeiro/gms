package com.gms.repository.security;

import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.user.EUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BAuthorizationRepository extends CrudRepository<BAuthorization, BAuthorization.BAuthorizationPk> {

    BAuthorization findFirstByUserAndEntityNotNull(EUser user);

    List<BAuthorization> findAllByUserAndAndEntityAndRoleIsNotNull(EUser user, EOwnedEntity entity);
}
