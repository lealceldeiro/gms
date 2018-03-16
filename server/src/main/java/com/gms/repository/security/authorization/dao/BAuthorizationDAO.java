package com.gms.repository.security.authorization.dao;

import com.gms.domain.security.role.BRole;

import java.util.List;
import java.util.Map;

/**
 * BAuthorizationDAO
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 16, 2018
 */
public interface BAuthorizationDAO {

    Map<String, List<BRole>> getRolesForUserOverAllEntities(long userId);
    List<BRole> getRolesForUserOverEntity(long userId, long entityId);
}
