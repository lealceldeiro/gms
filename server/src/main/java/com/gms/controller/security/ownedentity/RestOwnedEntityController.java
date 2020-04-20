package com.gms.controller.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.security.ownedentity.OwnedEntityService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@BasePathAwareController
@RequiredArgsConstructor
public class RestOwnedEntityController {

    /**
     * Instance of {@link OwnedEntityService}.
     */
    private final OwnedEntityService entityService;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results. Registers a new {@link com.gms.domain.security.ownedentity.EOwnedEntity}. This method
     * overrides the default "entity" url in {@link com.gms.repository.security.ownedentity.EOwnedEntityRepository}
     * by setting its path to 'entity' (like in the `EOwnedEntityRepository` interface, by doing
     * {@code produces = 'application/hal+json'} and by putting in within a controller annotated as
     * {@code `@BasePathAwareController}.
     *
     * @param entityModel {@link com.gms.domain.security.ownedentity.EOwnedEntity} data to be created.
     * @return A {@link com.gms.domain.security.ownedentity.EOwnedEntity} mapped into a
     * {@link org.springframework.web.bind.annotation.ResponseBody}.
     * @throws GmsGeneralException when there is an error creating the entity.
     */
    @PostMapping(path = ResourcePath.OWNED_ENTITY, produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EOwnedEntity> create(@Valid @RequestBody final EntityModel<EOwnedEntity> entityModel)
            throws GmsGeneralException {
        final EOwnedEntity entity = entityService.create(entityModel.getContent());

        if (entity != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            throw GmsGeneralException.builder().messageI18N("entity.add.error").finishedOK(false).build();
        }
    }

}
