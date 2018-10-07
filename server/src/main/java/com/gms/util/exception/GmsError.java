package com.gms.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@AllArgsConstructor
public class GmsError {

    @Getter
    @Setter
    private Map<String, List<String>> errors;

    public static final String OTHERS = "others";

    GmsError() {
        this.errors = new HashMap<>();
        this.errors.put(OTHERS, new LinkedList<>());
    }

    /**
     * Adds a new error <code>message</code> for the specified <code>field</code>.
     * @param field Field to which the errors belongs.
     * @param message Error message (generally a i18n code).
     */
    public void addError(String field, String message) {
        if (errors.get(field) == null) {
            LinkedList<String> l = new LinkedList<>();
            l.add(message);
            errors.put(field, l);
        } else {
            errors.get(field).add(message);
        }
    }

    /**
     * Adds a new error <code>message</code> for under the key {@link GmsError#OTHERS}.
     * @param message Error message (generally a i18n code).
     */
    public void addError(String message) {
        errors.get(OTHERS).add(message);
    }

    /**
     * Removes all errors fo a <code>field</code>.
     * @param field Field to ve removed the errors from.
     */
    public void removeErrors(String field) {
        errors.remove(field);
    }

    /**
     * Removes an error.
     * @param error Error to be removed.
     */
    public void removeError(String error) {
        errors.get(OTHERS).remove(error);
    }

    /**
     * Removes a <code>message</code> error set in a <code>field</code>.
     * @param field Field to ve removed the error from.
     * @param message Error to be removed.
     */
    public void removeError(String field, String message) {
        if (errors.get(field) != null) {
            errors.get(field).remove(message);
        }
    }

}
