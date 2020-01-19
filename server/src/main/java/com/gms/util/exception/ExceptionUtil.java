package com.gms.util.exception;

import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SQLExceptionCode;
import com.gms.util.i18n.MessageResolver;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    /**
     * Creates the response body for exceptions of type {@link GmsGeneralException}.
     *
     * @param ex  {@link GmsGeneralException} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc  {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Object getResponseBodyForGmsGeneralException(final GmsGeneralException ex, final MessageResolver msg,
                                                               final DefaultConst dc) {
        String suffixCode = ex.isFinishedOK() ? "request.finished.OK" : "request.finished.KO";

        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()) + ". " + msg.getMessage(suffixCode), dc);
    }

    /**
     * Creates the response body for exceptions of type {@link GmsSecurityException}.
     *
     * @param ex  {@link GmsSecurityException} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc  {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Map<String, Object> getResponseBodyForGmsSecurityException(final GmsSecurityException ex,
                                                                             final MessageResolver msg,
                                                                             final DefaultConst dc) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("timestamp", System.currentTimeMillis());
        additionalData.put("status", HttpStatus.UNAUTHORIZED.value());
        additionalData.put("error", msg.getMessage("security.unauthorized"));
        additionalData.put("path", (dc.getApiBasePath() + "/" + ex.getPath()).replaceAll("//", "/"));
        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()), dc, additionalData);
    }

    /**
     * Creates the response body for constraint violation exceptions such as {@link TransactionSystemException} and
     * {@link DataIntegrityViolationException} caused by violated domain restrictions.
     *
     * @param ex  {@link Exception} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc  {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Object getResponseBodyForConstraintViolationException(final Exception ex, final MessageResolver msg,
                                                                        final DefaultConst dc) {
        Object responseBody = "";
        Throwable cause = null;
        if (ex instanceof TransactionSystemException) {
            cause = ((TransactionSystemException) ex).getRootCause();
        } else if (ex instanceof DataIntegrityViolationException) {
            cause = ((DataIntegrityViolationException) ex).getRootCause();
        }

        if (cause != null) {
            GmsError gmsError = new GmsError();
            if (cause instanceof ConstraintViolationException) {
                gmsError = getErrorsWhenConstraintViolationException(cause, msg);
            } else if (cause instanceof SQLException) {
                gmsError = getErrorsWhenSQLException(cause, msg);
            }
            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("errors", gmsError.getErrors());
            responseBody = createResponseBodyAsMap(msg.getMessage("validation.fields.incorrect"), dc, additionalData);
        }

        return responseBody;
    }


    private static GmsError getErrorsWhenSQLException(final Throwable cause, final MessageResolver msg) {
        final String state = ((SQLException) cause).getSQLState();
        final Iterator<Throwable> th = ((SQLException) cause).iterator();
        GmsError gmsError = new GmsError();
        Throwable t;
        String[] pair;
        String field;
        String value;

        while (th.hasNext()) {
            t = th.next();
            switch (state) {
                case SQLExceptionCode.UNIQUE_VIOLATION:
                    pair = t.getMessage().split("Detail:")[1].split("=");
                    field = pair[0];
                    value = pair[1];
                    field = field.substring(field.indexOf('(') + 1, field.lastIndexOf(')'));
                    value = value.substring(1, value.lastIndexOf(')'));
                    gmsError.addError(field, msg.getMessage("validation.field.unique", value, field));
                    break;
                case SQLExceptionCode.STRING_DATA_RIGHT_TRUNCATION:
                    gmsError.addError(t.getLocalizedMessage());
                    break;
                default:
                    // for now, just add the error as in case SQLExceptionCode.STRING_DATA_RIGHT_TRUNCATION
                    break;
            }
        }

        return gmsError;
    }

    private static GmsError getErrorsWhenConstraintViolationException(final Throwable cause,
                                                                      final MessageResolver msg) {
        final Set<ConstraintViolation<?>> v = ((ConstraintViolationException) cause).getConstraintViolations();
        GmsError gmsError = new GmsError();
        Object aux1;
        Object aux2;
        LinkedList<String> args;

        for (ConstraintViolation<?> cv : v) {
            args = new LinkedList<>();
            args.add(cv.getPropertyPath().toString());
            aux1 = cv.getConstraintDescriptor().getAttributes().get("min");
            if (aux1 != null) {
                args.add(aux1.toString());
            }
            aux2 = cv.getConstraintDescriptor().getAttributes().get("max");
            if (aux2 != null) {
                args.add(aux2.toString());
            }

            gmsError.addError(
                    cv.getPropertyPath().toString(),
                    msg.getMessage(
                            cv.getMessage(),
                            args.toArray(new String[0])
                    )
            );
        }

        return gmsError;
    }

    /**
     * Creates a response body as a {@link Map} to be sent as response body when handling exceptions.
     *
     * @param o  Object with useful information.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @return A {@link Map} with keys retrieved from the {@code dc} object holding the object {@code o}.
     */
    public static Map<String, Object> createResponseBodyAsMap(final Object o, final DefaultConst dc) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(dc.getResMessageHolder(), o);

        return responseBody;
    }

    /**
     * Creates a response body as a {@link Map} to be sent as response body when handling exceptions.
     *
     * @param o                 Object with useful information.
     * @param dc                {@link DefaultConst} in order to get the variable placeholders.
     * @param additionalDataMap Additional information to be put beside the {@code o} param.
     * @return A {@link Map} with keys retrieved from the {@code dc} object holding the object {@code o}.
     */
    private static Map<String, Object> createResponseBodyAsMap(final Object o, final DefaultConst dc,
                                                               final Map<String, Object> additionalDataMap) {
        Map<String, Object> responseBodyAsMap = createResponseBodyAsMap(o, dc);
        responseBodyAsMap.putAll(additionalDataMap);

        return responseBodyAsMap;
    }

}
