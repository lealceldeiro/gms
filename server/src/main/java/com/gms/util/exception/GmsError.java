package com.gms.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GmsError
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 01, 2018
 */
@AllArgsConstructor
public class GmsError {

    @Getter
    @Setter
    private Map<String, List<String>> errors;

    GmsError() {
        this.errors = new HashMap<>();
    }

    public void addError(String field, String message) {
        if (errors.get(field) == null) {
            ArrayList<String> l = new ArrayList<>(1);
            l.add(message);
            errors.put(field, l);
        } else {
            errors.get(field).add(message);
        }
    }

    public void removeError(String field) {
        errors.remove(field);
    }

    public void removeError(String field, String message) {
        if (errors.get(field) != null) {
            errors.get(field).remove(message);
        }
    }
}
