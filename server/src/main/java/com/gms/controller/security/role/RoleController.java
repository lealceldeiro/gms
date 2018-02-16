package com.gms.controller.security.role;

import com.gms.service.security.role.RoleService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * RoleController
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Feb 17, 2017
 */
@BasePathAwareController
@RestControllerAdvice
public class RoleController extends ResponseEntityExceptionHandler {

    private final MessageResolver msg;
    private final DefaultConst dc;
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService, MessageResolver messageResolver, DefaultConst defaultConst) {
        this.roleService = roleService;
        this.msg = messageResolver;
        this.dc = defaultConst;
    }

    @PostMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    public List<Long> addPermissionsRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.addPermissionsToRole(id, permissionsId);
    }

    @DeleteMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    public List<Long> removePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.removePermissionsFromRole(id, permissionsId);
    }

    @PutMapping(path = ResourcePath.ROLE + "/{id}/" + ResourcePath.PERMISSION + "s", produces = "application/hal+json")
    public List<Long> updatePermissionsFromRole(@PathVariable Long id, @RequestBody List<Long> permissionsId) throws NotFoundEntityException {
        return roleService.updatePermissionsInRole(id, permissionsId);
    }

    //region exception handling
    /**
     * Handles all ConstraintViolationException exceptions thrown through the {@link TransactionSystemException} exception.
     *
     * @param ex  {@link TransactionSystemException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
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
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
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
