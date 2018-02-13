package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;

import java.util.List;

/**
 * BPermissionRepositoryCustom
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Feb 13, 2018
 */
public interface BPermissionRepositoryCustom {

    List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);
}
