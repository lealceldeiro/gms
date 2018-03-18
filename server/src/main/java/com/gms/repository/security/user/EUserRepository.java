package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import com.gms.util.constant.ResourcePath;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestResource(collectionResourceRel = ResourcePath.USER, path = ResourcePath.USER)
public interface EUserRepository extends EUserRepositoryCustom, PagingAndSortingRepository<EUser, Long> {

    EUser findFirstByUsernameOrEmail(String username, String email);

    EUser findFirstByUsername(String username);

    @Override
    <S extends EUser> S save(S s);

    @Override
    <S extends EUser> Iterable<S> saveAll(Iterable<S> it);
}
