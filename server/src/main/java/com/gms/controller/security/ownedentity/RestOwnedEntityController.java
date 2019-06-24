package com.gms.controller.security.ownedentity;

import javax.validation.Valid;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.security.ownedentity.OwnedEntityService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@BasePathAwareController
@RestControllerAdvice
@RequiredArgsConstructor
public class RestOwnedEntityController {

    private final OwnedEntityService entityService;

    /**
     * Registers a new {@link com.gms.domain.security.ownedentity.EOwnedEntity}.
     * This method overrides the default "entity" url in the {@link com.gms.repository.security.ownedentity.EOwnedEntityRepository}
     * by setting its path to 'entity' (like in the `EOwnedEntityRepository` interface, by doing `produces = 'application/hal+json'`
     * and by putting in within a controller annotated as `@BasePathAwareController`.
     * @param entity {@link com.gms.domain.security.ownedentity.EOwnedEntity} data to be created.
     * @return A {@link com.gms.domain.security.ownedentity.EOwnedEntity} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = ResourcePath.OWNED_ENTITY, produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EOwnedEntity> create(@Valid @RequestBody Resource<EOwnedEntity> entity)
            throws GmsGeneralException {
        EOwnedEntity e = entityService.create(entity.getContent());
        if (e != null) {
            return new ResponseEntity<EOwnedEntity>(HttpStatus.CREATED);
        }
        else throw new GmsGeneralException("entity.add.error", false);
    }

}
