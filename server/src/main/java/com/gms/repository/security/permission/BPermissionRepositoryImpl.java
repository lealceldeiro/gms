package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.repository.security.permission.dao.BPermissionDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.List;

/**
 * BPermissionRepositoryImpl
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 13, 2018
 */
@RequiredArgsConstructor
@Transactional
public class BPermissionRepositoryImpl implements BPermissionRepositoryCustom {

    @Qualifier("PostgreSQLBPermissionDAO")
    private final BPermissionDAO permissionDAO;

    /**
     * Return the list of permissions associated to some role(s). This (theses) role(s) is(are) the one(s) associated to
     * a user over an entity. Some conditions must be met in order to return the permissions:
     * - The following properties from the user must be true: <code>enabled</code>, <code>accountNonExpired</code>,
     * <code>accountNonLocked</code> and <code>credentialsNonExpired</code>
     * - The following properties from the role must be true: <code>enabled</code>
     */
    @Override
    public List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId) {
        return permissionDAO.findPermissionsByUserIdAndEntityId(userId, entityId);
    }
}
