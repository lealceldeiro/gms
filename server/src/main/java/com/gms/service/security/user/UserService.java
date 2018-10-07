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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
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

    /**
     * Registers a new user specifying whether he/she has verified his/her email or not.
     * @param u User's data ({@link EUser})
     * @param emailVerified {@link Boolean} which specify whether the email is verified or not.
     * @return The registered {@link EUser}'s data
     */
    public EUser signUp(EUser u, boolean emailVerified) {
        return signUp(u, emailVerified, false);
    }

    /**
     * Registers a new user specifying whether he/she has verified his/her email or not and accepting whether this is a
     * user registration performed by a superuser or not.
     * @param u User's data ({@link EUser})
     * @param emailVerified {@link Boolean} which specify whether the email is verified or not.
     * @param isSuperRegistration Whether this action is being performed by a superuser or not
     * @return The registered {@link EUser}'s data
     */
    public EUser signUp(EUser u, boolean emailVerified, boolean isSuperRegistration) {
        if (isSuperRegistration || configService.isUserRegistrationAllowed()) {
            u.setEmailVerified(emailVerified);
            return userRepository.save(u);
        }
        return null;
    }

    /**
     * Add some existing {@link BRole}s to an existing {@link EUser} over some {@link EOwnedEntity}
     * @param userId {@link EUser}'s id
     * @param entityId {@link EOwnedEntity}'id
     * @param rolesId A {@link List<Long>} which contains the identifiers of the {@link BRole}'s to be added ({@link BRole#id}
     * @return A {@link List<Long>} with the identifiers which were successfully added to the user.
     * @throws NotFoundEntityException if there is not any {@link EUser} registered with the provided <code>userUsername</code>,
     * if there is no any {@link EOwnedEntity} registered with the provided <code>entityUsername</code>, or if there is not
     * any {@link BRole} with an <code>id</code> equals to any of the provided in the <code>rolesId</code>.
     */
    public List<Long> addRolesToUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, true);
    }

    /**
     * Removes some existing {@link BRole}s from an existing {@link EUser} over some {@link EOwnedEntity}
     * @param userId {@link EUser}'s id
     * @param entityId {@link EOwnedEntity}'id
     * @param rolesId A {@link List<Long>} which contains the identifiers of the {@link BRole}'s to be removed ({@link BRole#id}
     * @return A {@link List<Long>} with the identifiers which were successfully removed the the user.
     * @throws NotFoundEntityException if there is not any {@link EUser} registered with the provided <code>userUsername</code>,
     * if there is no any {@link EOwnedEntity} registered with the provided <code>entityUsername</code>, or if there is not
     * any {@link BRole} with an <code>id</code> equals to any of the provided in the <code>rolesId</code>.
     */
    public List<Long> removeRolesFromUser(Long userId, Long entityId, List<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, false);
    }

    private List<Long> addRemoveRolesToFromUser (Long userId, Long entityId, List<Long> rolesId, Boolean add)
            throws NotFoundEntityException {
        LinkedList<Long> addedOrRemoved = new LinkedList<>();

        BAuthorizationPk pk;
        BAuthorization newUserAuth;

        EUser u = getUser(userId);
        EOwnedEntity e = getOwnedEntity(entityId);
        Optional<BRole> r;

        for (long iRoleId : rolesId) {
            r = roleRepository.findById(iRoleId);
            if (r.isPresent()) {
                pk = new BAuthorizationPk(u.getId(), e.getId(), r.get().getId());
                newUserAuth = new BAuthorization(pk, u, e, r.get());
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

    /**
     * Loads an {@link EUser}'s data from his/her username or email.
     * @param usernameOrEmail {@link EUser}'s username
     * @return The first username found with the provided <code>usernameOrEmail</code>
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        EUser u = userRepository.findFirstByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (u != null) {
            return u;
        }
        throw new UsernameNotFoundException(msg.getMessage(USER_NOT_FOUND));
    }

    /**
     * Gets a {@link EUser}'s permissions as string separated by a specified separator.
     * @param usernameOrEmail {@link EUser}'s username to who the permissions are associated.
     * @param separator {@link String} indicating the separator will be used for separating the permissions in the string
     * @return A {@link String} with all the permissions found separated by the specified <code>separator</code>.
     */
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
     * Returns all the roles associated to a user over all entities int the shape of a Map.
     * @param id {@link EUser}'s id.
     * @return A {@link Map} with all the roles associated to a user over all possible entities. Every key in the map is
     * the entity username and the associated value is a {@link List<BRole>} with all the roles the user has over that entity
     * represented by the key (which is the username).
     * @throws NotFoundEntityException if there is not any username with the specififed <code>userUsername</code>.
     */
    public Map<String, List<BRole>> getRolesForUser(Long id) throws NotFoundEntityException {
        EUser u = getUser(id);
        return authorizationRepository.getRolesForUserOverAllEntities(u.getId());
    }

    /**
     * Returns all he roles associated to a user over a specific entity.
     * @param userId {@link EUser}'s id.
     * @param entityId {@link EOwnedEntity}'s id.
     * @return A {@link List<BRole>} with all the roles the user has over the entity with the specified <code>entityUsername</code>.
     * @throws NotFoundEntityException if either the user or the entity are not found with the provided <code>userUsername</code>
     * and the <code>entityUsername</code> respectively.
     */
    public List<BRole> getRolesForUserOverEntity(Long userId, Long entityId) throws NotFoundEntityException {
        return authorizationRepository.getRolesForUserOverEntity(getUser(userId).getId(), getOwnedEntity(entityId).getId());
    }

    /**
     * Returns the id of the last accessed entity by a user. If the user has never accessed an entity before then a search will
     * be performed in order to know which entities the user has access to. The id of the first of theses found will be
     * returned. If no entities is found, then <code>null</code> is returned.
     * @param u {@link EUser} which is being trying to find the association to.
     * @return The identifier of the entity or <code>null</code> if none is found.
     */
    public Long getEntityIdByUser(EUser u) {
        Long entityId = configService.getLastAccessedEntityIdByUser(u.getId());
        if (entityId == null) {
            BAuthorization anAuth = authorizationRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(u, true);
            if (anAuth == null) {   // this user has no assigned roles or they are not enabled
                return null;
            }
            entityId = anAuth.getEntity().getId();
            configService.setLastAccessedEntityIdByUser(u.getId(), entityId);
        }
        return entityId;
    }

    private EUser getUser(Long id) throws NotFoundEntityException {
        Optional<EUser> u = userRepository.findById(id);
        if (!u.isPresent()) {
            throw new NotFoundEntityException(USER_NOT_FOUND);
        }
        return u.get();
    }

    private EOwnedEntity getOwnedEntity(Long id) throws NotFoundEntityException {
        Optional<EOwnedEntity> e = entityRepository.findById(id);
        if (!e.isPresent()) {
            throw new NotFoundEntityException(O_ENTITY_NOT_FOUND);
        }
        return e.get();
    }

}
