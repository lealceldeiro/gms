package com.gms.service.security.role;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final BRoleRepository repository;
    private final BPermissionRepository permissionRepository;
    private final DefaultConst dc;

    public static final String ROLE_NOT_FOUND = "role.not.found";
    private static final int ADD_PERMISSIONS = 1;
    private static final int REMOVE_PERMISSIONS = -1;
    private static final int UPDATE_PERMISSIONS = 0;

    // region default role

    /**
     * Creates the default Role according to the values regarding to this resource
     * in {@link DefaultConst}
     * 
     * @return The just created resource {@link BRole}
     */
    public BRole createDefaultRole() {
        BRole role = new BRole(dc.getRoleAdminDefaultLabel());
        role.setDescription(dc.getRoleAdminDefaultDescription());
        role.setEnabled(dc.isRoleAdminDefaultEnabled());

        final Iterable<BPermission> permissions = permissionRepository.findAll();

        for (BPermission p : permissions) {
            role.addPermission(p);
        }

        return repository.save(role);
    }
    // endregion

    /**
     * Adds some {@link BPermission}s to a {@link BRole}
     * 
     * @param roleId        {@link BRole#id} to which the permissions will be added.
     * @param permissionsId A {@link List} of {@link BPermission#id} with all the
     *                      <code>id</code> belonging to the permissions that are
     *                      going to be added.
     * @return A {@link List} with all the <code>id</code> of the
     *         {@link BPermission}s which where successfully added.
     * @throws NotFoundEntityException If none of the provided <code>id</code> in
     *                                 the <code>permissionsId</code> list
     *                                 correspond to any {@link BPermission} in the
     *                                 database.
     */
    public List<Long> addPermissionsToRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, ADD_PERMISSIONS);
    }

    /**
     * Removes some {@link BPermission}s from a {@link BRole}
     * 
     * @param roleId        {@link BRole#id} from which the permissions will be
     *                      removed.
     * @param permissionsId A {@link List} of {@link BPermission#id} with all the
     *                      <code>id</code> belonging to the permissions that are
     *                      going to be removed.
     * @return A {@link List} with all the <code>id</code> of the
     *         {@link BPermission}s which where successfully removed.
     * @throws NotFoundEntityException If none of the provided <code>id</code> in
     *                                 the <code>permissionsId</code> list
     *                                 correspond to any {@link BPermission} in the
     *                                 database.
     */
    public List<Long> removePermissionsFromRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, REMOVE_PERMISSIONS);
    }

    /**
     * Updates the {@link BPermission}s in a {@link BRole}
     * 
     * @param roleId        {@link BRole#id} in which the permissions will be
     *                      updated.
     * @param permissionsId A {@link List} of {@link BPermission#id} with all the
     *                      <code>id</code> belonging to the permissions that are
     *                      going to be set in the role.
     * @return A {@link List} with all the <code>id</code> of the
     *         {@link BPermission}s which where successfully set.
     * @throws NotFoundEntityException If none of the provided <code>id</code> in
     *                                 the <code>permissionsId</code> list
     *                                 correspond to any {@link BPermission} in the
     *                                 database.
     */
    public List<Long> updatePermissionsInRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, UPDATE_PERMISSIONS);
    }

    private List<Long> addRemovePermissionToFromRole(long id, List<Long> permissionsId, int operation)
            throws NotFoundEntityException {
        BRole r = getRole(id);
        List<Long> set = new LinkedList<>();
        Optional<BPermission> p;

        if (operation == UPDATE_PERMISSIONS)
            r.removeAllPermissions();

        for (Long pId : permissionsId) {
            p = permissionRepository.findById(pId);
            if (p.isPresent()) {
                if (operation == REMOVE_PERMISSIONS)
                    r.removePermission(p.get());
                else
                    r.addPermission(p.get());
                set.add(pId);
            }
        }
        if (set.isEmpty()) {
            throw new NotFoundEntityException("role.add.permissions.found.none");
        }
        return set;
    }

    public BRole getRole(long id) throws NotFoundEntityException {
        Optional<BRole> r = repository.findById(id);
        if (!r.isPresent()) {
            throw new NotFoundEntityException(ROLE_NOT_FOUND);
        }
        return r.get();
    }

    /**
     * Returns a pageable result with the permissions associated to a role by
     * specifying specifying the role id.
     * 
     * @param id       Role identifier.
     * @param pageable Pageable with all the pagination attributes.
     */
    public Page<BPermission> getAllPermissionsByRoleId(long id, Pageable pageable) {
        BRole role = repository.findById(id).orElse(new BRole());
        Set<BRole> set = new HashSet<>();
        set.add(role);
        return permissionRepository.findAllByRoles(set, pageable);
    }
}
