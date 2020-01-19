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
public class UserService implements UserDetailsService {

    /**
     * An instance of .
     */
    private final EUserRepository userRepository;
    /**
     * An instance of {@link EOwnedEntityRepository}.
     */
    private final EOwnedEntityRepository entityRepository;
    /**
     * An instance of {@link BRoleRepository}.
     */
    private final BRoleRepository roleRepository;
    /**
     * An instance of {@link BAuthorizationRepository}.
     */
    private final BAuthorizationRepository authorizationRepository;
    /**
     * An instance of {@link ConfigurationService}.
     */
    private final ConfigurationService configService;
    /**
     * An instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * An instance of {@link MessageResolver}.
     */
    private final MessageResolver msg;
    /**
     * An instance of {@link PermissionService}.
     */
    private final PermissionService permissionService;

    /**
     * i18n key for the message shown when the user is not found.
     */
    private static final String USER_NOT_FOUND = "user.not.found";
    /**
     * i18n key for the message shown when the entity is not found.
     */
    private static final String O_ENTITY_NOT_FOUND = "entity.not.found";

    //region default user

    /**
     * Creates the default user that is registered when the application first start up.
     *
     * @return The created {@link EUser}.
     */
    public EUser createDefaultUser() {
        EUser u = new EUser(
                dc.getUserAdminDefaultUsername(),
                dc.getUserAdminDefaultEmail(),
                dc.getUserAdminDefaultName(),
                dc.getUserAdminDefaultLastName(),
                dc.getUserAdminDefaultPassword()
        );
        u.setEnabled(true);

        return signUp(u, EmailStatus.VERIFIED);
    }
    //endregion

    /**
     * Registers a new user specifying whether his/her email verification status.
     *
     * @param u           User's data ({@link EUser})
     * @param emailStatus One of {@link EmailStatus} which specify whether the email is verified or not.
     * @return The registered {@link EUser}'s data
     */
    public EUser signUp(final EUser u, final EmailStatus emailStatus) {
        return signUp(u, emailStatus, RegistrationPrivilege.REGULAR_USER);
    }

    /**
     * Registers a new user specifying his/her email verification status and  whether this is a
     * user registration performed by a superuser or not.
     *
     * @param u                     User's data ({@link EUser})
     * @param emailStatus           One of {@link EmailStatus} which specify whether the email is verified or not.
     * @param registrationPrivilege One of {@link RegistrationPrivilege} which indicates whether this action is being
     *                              performed by a superuser or not.
     * @return The registered {@link EUser}'s data
     */
    public EUser signUp(final EUser u, final EmailStatus emailStatus,
                        final RegistrationPrivilege registrationPrivilege) {
        if (registrationPrivilege == RegistrationPrivilege.SUPER_USER || configService.isUserRegistrationAllowed()) {
            u.setEmailVerified(emailStatus == EmailStatus.VERIFIED);
            return userRepository.save(u);
        }

        return null;
    }

    /**
     * Add some existing {@link BRole}s to an existing {@link EUser} over some {@link EOwnedEntity}.
     *
     * @param userId   {@link EUser}'s id
     * @param entityId {@link EOwnedEntity}'id
     * @param rolesId  An {@link Iterable<Long>} which contains the identifiers of the {@link BRole}'s to be added.
     * @return An {@link Iterable<Long>} with the identifiers which were successfully added to the user.
     * @throws NotFoundEntityException if there is not any {@link EUser} registered with the provided
     *                                 {@code userUsername}, if there is no any {@link EOwnedEntity} registered with the
     *                                 provided {@code entityUsername}, or if there is not any {@link BRole} with an
     *                                 {@code id} equals to any of the provided in the {@code rolesId}.
     */
    public List<Long> addRolesToUser(final Long userId, final Long entityId,
                                     final Iterable<Long> rolesId) throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, true);
    }

    /**
     * Removes some existing {@link BRole}s from an existing {@link EUser} over some {@link EOwnedEntity}.
     *
     * @param userId   {@link EUser}'s id
     * @param entityId {@link EOwnedEntity}'id
     * @param rolesId  An {@link Iterable<Long>} which contains the identifiers of the {@link BRole}'s to be removed.
     * @return An {@link Iterable<Long>} with the identifiers which were successfully removed the the user.
     * @throws NotFoundEntityException if there is not any {@link EUser} registered with the provided
     *                                 {@code userUsername}, if there is no any {@link EOwnedEntity} registered with the
     *                                 provided {@code entityUsername}, or if there is not any {@link BRole} with an
     *                                 {@code id} equals to any of the provided in the {@code rolesId}.
     */
    public List<Long> removeRolesFromUser(final Long userId, final Long entityId, final Iterable<Long> rolesId)
            throws NotFoundEntityException {
        return addRemoveRolesToFromUser(userId, entityId, rolesId, false);
    }

    private List<Long> addRemoveRolesToFromUser(final long userId, final long entityId, final Iterable<Long> rolesId,
                                                final boolean add) throws NotFoundEntityException {
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
                } else {
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
     *
     * @param usernameOrEmail {@link EUser}'s username
     * @return The first username found with the provided {@code usernameOrEmail}
     */
    @Override
    public UserDetails loadUserByUsername(final String usernameOrEmail) {
        EUser u = userRepository.findFirstByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (u != null) {
            return u;
        }
        throw new UsernameNotFoundException(msg.getMessage(USER_NOT_FOUND));
    }

    /**
     * Gets a {@link EUser}'s permissions as string separated by a specified separator.
     *
     * @param usernameOrEmail {@link EUser}'s username to who the permissions are associated.
     * @param separator       {@link String} indicating the separator will be used for separating the permissions in the
     *                        string.
     * @return A {@link String} with all the permissions found separated by the specified {@code separator}.
     */
    public String getUserAuthoritiesForToken(final String usernameOrEmail, final String separator) {
        StringBuilder authBuilder = new StringBuilder();
        EUser u = (EUser) loadUserByUsername(usernameOrEmail);

        if (u != null) { // got user
            Long entityId = getEntityIdByUser(u);

            if (entityId == null) {
                return "";
            }    // has not any role over any entity

            List<BPermission> pFromDB = permissionService.findPermissionsByUserIdAndEntityId(u.getId(), entityId);

            for (BPermission p : pFromDB) {
                authBuilder.append(p.getName()).append(separator);
            }
        }

        return authBuilder.toString();
    }

    /**
     * Returns all the roles associated to a user over all entities int the shape of a Map.
     *
     * @param id {@link EUser}'s id.
     * @return A {@link Map} with all the roles associated to a user over all possible entities. Every key in the map is
     * the entity username and the associated value is a {@link List<BRole>} with all the roles the user has over that
     * entity represented by the key (which is the username).
     * @throws NotFoundEntityException if there is not any username with the specified {@code userUsername}.
     */
    public Map<String, List<BRole>> getRolesForUser(final Long id) throws NotFoundEntityException {
        EUser u = getUser(id);
        return authorizationRepository.getRolesForUserOverAllEntities(u.getId());
    }

    /**
     * Returns all he roles associated to a user over a specific entity.
     *
     * @param userId   {@link EUser}'s id.
     * @param entityId {@link EOwnedEntity}'s id.
     * @return A {@link List<BRole>} with all the roles the user has over the entity with the specified
     * {@code entityUsername}.
     * @throws NotFoundEntityException if either the user or the entity are not found with the provided
     *                                 {@code userUsername} and the {@code entityUsername} respectively.
     */
    public List<BRole> getRolesForUserOverEntity(final Long userId, final Long entityId)
            throws NotFoundEntityException {
        return authorizationRepository.getRolesForUserOverEntity(
                getUser(userId).getId(), getOwnedEntity(entityId).getId()
        );
    }

    /**
     * Returns the id of the last accessed entity by a user. If the user has never accessed an entity before then a
     * search will be performed in order to know which entities the user has access to. The id of the first of theses
     * found will be returned. If no entities is found, then {@code null} is returned.
     *
     * @param u {@link EUser} which is being trying to find the association to.
     * @return The identifier of the entity or {@code null} if none is found.
     */
    public Long getEntityIdByUser(final EUser u) {
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

    private EUser getUser(final Long id) throws NotFoundEntityException {
        Optional<EUser> u = userRepository.findById(id);
        if (!u.isPresent()) {
            throw new NotFoundEntityException(USER_NOT_FOUND);
        }
        return u.get();
    }

    private EOwnedEntity getOwnedEntity(final Long id) throws NotFoundEntityException {
        Optional<EOwnedEntity> e = entityRepository.findById(id);
        if (!e.isPresent()) {
            throw new NotFoundEntityException(O_ENTITY_NOT_FOUND);
        }
        return e.get();
    }

    /**
     * Possible email statuses of a user when it is being registered.
     */
    public enum EmailStatus {
        /**
         * The user has verified his/her email.
         */
        VERIFIED,
        /**
         * The user has not verified his/her email yet.
         */
        NOT_VERIFIED
    }

    /**
     * Possible registration types when a new user is being registered.
     */
    public enum RegistrationPrivilege {
        /**
         * Used to indicated that the user is being registered by a super user.
         */
        SUPER_USER,

        /**
         * Used to indicate that the user is being registered as a regular user with no special privileges.
         */
        REGULAR_USER
    }

}
