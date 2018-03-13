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
import java.util.*;

/**
 * ExceptionUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 31, 2018
 */
public class ExceptionUtil {

    private ExceptionUtil() {

    }

    /**
     * Creates the response body for exceptions of type {@link GmsGeneralException}
     * @param ex {@link GmsGeneralException} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Object getResponseBodyForGmsGeneralException(GmsGeneralException ex, MessageResolver msg, DefaultConst dc) {
        String suffixCode = ex.finishedOK() ? "request.finished.OK" : "request.finished.KO";
        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()) + ". " + msg.getMessage(suffixCode), dc);
    }

    /**
     * Creates the response body for exceptions of type {@link GmsSecurityException}
     * @param ex {@link GmsSecurityException} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Map getResponseBodyForGmsSecurityException(GmsSecurityException ex, MessageResolver msg, DefaultConst dc) {
        HashMap<String, Object> additionalData = new HashMap<>();
        additionalData.put("timestamp", System.currentTimeMillis());
        additionalData.put("status", HttpStatus.UNAUTHORIZED.value());
        additionalData.put("error", msg.getMessage("security.unauthorized"));
        additionalData.put("path", (dc.getApiBasePath() + "/" + ex.getPath()).replaceAll("//", "/"));
        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()), dc, additionalData);
    }

    /**
     * Creates the response body for constraint violation exceptions such as {@link TransactionSystemException} and
     * {@link DataIntegrityViolationException} caused by violated domain restrictions.
     * @param ex {@link Exception} for getting the exception data.
     * @param msg {@link MessageResolver} for translating the message.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @return Object with the response data.
     */
    public static Object getResponseBodyForConstraintViolationException(Exception ex, MessageResolver msg, DefaultConst dc) {
        Object resBody = "";
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
            HashMap<String, Object> additionalData = new HashMap<>();
            additionalData.put("errors", gmsError.getErrors());
            resBody = createResponseBodyAsMap(msg.getMessage("validation.fields.incorrect"), dc, additionalData);
        }
        return resBody;
    }


    private static GmsError getErrorsWhenSQLException(Throwable cause, MessageResolver msg) {
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
                    // for now, just add the error
                    gmsError.addError(t.getLocalizedMessage());
                    break;
            }
        }
        return gmsError;
    }

    private static GmsError getErrorsWhenConstraintViolationException(Throwable cause, MessageResolver msg) {
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

            gmsError.addError(cv.getPropertyPath().toString(), msg.getMessage(cv.getMessage(), args.toArray(new String[args.size()])));
        }
        return gmsError;
    }

    /**
     * Creates a response body as a {@link Map} to be sent as response body when handling exceptions.
     * @param o Object with useful information.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @return A {@link Map} with keys retrieved from the <code>dc</code> object holding the object <code>o</code>.
     */
    public static Map<String, Object> createResponseBodyAsMap(Object o, DefaultConst dc) {
        HashMap<String, Object> r = new HashMap<>();
        r.put(dc.getResMessageHolder(), o);
        return r;
    }

    /**
     * Creates a response body as a {@link Map} to be sent as response body when handling exceptions.
     * @param o Object with useful information.
     * @param dc {@link DefaultConst} in order to get the variable placeholders.
     * @param additionalDataMap Additional information to be put beside the <code>o</code> param.
     * @return A {@link Map} with keys retrieved from the <code>dc</code> object holding the object <code>o</code>.
     */
    private static Map<String, Object> createResponseBodyAsMap(Object o, DefaultConst dc, Map<String, Object> additionalDataMap) {
        Map<String, Object> base = createResponseBodyAsMap(o, dc);
        base.putAll(additionalDataMap);

        return base;
    }
}
