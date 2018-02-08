package com.gms.repository.security;

import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.user.EUser;
import com.gms.util.constant.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = Resource.AUTHORIZATION_PATH, path = Resource.AUTHORIZATION_PATH)
public interface BAuthorizationRepository extends CrudRepository<BAuthorization, BAuthorization.BAuthorizationPk> {

    BAuthorization findFirstByUserAndEntityNotNull(EUser user);
}
