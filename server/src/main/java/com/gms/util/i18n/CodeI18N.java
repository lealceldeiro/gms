package com.gms.util.i18n;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class CodeI18N {

    /**
     * Code for the message indicating a field must not be blank.
     */
    public static final String FIELD_NOT_BLANK = "validation.field.notBlank";

    /**
     * Code for the message indicating a field must not be null.
     */
    public static final String FIELD_NOT_NULL = "validation.field.notNull";

    /**
     * Code for the message indicating a field must not be of the specified size.
     */
    public static final String FIELD_SIZE = "validation.field.size";

    /**
     * Code for the message indicating a field must not be in the provided shape. It means the field is not well-formed.
     * This is a common scenario when entering new email addresses.
     */
    public static final String FIELD_NOT_WELL_FORMED = "validation.field.not.well-formed";

    /**
     * Code for the message indicating a field does not fulfill the username patter. This pattern is not only applicable
     * to usernames, but to many others strings.
     */
    public static final String FIELD_PATTERN_INCORRECT_USERNAME = "validation.field.incorrect.pattern.username";

    private CodeI18N() {}

    public static CodeI18N getInstance() {
        return new CodeI18N();
    }

}
