package com.gms.controller.security.permission;

import com.gms.domain.security.role.BRole;
import com.gms.service.security.permission.PermissionService;
import com.gms.util.constant.ResourcePath;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@BasePathAwareController
public class PermissionController {

    /**
     * Instance of {@link PermissionService}.
     */
    private final PermissionService permissionService;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id            {@link com.gms.domain.security.permission.BPermission} id to get the roles associated to.
     * @param pageable      {@link Pageable} bean injected by spring.
     * @param pageAssembler {@link PagedResourcesAssembler} bean injected by spring.
     * @return A {@link ResponseEntity} of PagedModel of {@link EntityModel} of {@link BRole} containing the requested
     * information.
     */
    @GetMapping(path = ResourcePath.PERMISSION + "/{id}/" + ResourcePath.ROLE + "s", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<PagedModel<EntityModel<BRole>>> getAllRolesByPermission(
            @PathVariable final long id,
            final Pageable pageable,
            final PagedResourcesAssembler<BRole> pageAssembler
    ) {
        Page<BRole> page = permissionService.getAllRolesByPermissionId(id, pageable);
        Link link = linkTo(methodOn(PermissionController.class).getAllRolesByPermission(id, pageable, pageAssembler))
                .withSelfRel();
        PagedModel<EntityModel<BRole>> pagedResources = pageAssembler.toModel(page, link);

        return ResponseEntity.ok(pagedResources);
    }

}
