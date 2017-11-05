package com.gmsboilerplatesbng.controller;
import com.gmsboilerplatesbng.exception.GmsGeneralException;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Base controller for defining common actions to all controllers in the app.
 */
@RestController
@RestControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler{

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
        String resBody = "Todo: " + ex.getMessage();// todo
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
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
        String resBody = "Todo: " + ex.getMessage()  + ". The request " + (ex.finishedOK() ? "" : "did not ") + "finished OK";// todo
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, req);
    }

    //endregion
}
