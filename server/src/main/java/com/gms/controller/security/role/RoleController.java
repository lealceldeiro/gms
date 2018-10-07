package com.gms.controller.security.role;

import com.gms.service.security.role.RoleService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@BasePathAwareController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> addPermissionsRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.addPermissionsToRole(id, permissionsId);
    }

    @DeleteMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> removePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.removePermissionsFromRole(id, permissionsId);
    }

    @PutMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> updatePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.updatePermissionsInRole(id, permissionsId);
    }
}
