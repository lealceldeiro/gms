package com.gms.service.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.util.permission.BPermissionDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
public class PermissionService {

    /**
     * An instance of a {@link BPermissionRepository}.
     */
    private final BPermissionRepository permissionRepository;

    /**
     * An instance of {@link BRoleRepository}.
     */
    private final BRoleRepository roleRepository;

    //region default permissions

    /**
     * Creates the default Permissions.
     *
     * @return {@code true} if all the permissions where created successfully, {@code false} otherwise.
     */
    public boolean areDefaultPermissionsCreatedSuccessfully() {
        boolean ok = true;
        final BPermissionDefinition[] permissionDefinitions = BPermissionDefinition.values();
        for (BPermissionDefinition permissionDefinition : permissionDefinitions) {
            ok = ok && savePermission(permissionDefinition, permissionRepository) != null;
        }

        return ok;
    }
    //endregion

    /**
     * Lists all permissions associated to a {@link com.gms.domain.security.user.EUser} over an
     * {@link com.gms.domain.security.ownedentity.EOwnedEntity} through some role(s).
     *
     * @param userId   User ir
     * @param entityId Entity id
     * @return A {@link List} of {@link BPermission} with all the found permissions.
     * @see com.gms.domain.security.user.EUser
     * @see com.gms.domain.security.ownedentity.EOwnedEntity
     * @see com.gms.domain.GmsEntity
     */
    public List<BPermission> findPermissionsByUserIdAndEntityId(final long userId, final long entityId) {
        return permissionRepository.findPermissionsByUserIdAndEntityId(userId, entityId);
    }

    /**
     * Returns a pageable result with the roles where a permission is assigned. The permission is queried by
     * specifying specifying its id.
     *
     * @param id       Permission identifier.
     * @param pageable Pageable with all the pagination attributes.
     * @return A {@link Page} of {@link BRole} associated to the role with {@code id} equals to the specified as
     * argument.
     */
    public Page<BRole> getAllRolesByPermissionId(final long id, final Pageable pageable) {
        final Set<BPermission> permissions = new HashSet<>();
        permissions.add(permissionRepository.findById(id).orElse(new BPermission()));

        return roleRepository.findAllByPermissionsIn(permissions, pageable);
    }

    /**
     * Saves the {@code permissionDefinition} as a {@link BPermission} and returns it.
     *
     * @param permissionDefinition {@link BPermissionDefinition} to save.
     * @param permissionRepository {@link BPermissionRepository} used to persist a new instance of {@link BPermission}.
     * @return The newly created instance of {@link BPermission}.
     */
    private static BPermission savePermission(final BPermissionDefinition permissionDefinition,
                                              final BPermissionRepository permissionRepository) {
        final BPermission permission = new BPermission(permissionDefinition.toString(),
                                                       permissionDefinition
                                                               .toString()
                                                               .replace("__", " ").replace("_", " "));
        return permissionRepository.save(permission);
    }

}
