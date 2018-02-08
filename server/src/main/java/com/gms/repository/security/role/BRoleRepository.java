package com.gms.repository.security.role;

import com.gms.domain.security.role.BRole;
import com.gms.util.constant.Resource;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * BRoleRepository
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = Resource.ROLE_PATH, path = Resource.ROLE_PATH)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    BRole findFirstByLabel(String label);
}
