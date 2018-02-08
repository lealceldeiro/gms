package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.util.constant.Resource;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * BPermissionRepository
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = Resource.PERMISSION_PATH, path = Resource.PERMISSION_PATH)
public interface BPermissionRepository extends PagingAndSortingRepository<BPermission, Long> {
}
