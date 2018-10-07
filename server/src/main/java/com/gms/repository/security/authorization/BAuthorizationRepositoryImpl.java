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

    private final BAuthorizationDAO bAuthorizationDAO;

    public BAuthorizationRepositoryImpl(DAOProvider daoProvider) {
        this.bAuthorizationDAO = daoProvider.getBAuthorizationDAO();
    }

    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId) {
        return bAuthorizationDAO.getRolesForUserOverAllEntities(userId);
    }

    @Override
    public List<BRole> getRolesForUserOverEntity(long userId, long entityId) {
        return bAuthorizationDAO.getRolesForUserOverEntity(userId, entityId);
    }

}
