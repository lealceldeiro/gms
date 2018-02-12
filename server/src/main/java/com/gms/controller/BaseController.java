package com.gms.controller;

import com.gms.component.security.authentication.AuthenticationFacade;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * BaseController
 * Base controller for defining common actions to all controllers in the app.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RestController
@RestControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler {

    @Autowired protected MessageResolver msg;
    @Autowired private AuthenticationFacade authenticationFacade;

    @SuppressWarnings("WeakerAccess")
    protected final DefaultConst dc;

    public BaseController(DefaultConst defaultConst) {
        this.dc = defaultConst;
    }

    //region exceptions handling

    /**
     * Handles all not found exceptions which refers to domain entities.
     * @param ex {@link NotFoundEntityException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(NotFoundEntityException.class)
    protected ResponseEntity<Object> handleNotFoundEntityException(NotFoundEntityException ex, WebRequest req) {
        Object resBody = ExceptionUtil.createResponseBodyAsMap(msg.getMessage(ex.getMessage()), dc);
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

    /**
     * Handles all custom security exceptions.
     * @param ex {@link com.gms.util.exception.GmsSecurityException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(GmsSecurityException.class)
    protected ResponseEntity<Object> handleGmsSecurityException(GmsSecurityException ex, WebRequest req) {
        authenticationFacade.setAuthentication(null);
        Object resBody = ExceptionUtil.getResponseBodyForGmsSecurityException(ex, msg, dc);
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.UNAUTHORIZED, req);
    }

    /**
     * Handles all exceptions which where not specifically treated.
     * @param ex {@link GmsGeneralException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
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

    //endregion

    private ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest req) {
        Object resBody = ExceptionUtil.getResponseBodyForConstraintViolationException(ex, msg, dc);
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, req);
    }

}
