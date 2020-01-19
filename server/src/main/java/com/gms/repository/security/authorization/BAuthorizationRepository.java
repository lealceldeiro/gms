package com.gms.repository.security.authorization;

import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.user.EUser;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        collectionResourceRel = ResourcePath.AUTHORIZATION, path = ResourcePath.AUTHORIZATION, exported = false
)
public interface BAuthorizationRepository extends CrudRepository<BAuthorization, BAuthorization.BAuthorizationPk>,
        BAuthorizationRepositoryCustom {

    /**
     * Returns the first {@link com.gms.domain.configuration.BConfiguration} associated to a {@link EUser} and which is
     * also associated to an {@link com.gms.domain.security.ownedentity.EOwnedEntity} and which associated
     * {@link com.gms.domain.security.role.BRole} has the {@code enabled} property set to a given parameter.
     *
     * @param user    {@link EUser} associated to the {@link com.gms.domain.configuration.BConfiguration}.
     * @param enabled Indicates whether the {@link com.gms.domain.security.role.BRole} associated to the
     *                {@link BAuthorization} should be enabled or not.
     * @return A {@link BAuthorization}
     * @see com.gms.domain.security.role.BRole
     */
    BAuthorization findFirstByUserAndEntityNotNullAndRoleEnabled(EUser user, Boolean enabled);

}
