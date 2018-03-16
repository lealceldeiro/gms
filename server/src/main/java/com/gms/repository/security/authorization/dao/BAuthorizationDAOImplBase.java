package com.gms.repository.security.authorization.dao;

import com.gms.domain.security.role.BRole;

import java.util.List;

/**
 * BAuthorizationDAOImplBase
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 16, 2018
 */
public abstract class BAuthorizationDAOImplBase {

    static final String USER_ID_PARAM = "userId";

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
