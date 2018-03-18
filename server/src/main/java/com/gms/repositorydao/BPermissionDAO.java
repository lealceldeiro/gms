package com.gms.repositorydao;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.db.QueryService;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public abstract class BPermissionDAO {

    QueryService queryService;

    public abstract List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);

}
