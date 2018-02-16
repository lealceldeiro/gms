package com.gms.service.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

/**
 * RoleService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final BRoleRepository repository;
    private final BPermissionRepository permissionRepository;
    private final DefaultConst dc;

    private static final String ROLE_NOT_FOUND = "role.not.found";
    private static final int ADD_PERMISSIONS = 1;
    private static final int REMOVE_PERMISSIONS = -1;
    private static final int UPDATE_PERMISSIONS = 0;

    //region default role
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
    //endregion

    public List<Long> addPermissionsToRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, ADD_PERMISSIONS);
    }

    public List<Long> removePermissionsFromRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, REMOVE_PERMISSIONS);
    }

    public List<Long> updatePermissionsInRole(long roleId, List<Long> permissionsId) throws NotFoundEntityException {
        return addRemovePermissionToFromRole(roleId, permissionsId, UPDATE_PERMISSIONS);
    }

    private List<Long> addRemovePermissionToFromRole(long id, List<Long> permissionsId, int operation) throws NotFoundEntityException {
        BRole r = getRole(id);
        List<Long> set = new LinkedList<>();
        BPermission p;

        if (operation == UPDATE_PERMISSIONS) r.removeAllPermissions();

        for(Long pId : permissionsId) {
            p = permissionRepository.findOne(pId);
            if (p != null){
                if (operation == REMOVE_PERMISSIONS) r.removePermission(p);
                else r.addPermission(p);
                set.add(pId);
            }
        }
        if (set.isEmpty()) {
            throw new NotFoundEntityException("role.add.permissions.found.none");
        }
        return set;
    }

    private BRole getRole(long id) throws NotFoundEntityException{
        BRole r = repository.findOne(id);
        if (r == null) {
            throw new NotFoundEntityException(ROLE_NOT_FOUND);
        }
        return r;
    }
}
