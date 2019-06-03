package com.gms.repository.security.role;

import com.gms.domain.security.role.BRole;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.QUERY_VALUE;
import static com.gms.util.constant.ResourcePath.ROLE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = ROLE, path = ROLE)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    @RestResource(exported = false)
    BRole findFirstByLabel(String label);

    @RestResource(path = ResourcePath.LABEL_LIKE, rel = ResourcePath.LABEL_LIKE)
    Page<BRole> findByLabelContainsIgnoreCase(@Param(QUERY_VALUE) String label, Pageable pageable);

    @RestResource(path = ResourcePath.LABEL, rel = ResourcePath.LABEL)
    Page<BRole> findByLabelEquals(@Param(QUERY_VALUE) String label, Pageable pageable);

}
