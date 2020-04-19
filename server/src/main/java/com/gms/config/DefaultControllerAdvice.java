package com.gms.config;

import com.gms.config.security.authentication.AuthenticationFacade;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Default controller for handling exceptions in the app.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
@RequiredArgsConstructor
@Controller
@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Instance of {@link MessageResolver}.
     */
    private final MessageResolver msg;
    /**
     * nstance of {@link AuthenticationFacade}.
     */
    private final AuthenticationFacade authenticationFacade;
    /**
     * nstance of {@link DefaultConst}.
     */
    private final DefaultConst dc;

    //region exceptions handling

    /**
     * Handles all not found exceptions which refers to domain entities.
     *
     * @param ex  {@link NotFoundEntityException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<Object> handleNotFoundEntityException(final NotFoundEntityException ex,
                                                                final WebRequest req) {
        Object resBody = ExceptionUtil.createResponseBodyAsMap(msg.getMessage(ex.getMessage()), dc);

        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

    /**
     * Handles all custom security exceptions.
     *
     * @param ex  {@link com.gms.util.exception.GmsSecurityException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(GmsSecurityException.class)
    public ResponseEntity<Object> handleGmsSecurityException(final GmsSecurityException ex, final WebRequest req) {
        authenticationFacade.setAuthentication(null);
        Object resBody = ExceptionUtil.getResponseBodyForGmsSecurityException(ex, msg, dc);

        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.UNAUTHORIZED, req);
    }

    /**
     * Handles all exceptions which where not specifically treated.
     *
     * @param ex  {@link GmsGeneralException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(GmsGeneralException.class)
    public ResponseEntity<Object> handleGmsGeneralException(final GmsGeneralException ex, final WebRequest req) {
        Object resBody = ExceptionUtil.getResponseBodyForGmsGeneralException(ex, msg, dc);

        return handleExceptionInternal(ex, resBody, new HttpHeaders(), ex.getHttpStatus(), req);
    }


    /**
     * Handles all {@link javax.validation.ConstraintViolationException} exceptions thrown through the
     * {@link TransactionSystemException} exception.
     *
     * @param ex  {@link TransactionSystemException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(final TransactionSystemException ex,
                                                                   final WebRequest req) {
        return handleConstraintViolationException(ex, req);
    }

    /**
     * Handles all {@link javax.validation.ConstraintViolationException} exceptions thrown through the
     * {@link DataIntegrityViolationException} exception.
     *
     * @param ex  {@link DataIntegrityViolationException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex,
                                                                        final WebRequest req) {
        return handleConstraintViolationException(ex, req);
    }

    /**
     * Handles all {@link InvalidDataAccessApiUsageException} exceptions thrown through the
     * {@link DataIntegrityViolationException} exception.
     *
     * @param ex  {@link DataIntegrityViolationException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format
     * (i.e.: json, xml) containing detailed information about the exception.
     */
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(final InvalidDataAccessApiUsageException ex,
                                                                           final WebRequest req) {
        return handleExceptionInternal(ex,
                ExceptionUtil.createResponseBodyAsMap(msg.getMessage("validation.fields.incorrect"), dc),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                req
        );
    }

    //endregion

    private ResponseEntity<Object> handleConstraintViolationException(final Exception ex, final WebRequest req) {
        Object resBody = ExceptionUtil.getResponseBodyForConstraintViolationException(ex, msg, dc);

        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, req);
    }

}
