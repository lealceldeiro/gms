package com.gmsboilerplatesbng.controller;
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
 * Base controller for defining common actions to all controllers in the app
 */
@RestController
@RestControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler{

    //region exceptions handling

    @ExceptionHandler(NotFoundEntityException.class)
    protected ResponseEntity<Object> handleNotFoundEntityException(NotFoundEntityException ex, WebRequest req) {
        String resBody = "Todo: " + ex.getMessage();// todo
        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

    //endregion
}
