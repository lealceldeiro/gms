package com.gms.repository.security.authorization;

import com.gms.domain.security.role.BRole;
import com.gms.repository.security.authorization.dao.BAuthorizationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * BAuthorizationRepositoryImpl
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 13, 2018
 */
@RequiredArgsConstructor
@Transactional
public class BAuthorizationRepositoryImpl implements BAuthorizationRepositoryCustom {

    @Qualifier("PostgreSQLBAuthorizationDAOImpl")
    private final BAuthorizationDAO bAuthorizationDAO;

    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId) {
        return bAuthorizationDAO.getRolesForUserOverAllEntities(userId);
    }

    @Override
    public List<BRole> getRolesForUserOverEntity(long userId, long entityId) {
        return bAuthorizationDAO.getRolesForUserOverEntity(userId, entityId);
    }
}
