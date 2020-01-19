package com.gms.repository.security.authorization;

import com.gms.domain.security.role.BRole;

import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface BAuthorizationRepositoryCustom {

    /**
     * Returns a {@link Map} with all {@link com.gms.domain.security.role.BRole} a user has over all associated
     * {@link com.gms.domain.security.ownedentity.EOwnedEntity}.
     *
     * @param userId The user identifier.
     * @return A {@link Map} with all {@link com.gms.domain.security.role.BRole} a user has over all associated
     * {@link com.gms.domain.security.ownedentity.EOwnedEntity} structured as follow:
     * <p>
     * For every entity found an entry is created in the map with the username set as the map key. Under that key an
     * array of {@link com.gms.domain.security.role.BRole} is set. Theses roles match the one associated to the user
     * over the owned entity used for setting the map key with it's username.
     * </p>
     */
    Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId);

    /**
     * Returns a Map with all {@link com.gms.domain.security.role.BRole} a user has one
     * {@link com.gms.domain.security.ownedentity.EOwnedEntity}.
     *
     * @param userId   The user identifier.
     * @param entityId The associated entity to the user.
     * @return An {@link List} of {@link BRole} containing all the associated roles to the user over the entity which
     * identifier matches with the provided as second parameter.
     */
    List<BRole> getRolesForUserOverEntity(long userId, long entityId);

}
