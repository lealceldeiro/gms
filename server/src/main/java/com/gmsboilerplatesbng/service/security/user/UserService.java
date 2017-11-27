package com.gmsboilerplatesbng.service.security.user;

import com.gmsboilerplatesbng.domain.security.BAuthorization;
import com.gmsboilerplatesbng.domain.security.BAuthorization.BAuthorizationPk;
import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.util.exception.GmsGeneralException;
import com.gmsboilerplatesbng.util.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.repository.security.BAuthorizationRepository;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.repository.security.user.EUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService{

    @Value("${default.gmsuser.name}")
    private String defaultUserName = "Admin";

    @Value("${default.gmsuser.lastName}")
    private String defaultUserLastName = "Default";

    @Value("${default.gmsuser.username}")
    private String defaultUserUsername = "admin";

    @Value("${default.gmsuser.password}")
    private String defaultUserPassword = "admin";

    @Value("${default.gmsuser.email}")
    private String defaultUserEmail = "admin@example.com";

    private final EUserRepository userRepository;

    private final EOwnedEntityRepository entityRepository;

    private final BRoleRepository roleRepository;

    private final BAuthorizationRepository authorizationRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(EUserRepository userRepository, EOwnedEntityRepository entityRepository,
                       BRoleRepository roleRepository, BAuthorizationRepository authorizationRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authorizationRepository = authorizationRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    //region default user
    public EUser createDefaultUser() {
        EUser u = new EUser(this.defaultUserUsername, this.defaultUserEmail, this.defaultUserName,
                this.defaultUserLastName, this.defaultUserPassword);
        return signUp(u, true, true);
    }
    //endregion

    public EUser signUp(EUser u) { return signUp(u, false); }

    public EUser signUp(EUser u, Boolean enabled) { return signUp(u, enabled, false); }

    public EUser signUp(EUser u, Boolean enabled, Boolean emailVerified) {
        EUser sU = new EUser(u.getUsername(), u.getEmail(), u.getName(), u.getLastName(),
                this.passwordEncoder.encode(u.getPassword()));
        sU.setEnabled(enabled);
        sU.setEmailVerified(emailVerified);
        return this.userRepository.save(sU);
    }

    public ArrayList<Long> addRolesToUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException,
            GmsGeneralException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, true);
    }

    public ArrayList<Long> removeRolesFromUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException,
            GmsGeneralException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, false);
    }

    private ArrayList<Long> addRemoveRolesToFromUser (Long userId, Long entityId, List<Long> rolesId, Boolean add)
            throws NotFoundEntityException, GmsGeneralException {
        ArrayList<Long> addedOrRemoved = new ArrayList<>();

        BAuthorizationPk pk;
        BAuthorization newUserAuth;

        EUser u = userRepository.findOne(userId);
        if(u == null) throw new NotFoundEntityException("user.not.found");

        EOwnedEntity e = entityRepository.findOne(entityId);
        if (e == null) throw new NotFoundEntityException("entity.not.found");
        BRole r;

        for (Long iRoleId : rolesId) {
            r = roleRepository.findOne(iRoleId);
            if (r != null) {
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
                addedOrRemoved.add(iRoleId);
            }
        }
        //none of the roles was found
        if (addedOrRemoved.isEmpty()) {
            throw new NotFoundEntityException("user.add.roles.found.none");
        }

        return addedOrRemoved;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return this.userRepository.findFirstByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }
}
