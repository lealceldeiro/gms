package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.repositorydao.BPermissionDAO;
import com.gms.repositorydao.DAOProvider;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Transactional
public class BPermissionRepositoryImpl implements BPermissionRepositoryCustom {

    private final BPermissionDAO permissionDAO;

    BPermissionRepositoryImpl(DAOProvider daoProvider) {
        permissionDAO = daoProvider.getBPermissionDAO();
    }

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
