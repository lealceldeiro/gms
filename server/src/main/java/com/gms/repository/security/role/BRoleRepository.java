package com.gms.repository.security.role;

import com.gms.domain.security.role.BRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@SuppressWarnings("unused")
@RepositoryRestResource(collectionResourceRel = ROLE, path = ROLE)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    @RestResource(exported = false)
    BRole findFirstByLabel(String label);

    @RestResource(path = ROLE_SEARCH_LABEL_LIKE, rel = ROLE_SEARCH_LABEL_LIKE)
    Page<BRole> findByLabelContainsIgnoreCase(@Param(QUERY_VALUE) String label, Pageable pageable);

    @RestResource(path = ROLE_SEARCH_LABEL, rel = ROLE_SEARCH_LABEL)
    Page<BRole> findByLabelEquals(@Param(QUERY_VALUE) String label, Pageable pageable);

}
