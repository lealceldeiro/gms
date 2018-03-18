package com.gms.repository.security.role;

import com.gms.domain.security.role.BRole;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = ResourcePath.ROLE, path = ResourcePath.ROLE)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    BRole findFirstByLabel(String label);
}
