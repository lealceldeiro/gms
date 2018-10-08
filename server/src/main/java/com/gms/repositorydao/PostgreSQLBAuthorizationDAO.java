package com.gms.repositorydao;

import com.gms.domain.security.role.BRole;
import com.gms.service.db.QueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In order to get an instance of this class, you must call {@link DAOProvider#getBAuthorizationDAO()}.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * @see DAOProvider
 */
@Repository
@Transactional
class PostgreSQLBAuthorizationDAO extends BAuthorizationDAO {

    private static final String USER_ID_PARAM = "userId";

    PostgreSQLBAuthorizationDAO(QueryService queryService) {
        this.queryService = queryService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId) {
        Map<String, List<BRole>> r = new HashMap<>();
        final Query queryOE = queryService.createNativeQuery("SELECT e.username FROM eowned_entity e INNER JOIN" +
                " bauthorization auth ON e.id = auth.entity_id WHERE auth.user_id = :userId");
        queryOE.setParameter(USER_ID_PARAM, userId);
        List<String> oEntitiesUsernames = queryOE.getResultList();

        Query queryR;
        List<Object[]> result;
        for (String OEUsername : oEntitiesUsernames) {
            queryR = queryService.createNativeQuery("SELECT r.id, r.version, r.label, r.description, r.enabled" +
                    " FROM brole r INNER JOIN bauthorization auth ON r.id = auth.role_id INNER JOIN eowned_entity e ON" +
                    " auth.entity_id = e.id WHERE e.username = :eUsername AND auth.user_id = :userId");
            queryR.setParameter("eUsername", OEUsername).setParameter(USER_ID_PARAM, userId);
            result = queryR.getResultList();
            r.put(OEUsername, getRolesListFrom(result));
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BRole> getRolesForUserOverEntity(long userId, long entityId) {
        Query queryR = queryService.createNativeQuery("SELECT r.id, r.version, r.label, r.description, r.enabled" +
                " FROM brole r INNER JOIN bauthorization auth ON r.id = auth.role_id INNER JOIN eowned_entity e ON" +
                " auth.entity_id = e.id WHERE e.id = :entityId AND auth.user_id = :userId");
        queryR.setParameter("entityId", entityId).setParameter(USER_ID_PARAM, userId);
        List<Object[]> rawVal = queryR.getResultList();
        return getRolesListFrom(rawVal);
    }

}
