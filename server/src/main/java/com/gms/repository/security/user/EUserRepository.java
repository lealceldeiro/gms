package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
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

    @RestResource(path = USERNAME_EMAIL, rel = USERNAME_EMAIL)
    Page<EUser> findFirstByUsernameOrEmail(@Param(USERNAME) String username, @Param(EMAIL) String email, Pageable pageable);

    @RestResource(exported = false)
    EUser findFirstByUsername(String username);

    @Override
    <S extends EUser> S save(S s);

    @Override
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

    @RestResource(path = USERNAME, rel = USERNAME)
    Page<EUser> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

    @RestResource(path = EMAIL, rel = EMAIL)
    Page<EUser> findByEmailEquals(@Param(QUERY_VALUE) String email, Pageable pageable);

    @RestResource(path = NAME, rel = NAME)
    Page<EUser> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = LASTNAME, rel = LASTNAME)
    Page<EUser> findByLastNameEquals(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    @RestResource(path = USERNAME_LIKE, rel = USERNAME_LIKE)
    Page<EUser> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String username, Pageable pageable);

    @RestResource(path = USER_SEARCH_EMAIL_LIKE, rel = USER_SEARCH_EMAIL_LIKE)
    Page<EUser> findByEmailContainsIgnoreCase(@Param(QUERY_VALUE) String email, Pageable pageable);

    @RestResource(path = NAME_LIKE, rel = NAME_LIKE)
    Page<EUser> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String name, Pageable pageable);

    @RestResource(path = USER_SEARCH_LASTNAME_LIKE, rel = USER_SEARCH_LASTNAME_LIKE)
    Page<EUser> findByLastNameContainsIgnoreCase(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    @RestResource(path = MULTI_LIKE, rel = MULTI_LIKE)
    Page<EUser>
    findByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(
            @Param(USERNAME) String username, @Param(EMAIL) String email, @Param(NAME) String name,
            @Param(LASTNAME) String lastName, Pageable pageable
    );

    @RestResource(path = MULTI, rel = MULTI)
    Page<EUser> findByUsernameEqualsOrEmailEqualsOrNameEqualsOrLastNameEquals(
            @Param(USERNAME) String username, @Param(EMAIL) String email, @Param(NAME) String name,
            @Param(LASTNAME) String lastName, Pageable pageable
    );

}
