package com.gms.controller.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.security.ownedentity.OwnedEntityService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;

/**
 * RestOwnedEntityController
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Feb 24, 2018
 */
@BasePathAwareController
@RestControllerAdvice
public class RestOwnedEntityController extends ResponseEntityExceptionHandler {

    private final MessageResolver msg;
    private final DefaultConst dc;
    private final OwnedEntityService entityService;

    public RestOwnedEntityController(OwnedEntityService entityService, MessageResolver messageResolver, DefaultConst defaultConst) {
        this.dc = defaultConst;
        this.entityService = entityService;
        this.msg = messageResolver;
    }


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
    @ResponseBody
    public ResponseEntity create(@Valid @RequestBody Resource<EOwnedEntity> entity)
            throws GmsGeneralException {
        EOwnedEntity e = entityService.create(entity.getContent());
        if (e != null) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else throw new GmsGeneralException("entity.add.error", false);
    }

    //region exception handling
    /**
     * Handles all exceptions which where not specifically treated.
     * @param ex {@link GmsGeneralException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(GmsGeneralException.class)
    protected ResponseEntity<Object> handleGmsGeneralException(GmsGeneralException ex, WebRequest req) {
        Object resBody = ExceptionUtil.getResponseBodyForGmsGeneralException(ex, msg, dc);
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), ex.getHttpStatus(), req);
    }


    /**
     * Handles all ConstraintViolationException exceptions thrown through the {@link TransactionSystemException} exception.
     *
     * @param ex  {@link TransactionSystemException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest req) {
        return handleConstraintViolationException(ex, req);
    }

    /**
     * Handles all ConstraintViolationException exceptions thrown through the {@link DataIntegrityViolationException} exception.
     *
     * @param ex  {@link DataIntegrityViolationException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest req) {
        return handleConstraintViolationException(ex, req);
    }

    private ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest req) {
        Object resBody = ExceptionUtil.getResponseBodyForConstraintViolationException(ex, msg, dc);
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, req);
    }
    //endregion

}
