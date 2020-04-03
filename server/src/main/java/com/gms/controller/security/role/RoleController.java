package com.gms.controller.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.security.role.RoleService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@BasePathAwareController
@RequiredArgsConstructor
public class RoleController {

    /**
     * Instance of {@link RoleService}.
     */
    private final RoleService roleService;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id            {@link com.gms.domain.security.role.BRole} id.
     * @param permissionsId {@link BPermission} id.
     * @return A {@link List} of {@link Long} with all of the {@link BPermission} IDs added to the role.
     * @throws NotFoundEntityException if none of the permissions with an {@code id} is found from the specified list
     *                                 of {@code id}s.
     */
    @PostMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> addPermissionsRole(@PathVariable final Long id, @RequestBody final List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.addPermissionsToRole(id, permissionsId);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id            {@link com.gms.domain.security.role.BRole} id.
     * @param permissionsId {@link BPermission} id.
     * @return A {@link List} of {@link Long} with all of the {@link BPermission} IDs removed from the role.
     * @throws NotFoundEntityException if none of the permissions with an {@code id} is found from the specified list
     *                                 *                                 of {@code id}s.
     */
    @DeleteMapping(
            path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json"
    )
    @ResponseBody
    public List<Long> removePermissionsFromRole(@PathVariable final Long id,
                                                @RequestBody final List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.removePermissionsFromRole(id, permissionsId);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id            {@link com.gms.domain.security.role.BRole} id.
     * @param permissionsId {@link BPermission} id.
     * @return A {@link List} of {@link Long} with all of the {@link BPermission} IDs updated from the role.
     * @throws NotFoundEntityException if none of the permissions with an {@code id} is found from the specified list
     *                                 of {@code id}s.
     */
    @PutMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public List<Long> updatePermissionsFromRole(@PathVariable final Long id,
                                                @RequestBody final List<Long> permissionsId)
            throws NotFoundEntityException {
        return roleService.updatePermissionsInRole(id, permissionsId);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id            {@link com.gms.domain.security.role.BRole} id.
     * @param pageable      {@link Pageable} bean injected by spring.
     * @param pageAssembler {@link PagedResourcesAssembler} bean injected by spring.
     * @return A {@link ResponseEntity} of {@link PagedModel} of {@link EntityModel} of {@link BPermission}
     * containing the requested information.
     */
    @GetMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<PagedModel<EntityModel<BPermission>>> getAllPermissionsByRole(
            @PathVariable final long id,
            final Pageable pageable,
            final PagedResourcesAssembler<BPermission> pageAssembler
    ) {
        Page<BPermission> page = roleService.getAllPermissionsByRoleId(id, pageable);
        Link link = linkTo(methodOn(RoleController.class).getAllPermissionsByRole(id, pageable, pageAssembler))
                .withSelfRel();
        PagedModel<EntityModel<BPermission>> pagedResources = pageAssembler.toModel(page, link);

        return ResponseEntity.ok(pagedResources);
    }

}
