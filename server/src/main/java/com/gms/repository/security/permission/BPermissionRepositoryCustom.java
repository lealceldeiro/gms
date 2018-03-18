package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface BPermissionRepositoryCustom {

    List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);
}
