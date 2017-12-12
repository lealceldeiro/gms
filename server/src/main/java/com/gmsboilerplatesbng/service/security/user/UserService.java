package com.gmsboilerplatesbng.service.security.user;

import com.gmsboilerplatesbng.domain.security.BAuthorization;
import com.gmsboilerplatesbng.domain.security.BAuthorization.BAuthorizationPk;
import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.permission.BPermission;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.repository.security.BAuthorizationRepository;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.repository.security.user.EUserRepository;
import com.gmsboilerplatesbng.service.configuration.ConfigurationService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.exception.GmsGeneralException;
import com.gmsboilerplatesbng.util.exception.domain.NotFoundEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * UserService
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
public class UserService implements UserDetailsService{

    private final DefaultConst c;

    private final EUserRepository userRepository;

    private final EOwnedEntityRepository entityRepository;

    private final BRoleRepository roleRepository;

    private final BAuthorizationRepository authorizationRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ConfigurationService configService;

    @Autowired
    public UserService(EUserRepository userRepository, EOwnedEntityRepository entityRepository,
                       BRoleRepository roleRepository, BAuthorizationRepository authorizationRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, ConfigurationService configService,
                       DefaultConst defaultConst) {
        this.userRepository = userRepository;
        this.entityRepository = entityRepository;
        this.roleRepository = roleRepository;
        this.authorizationRepository = authorizationRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.configService = configService;
        this.c = defaultConst;
    }

    //region default user
    public EUser createDefaultUser() {
        EUser u = new EUser(c.USER_USERNAME, c.USER_EMAIL, c.USER_NAME, c.USER_LAST_NAME,
                c.USER_PASSWORD);
        u.setEnabled(true);
        return signUp(u, true);
    }
    //endregion

    public EUser signUp(EUser u, Boolean emailVerified) {
        EUser sU = new EUser(u.getUsername(), u.getEmail(), u.getName(), u.getLastName(),
                passwordEncoder.encode(u.getPassword()));
        sU.setEnabled(u.isEnabled());
        sU.setEmailVerified(emailVerified);
        return userRepository.save(sU);
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
            throws NotFoundEntityException {
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
                if (add) {
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
        EUser u = userRepository.findFirstByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (u == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return u;
    }

    public String getUserAuthoritiesForToken(String usernameOrEmail, String separator) {
        StringBuilder authBuilder = new StringBuilder();
        EUser u = (EUser)loadUserByUsername(usernameOrEmail);
        if (u != null) { // got user
            BAuthorization auth = null;
            Long entityId = configService.getLastAccessedEntityIdByUser(u.getId());
            EOwnedEntity e = null;
            if (entityId != null) {
                e = entityRepository.findOne(entityId);
            }
            if (entityId == null) { // no last accessed entity registered
                auth = authorizationRepository.findFirstByUserAndEntityNotNull(u); // find any of the assigned entities
            }
            if (entityId != null || auth != null) { // got last accessed entity or first of the assigned one to the user
                if (auth == null) { // get authorization if it was not previously gotten
                    auth = authorizationRepository.findFirstByUserAndEntity(u, e);
                }
                if (auth != null) { // got authorization
                    Set<BPermission> permissions = auth.getRole().getPermissions();
                    StringBuilder auxBuilder;
                    for (BPermission p: permissions) {
                        auxBuilder = new StringBuilder();
                        authBuilder.append(auxBuilder.append(p.getName()).append(separator).toString());
                    }
                }
            }
        }
        return authBuilder.toString();
    }
}
