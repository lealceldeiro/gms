package com.gms.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * HibernateValidationUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 02, 2018
 */
public class PersistenceValidation {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Test the validity of an object against the error messages the validator provides.
     *
     * @param o            Object to be tested.
     * @param errorMessage Error message that must be found on the errors found when this object is invalid.
     * @return <code>true</code> if the object is invalid and there is an error message equals to the one provided as third
     * parameter. <code>false</code> otherwise.
     */
    public static boolean objectIsInvalidWithErrorMessage(Object o, String errorMessage) {
        final Iterator<ConstraintViolation<Object>> cv = validate(o).iterator();
        boolean ok = false;
        ConstraintViolation<Object> v;
        while (cv.hasNext() && !ok) {
            v = cv.next();
            if (v.getMessage().equals(errorMessage)) {
                ok = true;
            }
        }
        return ok;
    }

    public static Set<ConstraintViolation<Object>> validate(Object o, Class<?>... groups) {
        return validator.validate(o, groups);
    }

    public static Set<ConstraintViolation<Object>> validateProperty(Object o, String propertyName, Class<?>... groups) {
        return validator.validateProperty(o, propertyName, groups);
    }

    public static Set<ConstraintViolation<Object>> validateValue(Class<Object> beanType, String propertyName, Object value, Class<?>... groups) {
        return validator.validateValue(beanType, propertyName, value, groups);
    }
}
