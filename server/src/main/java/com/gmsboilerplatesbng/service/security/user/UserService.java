package com.gmsboilerplatesbng.service.security.user;

import com.gmsboilerplatesbng.domain.security.BAuthorization;
import com.gmsboilerplatesbng.domain.security.BAuthorization.BAuthorizationPk;
import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.exception.GmsGeneralException;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.repository.security.BAuthorizationRepository;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.repository.security.user.EUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    public Boolean addRolesToUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException,
            GmsGeneralException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, true);
    }

    public Boolean removeRolesFromUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException,
            GmsGeneralException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, false);
    }

    private Boolean addRemoveRolesToFromUser (Long userId, Long entityId, List<Long> rolesId, Boolean add)
            throws NotFoundEntityException, GmsGeneralException {
        int notFoundRoles = 0;

        BAuthorizationPk pk;
        BAuthorization newUserAuth;

        EUser u = userRepository.findOne(userId);
        if(u == null) throw new NotFoundEntityException("user.not.found");

        EOwnedEntity e = entityRepository.findOne(entityId);
        if (e == null) throw new NotFoundEntityException("entity.not.found");
        BRole r;

        for (Long iRoleId : rolesId) {
            r = roleRepository.findOne(iRoleId);
            if (r == null) {
                notFoundRoles++;
            } else {
                pk = new BAuthorizationPk();
                pk.setEntityId(e.getId());
                pk.setUserId(u.getId());
                pk.setRoleId(r.getId());

                newUserAuth = new BAuthorization();
                newUserAuth.setBAuthorizationPk(pk);
                newUserAuth.setUser(u);
                newUserAuth.setRole(r);
                newUserAuth.setEntity(e);
                if(add) {
                    authorizationRepository.save(newUserAuth);
                }
                else {
                    authorizationRepository.delete(newUserAuth);
                }
            }
        }
        //many roles (at least one not found
        if(notFoundRoles > 1) {
            String msg = notFoundRoles == rolesId.size() ? "user.roles.found.none"  //none of them found
                    : "user.roles.found.not.some";                                  //some not found
            throw new GmsGeneralException(msg, true);
        }
        else if (notFoundRoles > 0) throw new NotFoundEntityException("role.not.found");

        return true;
    }
}
