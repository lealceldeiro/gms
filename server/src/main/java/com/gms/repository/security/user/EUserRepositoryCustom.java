package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface EUserRepositoryCustom {


    /**
     * Saves a {@link EUser} instance to database.
     *
     * @param s   {@link EUser} to be saved.
     * @param <S> Type which is a subtype of {@link EUser}.
     * @return The entity persisted in database.
     */
    <S extends EUser> S save(S s);

    /**
     * Saves an {@link Iterable} of {@link EUser} instances to database.
     *
     * @param it  {@link Iterable} of {@link EUser}s to be saved.
     * @param <S> Type which is a subtype of {@link EUser}.
     * @return The entities persisted in database as {@link Iterable} of S.
     */
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

}
