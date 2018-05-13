package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import com.gms.util.constant.ResourcePath;
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
@RepositoryRestResource(collectionResourceRel = USER, path = USER)
public interface EUserRepository extends EUserRepositoryCustom, PagingAndSortingRepository<EUser, Long> {

    @RestResource(exported = false)
    EUser findFirstByUsernameOrEmail(String username, String email);

    @RestResource(exported = false)
    EUser findFirstByUsername(String username);

    @Override
    <S extends EUser> S save(S s);

    @Override
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

    @RestResource(path = ResourcePath.USERNAME, rel = ResourcePath.USERNAME)
    Page<EUser> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

    @RestResource(path = EMAIL, rel = EMAIL)
    Page<EUser> findByEmailEquals(@Param(QUERY_VALUE) String email, Pageable pageable);

    @RestResource(path = ResourcePath.NAME, rel = ResourcePath.NAME)
    Page<EUser> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = LASTNAME, rel = LASTNAME)
    Page<EUser> findByLastNameEquals(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    @RestResource(path = ResourcePath.USERNAME_LIKE, rel = ResourcePath.USERNAME_LIKE)
    Page<EUser> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String username, Pageable pageable);

    @RestResource(path = USER_SEARCH_EMAIL_LIKE, rel = USER_SEARCH_EMAIL_LIKE)
    Page<EUser> findByEmailContainsIgnoreCase(@Param(QUERY_VALUE) String email, Pageable pageable);

    @RestResource(path = ResourcePath.NAME_LIKE, rel = ResourcePath.NAME_LIKE)
    Page<EUser> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = USER_SEARCH_LASTNAME_LIKE, rel = USER_SEARCH_LASTNAME_LIKE)
    Page<EUser> findByLastNameContainsIgnoreCase(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    @RestResource(path = ResourcePath.MULTI_LIKE, rel = ResourcePath.MULTI_LIKE)
    Page<EUser>
    findByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(
            @Param(ResourcePath.USERNAME) String username, @Param(ResourcePath.EMAIL) String email, @Param(ResourcePath.NAME) String name,
            @Param(ResourcePath.LASTNAME) String lastName, Pageable pageable
    );

    @RestResource(path = ResourcePath.MULTI, rel = ResourcePath.MULTI)
    Page<EUser> findByUsernameEqualsOrEmailEqualsOrNameEqualsOrLastNameEquals(
            @Param(ResourcePath.USERNAME) String username, @Param(ResourcePath.EMAIL) String email, @Param(ResourcePath.NAME) String name,
            @Param(ResourcePath.LASTNAME) String lastName, Pageable pageable
    );
}
