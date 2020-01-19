package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static com.gms.util.constant.ResourcePath.EMAIL;
import static com.gms.util.constant.ResourcePath.LASTNAME;
import static com.gms.util.constant.ResourcePath.MULTI;
import static com.gms.util.constant.ResourcePath.MULTI_LIKE;
import static com.gms.util.constant.ResourcePath.NAME;
import static com.gms.util.constant.ResourcePath.NAME_LIKE;
import static com.gms.util.constant.ResourcePath.QUERY_VALUE;
import static com.gms.util.constant.ResourcePath.USER;
import static com.gms.util.constant.ResourcePath.USERNAME;
import static com.gms.util.constant.ResourcePath.USERNAME_EMAIL;
import static com.gms.util.constant.ResourcePath.USERNAME_LIKE;
import static com.gms.util.constant.ResourcePath.USER_SEARCH_EMAIL_LIKE;
import static com.gms.util.constant.ResourcePath.USER_SEARCH_LASTNAME_LIKE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = USER, path = USER)
public interface EUserRepository extends EUserRepositoryCustom, PagingAndSortingRepository<EUser, Long> {

    /**
     * Returns the first {@link EUser}s with a username or email equals to the provided arguments. This method will
     * always return the same instnace for a given combination of arguments since the username and email are unique in
     * the {@link EUser} entity.
     *
     * @param username Username to be matched against to.
     * @param email    Email to be matched against to.
     * @return An {@link EUser}
     */
    @RestResource(exported = false)
    EUser findFirstByUsernameOrEmail(String username, String email);

    /**
     * Returns all {@link EUser}s with a username or email equals to the provided arguments.
     *
     * @param username Username to be matched against to.
     * @param email    Email to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = USERNAME_EMAIL, rel = USERNAME_EMAIL)
    Page<EUser> findFirstByUsernameOrEmail(@Param(USERNAME) String username, @Param(EMAIL) String email,
                                           Pageable pageable);

    /**
     * Returns the first {@link EUser}s with a username equals to the provided argument. This method will always return
     * the same instance for a given username as argument since he username attribute is unique for the entity
     * {@link EUser}.
     *
     * @param username Username to be matched against to.
     * @return A {@link EUser}
     */
    @RestResource(exported = false)
    EUser findFirstByUsername(String username);

    /**
     * Saves a {@link EUser} instance to database.
     *
     * @param s   {@link EUser} to be saved.
     * @param <S> Type which is a subtype of {@link EUser}.
     * @return The entity persisted in database.
     */
    @Override
    <S extends EUser> S save(S s);

    /**
     * Saves an {@link Iterable} of {@link EUser} instances to database.
     *
     * @param it  {@link Iterable} of {@link EUser}s to be saved.
     * @param <S> Type which is a subtype of {@link EUser}.
     * @return The entities persisted in database as {@link Iterable} of S.
     */
    @Override
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

    /**
     * Returns all {@link EUser}s with a username equals to the provided argument.
     *
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = USERNAME, rel = USERNAME)
    Page<EUser> findByUsernameEquals(@Param(QUERY_VALUE) String username, Pageable pageable);

    /**
     * Returns all {@link EUser}s with an email equals to the provided argument.
     *
     * @param email    Email to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = EMAIL, rel = EMAIL)
    Page<EUser> findByEmailEquals(@Param(QUERY_VALUE) String email, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a name equals to the provided argument.
     *
     * @param name     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = NAME, rel = NAME)
    Page<EUser> findByNameEquals(@Param(QUERY_VALUE) String name, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a last name equals to the provided argument.
     *
     * @param lastName Last name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = LASTNAME, rel = LASTNAME)
    Page<EUser> findByLastNameEquals(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a username like the provided argument.
     *
     * @param username Username to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = USERNAME_LIKE, rel = USERNAME_LIKE)
    Page<EUser> findByUsernameContainsIgnoreCase(@Param(QUERY_VALUE) String username, Pageable pageable);

    /**
     * Returns all {@link EUser}s with an email like the provided argument.
     *
     * @param email    Email to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = USER_SEARCH_EMAIL_LIKE, rel = USER_SEARCH_EMAIL_LIKE)
    Page<EUser> findByEmailContainsIgnoreCase(@Param(QUERY_VALUE) String email, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a name like the provided argument.
     *
     * @param name     Name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = NAME_LIKE, rel = NAME_LIKE)
    Page<EUser> findByNameContainsIgnoreCase(@Param(QUERY_VALUE) String name, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a last name like the provided argument.
     *
     * @param lastName Last name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = USER_SEARCH_LASTNAME_LIKE, rel = USER_SEARCH_LASTNAME_LIKE)
    Page<EUser> findByLastNameContainsIgnoreCase(@Param(QUERY_VALUE) String lastName, Pageable pageable);

    /**
     * Returns all {@link EUser}s with a username, email, name or last name like the provided arguments.
     *
     * @param username Username to be matched against to.
     * @param email    Email to be matched against to.
     * @param name     Name to be matched against to.
     * @param lastName Last name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = MULTI_LIKE, rel = MULTI_LIKE)
    Page<EUser>
    findByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(
            @Param(USERNAME) String username, @Param(EMAIL) String email, @Param(NAME) String name,
            @Param(LASTNAME) String lastName, Pageable pageable
    );

    /**
     * Returns all {@link EUser}s with a username, email, name or last name equals to the provided arguments.
     *
     * @param username Username to be matched against to.
     * @param email    Email to be matched against to.
     * @param name     Name to be matched against to.
     * @param lastName Last name to be matched against to.
     * @param pageable A {@link Pageable} attribute from the Spring framework.
     * @return A {@link Page} of {@link EUser}
     */
    @RestResource(path = MULTI, rel = MULTI)
    Page<EUser> findByUsernameEqualsOrEmailEqualsOrNameEqualsOrLastNameEquals(
            @Param(USERNAME) String username, @Param(EMAIL) String email, @Param(NAME) String name,
            @Param(LASTNAME) String lastName, Pageable pageable
    );

}
