package com.gms.service;

import com.gms.service.configuration.ConfigurationService;
import com.gms.service.security.ownedentity.OwnedEntityService;
import com.gms.service.security.permission.PermissionService;
import com.gms.service.security.role.RoleService;
import com.gms.service.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AppService {

     private final ConfigurationService configurationService;
     private final PermissionService permissionService;
     private final RoleService roleService;
     private final UserService userService;
     private final OwnedEntityService oeService;
     private Boolean initialLoadOK = null;

    /**
     * Returns whether the application started successfully or not. In order to start the application successfully there
     * are some requisites that must be fulfilled.
     * @return <code>true</code> if the application started successfully, <code>false</code> otherwise.
     */
     public boolean isInitialLoadOK() {
         if (initialLoadOK == null) {
             boolean ok = true;
             if (!configurationService.configurationExist()) { //first app start up
                 ok = configurationService.createDefaultConfig();
                 ok = ok && permissionService.createDefaultPermissions();
                 ok = ok && roleService.createDefaultRole() != null;
                 ok = ok && userService.createDefaultUser() != null;
                 ok = ok && oeService.createDefaultEntity() != null;
                 ok = ok && configurationService.assignDefaultUserToEntityWithRole();
             }
             initialLoadOK = ok;
         }
         return initialLoadOK;
     }
}
