package com.gms.repository.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

import static com.gms.util.constant.ResourcePath.QUERY_VALUE;
import static com.gms.util.constant.ResourcePath.ROLE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = ROLE, path = ROLE)
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {

    /**
     * Returns all {@link BRole}s with label equals to the provided arguments.
     *
     * @param label Label to be matched against to.
     * @return A {@link BRole}
     */
    @RestResource(exported = false)
    BRole findFirstByLabel(String label);

    /**
     * Returns all {@link BRole}s with a label like the provided argument.
     *
     * @param label    Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BRole}
     */
    @RestResource(path = ResourcePath.LABEL_LIKE, rel = ResourcePath.LABEL_LIKE)
    Page<BRole> findByLabelContainsIgnoreCase(@Param(QUERY_VALUE) String label, Pageable pageable);

    /**
     * Returns all {@link BRole}s with a label equals to the provided argument.
     *
     * @param label    Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BRole}
     */
    @RestResource(path = ResourcePath.LABEL, rel = ResourcePath.LABEL)
    Page<BRole> findByLabelEquals(@Param(QUERY_VALUE) String label, Pageable pageable);

    /**
     * Returns all {@link BRole}s with associated to a given {@link BPermission}s.
     *
     * @param permissions {@link Set} of id of {@link BPermission} to check the associated roles to.
     * @param pageable    A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(exported = false)
    Page<BRole> findAllByPermissions(Set<BPermission> permissions, Pageable pageable);

}
