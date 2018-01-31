package com.gms.util.exception;

import com.gms.util.constant.DefaultConst;
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

    /**
     * According to https://www.postgresql.org/docs/9.2/static/errcodes-appendix.html
     */
    private static final int SQLSTATE_DUPLICATED_VALUES = 23505;

    private ExceptionUtil() {

    }

    public static Object getResponseBodyForGmsGeneralException(GmsGeneralException ex, MessageResolver msg, DefaultConst dc) {
        String suffixCode = ex.finishedOK() ? "request.finished.OK" : "request.finished.KO";
        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()) + ". " + msg.getMessage(suffixCode), dc);
    }

    public static Object getResponseBodyForGmsSecurityException(GmsSecurityException ex, MessageResolver msg, DefaultConst dc) {
        HashMap<String, Object> additionalData = new HashMap<>();
        additionalData.put("timestamp", System.currentTimeMillis());
        additionalData.put("status", HttpStatus.UNAUTHORIZED.value());
        additionalData.put("error", msg.getMessage("security.unauthorized"));
        additionalData.put("path", dc.getApiBasePath() + "/" + ex.getPath());
        return createResponseBodyAsMap(msg.getMessage(ex.getMessage()), dc, additionalData);
    }

    public static Object getResponseBodyForConstraintViolationException(Exception ex, MessageResolver msg, DefaultConst dc) {
        Object resBody = "";
        Throwable cause = null;
        if (ex instanceof TransactionSystemException) {
            cause = ((TransactionSystemException) ex).getRootCause();
        } else if (ex instanceof DataIntegrityViolationException) {
            cause = ((DataIntegrityViolationException) ex).getRootCause();
        }

        if (cause != null) {
            String[] errors = new String[0];
            if (cause instanceof ConstraintViolationException) {
                errors = getErrorsWhenConstraintViolationException(cause, msg);
            } else if (cause instanceof SQLException) {
                errors = getErrorsWhenSQLException(cause, msg);
            }
            HashMap<String, Object> additionalData = new HashMap<>();
            additionalData.put("errors", errors);
            resBody = createResponseBodyAsMap(msg.getMessage("validation.fields.incorrect"), dc, additionalData);
        }
        return resBody;
    }


    private static String[] getErrorsWhenSQLException(Throwable cause, MessageResolver msg) {
        final int state = Integer.parseInt(((SQLException) cause).getSQLState());
        final Iterator<Throwable> th = ((SQLException) cause).iterator();
        ArrayList<String> errList = new ArrayList<>();
        Throwable t;
        String[] pair;
        String field;
        String value;

        while (th.hasNext()) {
            t = th.next();
            pair = t.getMessage().split("Detail:")[1].split("=");
            field = pair[0];
            value = pair[1];
            field = field.substring(field.indexOf('('), field.lastIndexOf(')') + 1);
            value = value.substring(0, value.lastIndexOf(')') + 1);
            if (state == SQLSTATE_DUPLICATED_VALUES) {
                errList.add(msg.getMessage("validation.field.unique", field, value));
            }
        }
        String[] errors = new String[errList.size()];
        return errList.toArray(errors);
    }

    private static String[] getErrorsWhenConstraintViolationException(Throwable cause, MessageResolver msg) {
        final Set<ConstraintViolation<?>> v = ((ConstraintViolationException) cause).getConstraintViolations();
        String[] errors = new String[v.size()];
        int i = 0;
        for (ConstraintViolation<?> cv : v) {
            errors[i++] = msg.getMessage(cv.getMessage(), cv.getPropertyPath().toString());
        }
        return errors;
    }

    public static Map<String, Object> createResponseBodyAsMap(Object o, DefaultConst dc) {
        HashMap<String, Object> r = new HashMap<>();
        r.put(dc.getResMessageHolder(), o);
        return r;
    }

    public static Map<String, Object> createResponseBodyAsMap(Object o, DefaultConst dc, Map<String, Object> additionalDataMap) {
        Map<String, Object> base = createResponseBodyAsMap(o, dc);
        base.putAll(additionalDataMap);

        return base;
    }
}
