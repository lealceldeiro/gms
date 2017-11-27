package com.gmsboilerplatesbng.service.security.role;

import com.gmsboilerplatesbng.domain.security.permission.BPermission;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.repository.security.permission.BPermissionRepository;
import com.gmsboilerplatesbng.repository.security.role.BRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Value("${default.gmsrole.label}")
    private String defaultRoleLabel = "ROLE_ADMIN";

    @Value("${default.gmsrole.description}")
    private String defaultRoleDescription = "Default role";

    @Value("${default.gmsrole.enabled}")
    private Boolean defaultRoleEnabled = true;

    private final BRoleRepository repository;

    private final BPermissionRepository permissionRepository;


    @Autowired
    public RoleService(BRoleRepository repository, BPermissionRepository permissionRepository) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
    }

    //region default user
    public BRole createDefaultRole() {
        BRole role = new BRole(this.defaultRoleLabel);
        role.setDescription(this.defaultRoleDescription);
        role.setEnabled(this.defaultRoleEnabled);

        final Iterable<BPermission> permissions = this.permissionRepository.findAll();

        for (BPermission p : permissions) {
            role.addPermission(p);
        }

        return this.repository.save(role);
    }
    //endregion

}
