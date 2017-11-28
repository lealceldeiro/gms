package com.gmsboilerplatesbng.service.security.role;

import com.gmsboilerplatesbng.domain.security.permission.BPermission;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.repository.security.permission.BPermissionRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    private final DefaultConst c;

    private final BRoleRepository repository;

    private final BPermissionRepository permissionRepository;


    @Autowired
    public RoleService(BRoleRepository repository, BPermissionRepository permissionRepository, DefaultConst defaultConst) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
        this.c = defaultConst;
    }

    //region default role
    public BRole createDefaultRole() {
        BRole role = new BRole(this.c.ROLE_LABEL);
        role.setDescription(this.c.ROLE_DESCRIPTION);
        role.setEnabled(this.c.ROLE_ENABLED);

        final Iterable<BPermission> permissions = this.permissionRepository.findAll();

        for (BPermission p : permissions) {
            role.addPermission(p);
        }

        return this.repository.save(role);
    }
    //endregion

}
