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

    /**
     * An instance of {@link BPermissionDAO}.
     */
    private final BPermissionDAO permissionDAO;

    BPermissionRepositoryImpl(final DAOProvider daoProvider) {
        permissionDAO = daoProvider.getBPermissionDAO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not intended to be overridden. If a custom implementation is required, consider implementing
     * {@link BPermissionRepositoryCustom}
     */
    @Override
    public List<BPermission> findPermissionsByUserIdAndEntityId(final long userId, final long entityId) {
        return permissionDAO.findPermissionsByUserIdAndEntityId(userId, entityId);
    }

}
