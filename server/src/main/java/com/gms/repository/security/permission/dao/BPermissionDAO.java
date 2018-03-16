package com.gms.repository.security.permission.dao;

import com.gms.domain.security.permission.BPermission;

import java.util.List;

/**
 * BPermissionDAO
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 16, 2018
 */
public interface BPermissionDAO {

    List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId);

}
