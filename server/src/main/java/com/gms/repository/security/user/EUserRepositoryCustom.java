package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;

/**
 * EUserRepositoryCustom
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Feb 12, 2018
 */
public interface EUserRepositoryCustom {

    <S extends EUser> S save(S s);

    <S extends EUser> Iterable<S> save(Iterable<S> it);
}
