package com.gms.repository.security.authorization;

import com.gms.domain.security.role.BRole;
import com.gms.repositorydao.BAuthorizationDAO;
import com.gms.repositorydao.DAOProvider;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Transactional
public class BAuthorizationRepositoryImpl implements BAuthorizationRepositoryCustom {

    /**
     * An instance of {@link BAuthorizationDAO}.
     */
    private final BAuthorizationDAO bAuthorizationDAO;

    /**
     * Creates a new instance of {@link BAuthorizationRepositoryCustom} (specifically a new
     * BAuthorizationRepositoryImpl).
     *
     * @param daoProvider An instance of a {@link DAOProvider}.
     */
    public BAuthorizationRepositoryImpl(final DAOProvider daoProvider) {
        this.bAuthorizationDAO = daoProvider.getBAuthorizationDAO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not intended to be overridden. If a custom implementation is required, consider implementing
     * {@link BAuthorizationRepositoryCustom}
     */
    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(final long userId) {
        return bAuthorizationDAO.getRolesForUserOverAllEntities(userId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not intended to be overridden. If a custom implementation is required, consider implementing
     * {@link BAuthorizationRepositoryCustom}
     */
    @Override
    public List<BRole> getRolesForUserOverEntity(final long userId, final long entityId) {
        return bAuthorizationDAO.getRolesForUserOverEntity(userId, entityId);
    }

}
