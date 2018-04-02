package com.gms.repository.security.role;

import com.gms.domain.security.role.BRole;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@SuppressWarnings("unused")
@RepositoryRestResource(collectionResourceRel = ResourcePath.ROLE, path = ResourcePath.ROLE)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    String value = ResourcePath.QUERY_VALUE;

    @RestResource(exported = false)
    BRole findFirstByLabel(String label);

    @RestResource(path = ResourcePath.ROLE_SEARCH_LABEL_LIKE, rel = ResourcePath.ROLE_SEARCH_LABEL_LIKE)
    Page<BRole> findByLabelContainsIgnoreCase(@Param(value) String label, Pageable pageable);

    @RestResource(path = ResourcePath.ROLE_SEARCH_LABEL, rel = ResourcePath.ROLE_SEARCH_LABEL)
    Page<BRole> findByLabelEquals(@Param(value) String label, Pageable pageable);

}
