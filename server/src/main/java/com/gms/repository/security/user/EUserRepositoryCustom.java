package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public interface EUserRepositoryCustom {

    <S extends EUser> S save(S s);

    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);

}
