package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

import static com.gms.util.constant.ResourcePath.LABEL;
import static com.gms.util.constant.ResourcePath.LABEL_LIKE;
import static com.gms.util.constant.ResourcePath.MULTI;
import static com.gms.util.constant.ResourcePath.MULTI_LIKE;
import static com.gms.util.constant.ResourcePath.NAME;
import static com.gms.util.constant.ResourcePath.NAME_LIKE;
import static com.gms.util.constant.ResourcePath.PERMISSION;
import static com.gms.util.constant.ResourcePath.QUERY_VALUE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = PERMISSION, path = PERMISSION)
public interface BPermissionRepository extends PagingAndSortingRepository<BPermission, Long>,
        BPermissionRepositoryCustom {

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(BPermission permission);

    @Override
    @RestResource(exported = false)
    <S extends BPermission> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends BPermission> Iterable<S> saveAll(Iterable<S> it);

    /**
     * Returns the first {@link BPermission} whose username is equals to the provided argument. This method guarantees
     * to return always the same instance since the property name is unique in {@link BPermission}.
     *
     * @param name Argument with which to match the permission name.
     * @return A {@link BPermission}
     */
    @RestResource(exported = false)
    BPermission findFirstByName(String name);

    /**
     * Returns all {@link BPermission}s with a name or label equals to the provided arguments.
     *
     * @param name     Name to be matched against to.
     * @param label    Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = MULTI_LIKE, rel = MULTI_LIKE)
    Page<BPermission> findByNameContainsIgnoreCaseOrLabelContainsIgnoreCase(
            @Param(NAME) String name, @Param(LABEL) String label, Pageable pageable
    );

    /**
     * Returns all {@link BPermission}s with a name or label like the provided arguments.
     *
     * @param name     Name to be matched against to.
     * @param label    Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = MULTI, rel = MULTI)
    Page<BPermission> findByNameEqualsOrLabelEquals(
            @Param(NAME) String name, @Param(LABEL) String label, Pageable pageable
    );

    /**
     * Returns all {@link BPermission}s with a name like the provided argument.
     *
     * @param like     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = NAME_LIKE, rel = NAME_LIKE)
    Page<BPermission> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    /**
     * Returns all {@link BPermission}s with a name equals to the provided argument.
     *
     * @param name     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = NAME, rel = NAME)
    Page<BPermission> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    /**
     * Returns all {@link BPermission}s with a label like the provided argument.
     *
     * @param like     Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = LABEL_LIKE, rel = LABEL_LIKE)
    Page<BPermission> findByLabelContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    /**
     * Returns all {@link BPermission}s with a label equals to the provided argument.
     *
     * @param label    Label to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(path = LABEL, rel = LABEL)
    Page<BPermission> findByLabelEquals(@Param(QUERY_VALUE) String label, Pageable pageable);

    /**
     * Returns all {@link BPermission}s with associated to a given {@link BRole}s.
     *
     * @param roles    {@link Set} of id of {@link BRole} to check the associated permissions to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link BPermission}
     */
    @RestResource(exported = false)
    Page<BPermission> findAllByRolesIn(Set<BRole> roles, Pageable pageable);

}
