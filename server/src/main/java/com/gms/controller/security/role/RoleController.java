package com.gms.controller.security.role;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.security.role.RoleService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RepositoryRestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> addPermissionsRole(@PathVariable Long id, @RequestBody List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.addPermissionsToRole(id, permissionsId);
    }

    @DeleteMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION
            + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> removePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.removePermissionsFromRole(id, permissionsId);
    }

    @PutMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> updatePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.updatePermissionsInRole(id, permissionsId);
    }

    @GetMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<PagedResources<Resource<BPermission>>> getAllPermissionsByRole(@PathVariable long id,
            Pageable pageable, PagedResourcesAssembler<BPermission> pageAssembler) throws NotFoundEntityException {
        Page<BPermission> page = roleService.getAllPermissionsByRoleId(id, pageable);
        Link link = linkTo(methodOn(RoleController.class).getAllPermissionsByRole(id, pageable, pageAssembler))
                .withSelfRel();
        PagedResources<Resource<BPermission>> pagedResources = pageAssembler.toResource(page, link);

        return ResponseEntity.ok(pagedResources);
    }

}
