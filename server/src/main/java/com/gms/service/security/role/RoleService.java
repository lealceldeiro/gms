package com.gms.service.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    /**
     * An instance of a {@link BRoleRepository}.
     */
    private final BRoleRepository roleRepository;

    /**
     * An instance of a {@link BPermissionRepository}.
     */
    private final BPermissionRepository permissionRepository;

    /**
     * An instance of a {@link DefaultConstant}.
     */
    private final DefaultConstant defaultConstant;

    /**
     * i18n key for the message shown when a role with the provided arguments is not found.
     */
    public static final String ROLE_NOT_FOUND_MESSAGE_KEY = "role.not.found";

    // region default role

    /**
     * Creates the default Role according to the values regarding to this resource in {@link DefaultConstant}.
     *
     * @return The just created resource {@link BRole}
     */
    public BRole createDefaultRole() {
        final BRole role = new BRole(defaultConstant.getRoleAdminDefaultLabel());
        role.setDescription(defaultConstant.getRoleAdminDefaultDescription());
        role.setEnabled(defaultConstant.isRoleAdminDefaultEnabled());

        permissionRepository.findAll().forEach(role::addPermission);

        return roleRepository.save(role);
    }
    // endregion

    /**
     * Adds some {@link BPermission}s to a {@link BRole}.
     *
     * @param roleId        Role id to which the permissions will be added.
     * @param permissionsId A {@link List} of permissions IDs with all the
     *                      {@code id} belonging to the permissions that are
     *                      going to be added.
     * @return A {@link List} with all the {@code id} of the
     * {@link BPermission}s which where successfully added.
     * @throws NotFoundEntityException If none of the provided {@code id} in the {@code permissionsId} list correspond
     *                                 to any {@link BPermission} in the database.
     * @see BRole
     * @see BPermission
     */
    public List<Long> addPermissionsToRole(final long roleId, final Iterable<Long> permissionsId)
            throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, ActionOverRolePermissions.ADD_PERMISSIONS);
    }

    /**
     * Removes some {@link BPermission}s from a {@link BRole}.
     *
     * @param roleId        Role id from which the permissions will be removed.
     * @param permissionsId A {@link List} of permission IDs with all the {@code id} belonging to the permissions that
     *                      are going to be removed.
     * @return A {@link List} with all the {@code id} of the
     * {@link BPermission}s which where successfully removed.
     * @throws NotFoundEntityException If none of the provided {@code id} in the {@code permissionsId} list correspond
     *                                 to any {@link BPermission} in the database.
     * @see BRole
     * @see BPermission
     */
    public List<Long> removePermissionsFromRole(final long roleId, final Iterable<Long> permissionsId)
            throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, ActionOverRolePermissions.REMOVE_PERMISSIONS);
    }

    /**
     * Updates the {@link BPermission}s in a {@link BRole}.
     *
     * @param roleId        Role id in which the permissions will be updated.
     * @param permissionsId A {@link List} of permission IDs with all the {@code id} belonging to the permissions that
     *                      are going to be set in the role.
     * @return A {@link List} with all the {@code id} of the
     * {@link BPermission}s which where successfully set.
     * @throws NotFoundEntityException If none of the provided {@code id} in the {@code permissionsId} list correspond
     *                                 to any {@link BPermission} in the database.
     * @see BRole
     * @see BPermission
     */
    public List<Long> updatePermissionsInRole(final long roleId, final Iterable<Long> permissionsId)
            throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, ActionOverRolePermissions.UPDATE_PERMISSIONS);
    }

    /**
     * Returns a {@link BRole} from its id provided as argument.
     *
     * @param id {@link BRole}'s id.
     * @return The {@link BRole} which id is equal to the provided argument {@code id}.
     * @throws NotFoundEntityException If there is no role with the provided id.
     */
    public BRole getRole(final long id) throws NotFoundEntityException {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ROLE_NOT_FOUND_MESSAGE_KEY));
    }

    private List<Long> addRemovePermissionToFromRole(final long id, final Iterable<Long> permissionIdentifiers,
                                                     final ActionOverRolePermissions operation)
            throws NotFoundEntityException {

        final List<Long> permissionWorkCollection = updateRolePermissions(permissionIdentifiers,
                                                                          operation,
                                                                          getRole(id),
                                                                          permissionRepository);
        if (permissionWorkCollection.isEmpty()) {
            throw new NotFoundEntityException("role.add.permissions.found.none");
        }

        return permissionWorkCollection;
    }

    /**
     * Returns a pageable result with the permissions associated to a role by specifying specifying the role id.
     *
     * @param id       Role identifier.
     * @param pageable Pageable with all the pagination attributes.
     * @return A {@link Page} of {@link BPermission}'s.
     */
    public Page<BPermission> getAllPermissionsByRoleId(final long id, final Pageable pageable) {
        final Set<BRole> roles = new HashSet<>();
        roles.add(roleRepository.findById(id).orElse(new BRole()));

        return permissionRepository.findAllByRolesIn(roles, pageable);
    }

    /**
     * Indicates an action for a permission over a role.
     */
    public enum ActionOverRolePermissions {
        /**
         * Indicates that the permissions should be added to the role.
         */
        ADD_PERMISSIONS,
        /**
         * Indicates that the permissions should be removed from the role.
         */
        REMOVE_PERMISSIONS,
        /**
         * Indicates that the permissions should be updated to the role.
         */
        UPDATE_PERMISSIONS
    }

    private static List<Long> updateRolePermissions(final Iterable<Long> permissionIdentifiers,
                                                    final ActionOverRolePermissions operation,
                                                    final BRole role,
                                                    final CrudRepository<? extends BPermission, ? super Long> repository
    ) {
        final List<Long> permissionWorkCollection = new ArrayList<>();

        if (operation == ActionOverRolePermissions.UPDATE_PERMISSIONS) {
            role.removeAllPermissions();
        }

        permissionIdentifiers.forEach(permissionId -> repository
                .findById(permissionId)
                .ifPresent(permission -> {
                    if (operation == ActionOverRolePermissions.REMOVE_PERMISSIONS) {
                        role.removePermission(permission);
                    } else {
                        role.addPermission(permission);
                    }
                    permissionWorkCollection.add(permissionId);
                })
        );

        return permissionWorkCollection;
    }

}
