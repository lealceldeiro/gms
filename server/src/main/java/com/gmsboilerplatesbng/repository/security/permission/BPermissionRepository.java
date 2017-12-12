package com.gmsboilerplatesbng.repository.security.permission;

import com.gmsboilerplatesbng.domain.security.permission.BPermission;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * BPermissionRepository
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = "permission", path = "permission")
public interface BPermissionRepository extends PagingAndSortingRepository<BPermission, Long> {
}
