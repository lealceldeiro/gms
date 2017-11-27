package com.gmsboilerplatesbng.repository.security.user;

import com.gmsboilerplatesbng.domain.security.user.EUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface EUserRepository extends PagingAndSortingRepository<EUser, Long> {

    EUser findFirstByUsernameOrEmail();
}
