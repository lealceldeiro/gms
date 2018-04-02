package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.QUERY_VALUE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@SuppressWarnings("unused")
@RepositoryRestResource(collectionResourceRel = ResourcePath.PERMISSION, path = ResourcePath.PERMISSION)
public interface BPermissionRepository extends PagingAndSortingRepository<BPermission, Long>, BPermissionRepositoryCustom {

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

    @RestResource(exported = false)
    BPermission findFirstByName(String name);

    @RestResource(
            path = ResourcePath.PERMISSION_SEARCH_NAME_LABEL_LIKE,
            rel =  ResourcePath.PERMISSION_SEARCH_NAME_LABEL_LIKE
    )
    Page<BPermission> findByNameContainsIgnoreCaseOrLabelContainsIgnoreCase(
            @Param(QUERY_VALUE) String name, @Param(QUERY_VALUE) String label, Pageable pageable
    );

    @RestResource(path = ResourcePath.PERMISSION_SEARCH_NAME_LIKE, rel = ResourcePath.PERMISSION_SEARCH_NAME_LIKE)
    Page<BPermission> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    @RestResource(path = ResourcePath.PERMISSION_SEARCH_NAME, rel = ResourcePath.PERMISSION_SEARCH_NAME)
    Page<BPermission> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = ResourcePath.PERMISSION_SEARCH_LABEL_LIKE, rel = ResourcePath.PERMISSION_SEARCH_LABEL_LIKE)
    Page<BPermission> findByLabelContainsIgnoreCase(@Param(QUERY_VALUE) String like, Pageable pageable);

    @RestResource(path = ResourcePath.PERMISSION_SEARCH_LABEL, rel = ResourcePath.PERMISSION_SEARCH_LABEL)
    Page<BPermission> findByLabelEquals(@Param(QUERY_VALUE) String label, Pageable pageable);

}
