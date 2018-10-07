package com.gms.repositorydao;

import com.gms.domain.security.role.BRole;
import com.gms.service.db.QueryService;

import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public abstract class BAuthorizationDAO {

    QueryService queryService;

    public abstract Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId);
    public abstract List<BRole> getRolesForUserOverEntity(long userId, long entityId);

    void processRolesVal(List<Object[]> rawVal, List<BRole> r) {
        BRole role;
        for (Object[] oVal : rawVal) {
            role = new BRole(oVal[2].toString());
            role.setId(Long.valueOf(oVal[0].toString()));
            role.setVersion(Integer.valueOf(oVal[1].toString()));
            role.setEnabled(Boolean.valueOf(oVal[4].toString()));
            role.setDescription(oVal[3].toString());
            r.add(role);
        }
    }

}
