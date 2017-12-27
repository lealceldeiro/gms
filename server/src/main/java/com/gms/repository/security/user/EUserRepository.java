package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * EUserRepository
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface EUserRepository extends PagingAndSortingRepository<EUser, Long> {

    EUser findFirstByUsernameOrEmail(String username, String email);
}
