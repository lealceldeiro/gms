package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
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
@RepositoryRestResource(collectionResourceRel = ResourcePath.USER, path = ResourcePath.USER)
public interface EUserRepository extends EUserRepositoryCustom, PagingAndSortingRepository<EUser, Long> {

    String value = ResourcePath.QUERY_VALUE;

    @RestResource(exported = false)
    EUser findFirstByUsernameOrEmail(String username, String email);

    @RestResource(exported = false)
    EUser findFirstByUsername(String username);

    @Override
    <S extends EUser> S save(S s);

    @Override
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

    @RestResource(path = ResourcePath.USER_SEARCH_USERNAME, rel = ResourcePath.USER_SEARCH_USERNAME)
    Page<EUser> findByUsernameEquals(@Param(value) String username, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_EMAIL, rel = ResourcePath.USER_SEARCH_EMAIL)
    Page<EUser> findByEmailEquals(@Param(value) String email, Pageable pageable);
    @RestResource(path = ResourcePath.USER_SEARCH_NAME, rel = ResourcePath.USER_SEARCH_NAME)
    Page<EUser> findByNameEquals(@Param(value) String name, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_LASTNAME, rel = ResourcePath.USER_SEARCH_LASTNAME)
    Page<EUser> findByLastNameEquals(@Param(value) String lastName, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_USERNAME_LIKE, rel = ResourcePath.USER_SEARCH_USERNAME_LIKE)
    Page<EUser> findByUsernameContainsIgnoreCase( @Param(value) String username, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_EMAIL_LIKE, rel = ResourcePath.USER_SEARCH_EMAIL_LIKE)
    Page<EUser> findByEmailContainsIgnoreCase(@Param(value) String email, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_NAME_LIKE, rel = ResourcePath.USER_SEARCH_NAME_LIKE)
    Page<EUser> findByNameContainsIgnoreCase(@Param(value) String name, Pageable pageable);

    @RestResource(path = ResourcePath.USER_SEARCH_LASTNAME_LIKE, rel = ResourcePath.USER_SEARCH_LASTNAME_LIKE)
    Page<EUser> findByLastNameContainsIgnoreCase( @Param(value) String lastName, Pageable pageable);
}
