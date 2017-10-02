package com.gmsboilerplatesbng.repository.security.permission;

import com.gmsboilerplatesbng.domain.secuirty.user.EUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "permission", path = "permission")
public interface BPermissionRepository extends PagingAndSortingRepository<EUser, Long> {
}
