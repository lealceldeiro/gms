package com.gms.repositorydao;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.db.QueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Getter
@RequiredArgsConstructor
public abstract class BPermissionDAO {

    /**
     * Instance of {@link QueryService}.
     */
    private final QueryService queryService;

    /**
     * Returns the permission a user has over an owned entity.
     *
     * @param userId   {@link com.gms.domain.security.user.EUser}'s id.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id.
     * @return A {@link List} of {@link BPermission} containing the permissions a user has over an entity.
     */
    public abstract List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);

}
