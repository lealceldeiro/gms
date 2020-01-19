package com.gms.testutil.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class PersistenceValidation {

    /**
     * Instance of {@link Validator}.
     */
    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    /**
     * Test the validity of an object against the error messages the validator provides.
     *
     * @param o            Object to be tested.
     * @param errorMessage Error message that must be found on the errors found when this object is invalid.
     * @return {@code true} if the object is invalid and there is an error message equals to the one provided as third
     * parameter. {@code false} otherwise.
     */
    public static boolean isObjectInvalidWithErrorMessage(final Object o, final String errorMessage) {
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

    /**
     * Validates all constraints on object.
     *
     * @param o      Object to validate.
     * @param groups The group or list of groups targeted for validation.
     * @return Constraint violations or an empty set if none.
     */
    public static Set<ConstraintViolation<Object>> validate(final Object o, final Class<?>... groups) {
        return VALIDATOR.validate(o, groups);
    }

    /**
     * Validates all constraints on object.
     *
     * @param o            Object to validate.
     * @param groups       The group or list of groups targeted for validation.
     * @param propertyName Property to validate (i.e. field and getter constraints).
     * @return Constraint violations or an empty set if none.
     */
    public static Set<ConstraintViolation<Object>> validateProperty(final Object o,
                                                                    final String propertyName,
                                                                    final Class<?>... groups) {
        return VALIDATOR.validateProperty(o, propertyName, groups);
    }

    /**
     * Validates all constraints on object.
     *
     * @param beanType     The bean type.
     * @param propertyName Property to validate (i.e. field and getter constraints).
     * @param value        Property value to validate.
     * @param groups       The group or list of groups targeted for validation.
     * @return Constraint violations or an empty set if none.
     */
    public static Set<ConstraintViolation<Object>> validateValue(final Class<Object> beanType,
                                                                 final String propertyName,
                                                                 final Object value, final Class<?>... groups) {
        return VALIDATOR.validateValue(beanType, propertyName, value, groups);
    }

    /**
     * Privates constructor to make class un-instantiable.
     */
    private PersistenceValidation() {
    }

}
