package com.gms.service.security.user;

import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.BAuthorization.BAuthorizationPk;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.configuration.ConfigurationService;
import com.gms.service.security.permission.PermissionService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * UserService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final EUserRepository userRepository;
    private final EOwnedEntityRepository entityRepository;
    private final BRoleRepository roleRepository;
    private final BAuthorizationRepository authorizationRepository;
    private final ConfigurationService configService;
    private final DefaultConst dc;
    private final MessageResolver msg;
    private final PermissionService permissionService;

    private static final String USER_NOT_FOUND = "user.not.found";
    private static final String O_ENTITY_NOT_FOUND = "entity.not.found";

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
            u.setEmailVerified(emailVerified);
            return userRepository.save(u);
        }
        return null;
    }

    public List<Long> addRolesToUser(String userUsername, String entityUsername, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userUsername, entityUsername, rolesId, true);
    }

    public List<Long> removeRolesFromUser(String userUsername, String entityUsername, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userUsername, entityUsername, rolesId, false);
    }

    private List<Long> addRemoveRolesToFromUser (String userUsername, String entityUsername, List<Long> rolesId, Boolean add)
            throws NotFoundEntityException {
        LinkedList<Long> addedOrRemoved = new LinkedList<>();

        BAuthorizationPk pk;
        BAuthorization newUserAuth;

        EUser u = getUser(userUsername);
        EOwnedEntity e = getOwnedEntity(entityUsername);
        BRole r;

        for (long iRoleId : rolesId) {
            r = roleRepository.findOne(iRoleId);
            if (r != null) {
                pk = new BAuthorizationPk(u.getId(), e.getId(), r.getId());
                newUserAuth = new BAuthorization(pk, u, e, r);
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
        throw new UsernameNotFoundException(msg.getMessage(USER_NOT_FOUND));
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

    public Map<String, List<BRole>> getRolesForUser(String userUsername) throws NotFoundEntityException {
        EUser u = getUser(userUsername);
        return authorizationRepository.getRolesForUserOverAllEntities(u.getId());
    }

    public List<BRole> getRolesForUserOverEntity(String userUsername, String entityUsername) throws NotFoundEntityException {
        return authorizationRepository.getRolesForUserOverEntity(getUser(userUsername).getId(), getOwnedEntity(entityUsername).getId());
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
            BAuthorization anAuth = authorizationRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(u, true);
            if (anAuth == null) {   // this user has no assigned roles or they are not enabled
                return null;
            }
            entityId = anAuth.getEntity().getId();
            if (!configService.setLastAccessedEntityIdByUser(u.getId(), entityId)) {
                log.warn("Last accessed entity with id `" + entityId + "` for user with id `" + u.getId()
                        + "` could not be saved.");
            }
        }
        return entityId;
    }

    private EUser getUser(String username) throws NotFoundEntityException {
        EUser u = userRepository.findFirstByUsername(username);
        if (u == null) {
            throw new NotFoundEntityException(USER_NOT_FOUND);
        }
        return u;
    }

    private EOwnedEntity getOwnedEntity(String username) throws NotFoundEntityException {
        EOwnedEntity e = entityRepository.findFirstByUsername(username);
        if (e == null) {
            throw new NotFoundEntityException(O_ENTITY_NOT_FOUND);
        }
        return e;
    }
}
