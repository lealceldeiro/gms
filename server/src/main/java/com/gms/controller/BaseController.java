package com.gms.controller;

import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

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

    protected MessageResolver msg;

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
        Object resBody = createResponseBodyAsMap(msg.getMessage(ex.getMessage()));
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
        HashMap<String, Object> additionalData = new HashMap<>();
        additionalData.put("timestamp", System.currentTimeMillis());
        additionalData.put("status", HttpStatus.UNAUTHORIZED.value());
        additionalData.put("error", msg.getMessage("security.unauthorized"));
        additionalData.put("path", dc.getApiBasePath() + "/" + ex.getPath());
        Object resBody = createResponseBodyAsMap(msg.getMessage(ex.getMessage()), additionalData);
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
        String prefixCode = ex.finishedOK() ? "request.finished.OK" : "request.finished.KO";
        Object resBody = createResponseBodyAsMap(msg.getMessage(ex.getMessage()) + ". " + msg.getMessage(prefixCode));
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, req);
    }

    //endregion

    private HashMap<String, Object> createResponseBodyAsMap(Object o) {
        HashMap<String, Object> r = new HashMap<>();
        r.put(dc.getResMessageHolder(), o);
        return r;
    }

    private HashMap<String, Object> createResponseBodyAsMap(Object o, HashMap<String, Object> additionalDataMap) {
        HashMap<String, Object> base = createResponseBodyAsMap(o);
        base.putAll(additionalDataMap);

        return base;
    }
}
