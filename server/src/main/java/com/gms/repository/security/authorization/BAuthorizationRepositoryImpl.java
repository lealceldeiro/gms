package com.gms.repository.security.authorization;

import com.gms.domain.security.role.BRole;
import com.gms.service.db.QueryService;
import lombok.RequiredArgsConstructor;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final QueryService queryService;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId) {
        Map<String, List<BRole>> r = new HashMap<>();
        final Query queryOE = queryService.createNativeQuery("SELECT e.username FROM eowned_entity e INNER JOIN" +
                " bauthorization auth ON e.id = auth.entity_id WHERE auth.user_id = :userId");
        queryOE.setParameter("userId", userId);
        List<String> oEntitiesUsernames = queryOE.getResultList();

        Query queryR;
        List<Object[]> result;
        List<BRole> rl;
        BRole role;
        for (String OEUsername : oEntitiesUsernames) {
            queryR = queryService.createNativeQuery("SELECT r.id, r.version, r.label, r.enabled FROM brole r" +
                    " INNER JOIN bauthorization auth ON r.id = auth.role_id INNER JOIN eowned_entity e ON" +
                    " auth.entity_id = e.id WHERE e.username = :eUsername AND auth.user_id = :userId");
            queryR.setParameter("eUsername", OEUsername).setParameter("userId", userId);
            result = queryR.getResultList();
            rl = new ArrayList<>(result.size());
            for (Object[] roleValues : result) {
                role = new BRole(roleValues[2].toString());
                role.setId(Long.valueOf(roleValues[0].toString()));
                role.setVersion(Integer.valueOf(roleValues[1].toString()));
                role.setEnabled(Boolean.valueOf(roleValues[3].toString()));
                rl.add(role);
            }
            r.put(OEUsername, rl);
        }
        return r;
    }
}
