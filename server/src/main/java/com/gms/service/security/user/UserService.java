package com.gms.service.security.user;

import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.BAuthorization.BAuthorizationPk;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.configuration.ConfigurationService;
import com.gms.service.security.permission.PermissionService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final EUserRepository userRepository;
    private final EOwnedEntityRepository entityRepository;
    private final BRoleRepository roleRepository;
    private final BAuthorizationRepository authorizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfigurationService configService;
    private final DefaultConst dc;
    private final MessageResolver msg;
    private final PermissionService permissionService;

    //region default user
    public EUser createDefaultUser() {
        EUser u = new EUser(dc.getUserAdminDefaultUsername(), dc.getUserAdminDefaultEmail(), dc.getUserAdminDefaultName(),
                dc.getUserAdminDefaultLastName(), dc.getUserAdminDefaultPassword());
        u.setEnabled(true);
        return signUp(u, true);
    }
    //endregion

    public EUser signUp(EUser u, boolean emailVerified) {
        return signUp(u, emailVerified, false);
    }

    public EUser signUp(EUser u, boolean emailVerified, boolean isSuperRegistration) {
        if (isSuperRegistration || configService.isUserRegistrationAllowed()) {
            EUser sU = new EUser(u.getUsername(), u.getEmail(), u.getName(), u.getLastName(), passwordEncoder.encode(u.getPassword()));
            sU.setEnabled(u.isEnabled());
            sU.setEmailVerified(emailVerified);
            return userRepository.save(sU);
        }
        return null;
    }

    public List<Long> addRolesToUser(long userId, long entityId, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, true);
    }

    public List<Long> removeRolesFromUser(long userId, long entityId, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, false);
    }

    private ArrayList<Long> addRemoveRolesToFromUser (long userId,long entityId, List<Long> rolesId, Boolean add)
            throws NotFoundEntityException {
        ArrayList<Long> addedOrRemoved = new ArrayList<>();

        BAuthorizationPk pk;
        BAuthorization newUserAuth;

        EUser u = userRepository.findOne(userId);
        if (u == null) throw new NotFoundEntityException("user.not.found");

        EOwnedEntity e = entityRepository.findOne(entityId);
        if (e == null) throw new NotFoundEntityException("entity.not.found");
        BRole r;

        for (Long iRoleId : rolesId) {
            r = roleRepository.findOne(iRoleId);
            if (r != null) {
                pk = new BAuthorizationPk(u.getId(), e.getId(), r.getId());
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
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        EUser u = userRepository.findFirstByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (u != null) {
            return u;
        }
        throw new UsernameNotFoundException(msg.getMessage("user.not.found"));
    }

    public String getUserAuthoritiesForToken(String usernameOrEmail, String separator) {
        StringBuilder authBuilder = new StringBuilder();
        EUser u = (EUser)loadUserByUsername(usernameOrEmail);

        if (u != null) { // got user
            Long entityId = getEntityIdByUser(u);

            if (entityId == null) { return ""; }    // has not any role over any entity

            List<BPermission> pFromDB = permissionService.findPermissionsByUserIdAndEntityId(u.getId(), entityId);

            for (BPermission p : pFromDB) {
                authBuilder.append(p.getName()).append(separator);
            }
        }

        return authBuilder.toString();
    }

    /**
     * Returns the id of the last accessed entity by a user. If the user has never accessed an entity before then a search will
     * be performed in order to know which entities the user has access to. The id of the first of theses found will be
     * returned. If no entities is found, then <code>null</code> is returned.
     * @param u {@link EUser} which is being trying to find the association to.
     * @return The identifier of the entity or <code>null</code> if none is found.
     */
    private Long getEntityIdByUser(EUser u) {
        Long entityId = configService.getLastAccessedEntityIdByUser(u.getId());
        if (entityId == null) {
            BAuthorization anAuth = authorizationRepository.findFirstByUserAndEntityNotNull(u);
            if (anAuth == null) {   // this user has no assigned roles
                return null;
            }
            entityId = anAuth.getEntity().getId();
            configService.setLastAccessedEntityIdByUser(u.getId(), entityId); // todo: log if the config couldn't be saved
        }
        return entityId;
    }
}
