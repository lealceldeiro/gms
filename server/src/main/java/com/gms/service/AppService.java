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

    /**
     * Instance of a {@link ConfigurationService}.
     */
    private final ConfigurationService configurationService;

    /**
     * Instance of a {@link PermissionService}.
     */
    private final PermissionService permissionService;
    /**
     * Instance of a {@link RoleService}.
     */
    private final RoleService roleService;
    /**
     * Instance of a {@link UserService}.
     */
    private final UserService userService;
    /**
     * Instance of a {@link OwnedEntityService}.
     */
    private final OwnedEntityService oeService;
    /**
     * Indicates whether the application has performed an initial load ({@code null}), loaded OK ({@code true} or not
     * ({@code false}).
     */
    private Boolean initialLoadOK;

    /**
     * Returns whether the application started successfully or not. In order to start the application successfully there
     * are some requisites that must be fulfilled.
     *
     * @return {@code true} if the application started successfully, {@code false} otherwise.
     */
    public boolean isInitialLoadOK() {
        if (initialLoadOK == null) {
            boolean ok = true;
            if (!configurationService.isApplicationConfigured()) { //first app start up
                ok = configurationService.isDefaultConfigurationCreated();
                ok = ok && permissionService.areDefaultPermissionsCreatedSuccessfully();
                ok = ok && roleService.createDefaultRole() != null;
                ok = ok && userService.createDefaultUser() != null;
                ok = ok && oeService.createDefaultEntity() != null;
                ok = ok && configurationService.isDefaultUserAssignedToEntityWithRole();
            }
            initialLoadOK = ok;
        }

        return initialLoadOK;
    }

}
