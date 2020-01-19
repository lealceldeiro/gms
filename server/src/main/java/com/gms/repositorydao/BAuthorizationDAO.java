package com.gms.repositorydao;

import com.gms.domain.security.role.BRole;
import com.gms.service.db.QueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@Getter
public abstract class BAuthorizationDAO {

    /**
     * Instance of {@link QueryService}.
     */
    private final QueryService queryService;

    /**
     * Returns all roles for a user over all entities.
     *
     * @param userId {@link com.gms.domain.security.user.EUser}'s id.
     * @return A Map where every key is the username of one {@link com.gms.domain.security.ownedentity.EOwnedEntity}
     * and the value associated to that key is a {@link List} of {@link BRole}s which are the roles of the
     * {@link com.gms.domain.security.user.EUser} with the given id over the entity with the username used as a key in
     * the map.
     */
    public abstract Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId);

    /**
     * Returns all roles for a user over all entities.
     *
     * @param userId   {@link com.gms.domain.security.user.EUser}'s id.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id.
     * @return A {@link List} of {@link BRole}s which are the roles of the
     * {@link com.gms.domain.security.user.EUser} with the given {@code userId} over the entity with the given
     * {@code entityId}.
     */
    public abstract List<BRole> getRolesForUserOverEntity(long userId, long entityId);

    final List<BRole> getRolesListFrom(final Iterable<Object[]> rawVal) {
        final int idIndex = 0;
        final int versionIndex = 1;
        final int labelIndex = 2;
        final int descriptionIndex = 3;
        final int enabledIndex = 4;
        BRole role;
        List<BRole> roles = new LinkedList<>();
        for (Object[] oVal : rawVal) {
            role = new BRole(oVal[labelIndex].toString());
            role.setId(Long.valueOf(oVal[idIndex].toString()));
            role.setVersion(Integer.valueOf(oVal[versionIndex].toString()));
            role.setEnabled(Boolean.valueOf(oVal[enabledIndex].toString()));
            role.setDescription(oVal[descriptionIndex].toString());
            roles.add(role);
        }

        return roles;
    }

}
