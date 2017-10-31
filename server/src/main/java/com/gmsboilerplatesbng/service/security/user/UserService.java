package com.gmsboilerplatesbng.service.security.user;

import com.gmsboilerplatesbng.domain.secuirty.BAuthorization;
import com.gmsboilerplatesbng.domain.secuirty.BAuthorization.BAuthorizationPk;
import com.gmsboilerplatesbng.domain.secuirty.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.secuirty.role.BRole;
import com.gmsboilerplatesbng.domain.secuirty.user.EUser;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.repository.security.BAuthorizationRepository;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.repository.security.user.EUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    final private EUserRepository userRepository;

    final private EOwnedEntityRepository entityRepository;

    final private BRoleRepository roleRepository;

    final private BAuthorizationRepository authorizationRepository;

    @Autowired
    public UserService(EUserRepository userRepository, EOwnedEntityRepository entityRepository,
                       BRoleRepository roleRepository, BAuthorizationRepository authorizationRepository) {
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authorizationRepository = authorizationRepository;
    }


    public Boolean addRolesToUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException {
        boolean roleNotFound = false;

        BAuthorizationPk pk;
        BAuthorization auth = null;

        EUser u = userRepository.findOne(userId);
        if(u == null) throw new NotFoundEntityException("User not found");

        EOwnedEntity e = entityRepository.findOne(entityId);
        if (e == null) throw new NotFoundEntityException("Entity not found");
        BRole r;

        for (Long iRoleId : rolesId) {
            r = roleRepository.findOne(iRoleId);
            if (r == null) {
                roleNotFound = true;
            } else {
                pk = new BAuthorizationPk();
                pk.setEntityId(e.getId());
                pk.setUserId(u.getId());
                pk.setRoleId(r.getId());

                auth = new BAuthorization();
                auth.setBAuthorizationPk(pk);
                auth.setUser(u);
                auth.setRole(r);
                auth.setEntity(e);
            }
        }

        if (auth != null) authorizationRepository.save(auth);
        else //TODO: throw generic exception here

        if(roleNotFound) {
            // TODO: notify some roles id were incorrect
        }
        return true;
    }
}
