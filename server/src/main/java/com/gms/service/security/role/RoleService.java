package com.gms.service.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.util.constant.DefaultConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private List<Long> addRemovePermissionToFromRole(long roleId, List<Long> permissionsId) {
       return null;
    }
}
