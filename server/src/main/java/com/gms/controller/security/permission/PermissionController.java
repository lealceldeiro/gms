package com.gms.controller.security.permission;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.gms.domain.security.role.BRole;
import com.gms.service.security.permission.PermissionService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@BasePathAwareController
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping(path = ResourcePath.PERMISSION + "/{id}/" + ResourcePath.ROLE + "s", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<PagedResources<Resource<BRole>>> getAllRolesByPermission(@PathVariable long id,
            Pageable pageable, PagedResourcesAssembler<BRole> pageAssembler) throws NotFoundEntityException {
        Page<BRole> page = permissionService.getAllRolesByPermissionId(id, pageable);
        Link link = linkTo(methodOn(PermissionController.class).getAllRolesByPermission(id, pageable, pageAssembler))
                .withSelfRel();
        PagedResources<Resource<BRole>> pagedResources = pageAssembler.toResource(page, link);

        return ResponseEntity.ok(pagedResources);
    }

}
