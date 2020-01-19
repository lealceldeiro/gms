package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface BPermissionRepositoryCustom {

    /**
     * Return the list of permissions associated to some role(s). This (these) role(s) is(are) the one(s) associated to
     * a user over an entity. Some conditions must be met in order to return the permissions:
     *
     * <ul>
     * <li> The following properties from the user must be true: {@code enabled}, {@code accountNonExpired},
     * {@code accountNonLocked} and {@code credentialsNonExpired} </li>
     * <li> The following properties from the role must be true: {@code enabled} </li>
     * </ul>
     *
     * @param userId   {@link com.gms.domain.security.user.EUser}'s id.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id.
     * @return List of {@link BPermission}s
     * @see com.gms.domain.security.user.EUser
     * @see com.gms.domain.security.ownedentity.EOwnedEntity
     */
    List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);

}
