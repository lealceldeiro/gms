package com.gms.testutil;

import java.util.Arrays;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class StringUtil {

    /**
     * Static sample value for "username".
     */
    public static final String EXAMPLE_USERNAME = "ExampleUsername-";
    /**
     * Static sample value for "name".
     */
    public static final String EXAMPLE_NAME = "ExampleName-";
    /**
     * Static sample value for "lastName".
     */
    public static final String EXAMPLE_LAST_NAME = "ExampleLastName-";
    /**
     * Static sample value for "password".
     */
    public static final String EXAMPLE_PASSWORD = "ExamplePassword-";
    /**
     * Static sample value for "label".
     */
    public static final String EXAMPLE_LABEL = "ExampleLabel-";
    /**
     * Static sample value for "email".
     */
    public static final String EXAMPLE_EMAIL = "example-email@test.com";
    /**
     * Static sample value for "description".
     */
    public static final String EXAMPLE_DESCRIPTION = "ExampleDescription-";

    /**
     * Static sample of incorrect values for "username".
     */
    public static final String[] INVALID_USERNAME = {
            "-9randomChars9",
            "-randomChars9",
            "-9randomChars",
            "-randomChars",
            "_random_Chars",
            "_random-Chars",
            "_random Chars whit spaces",

            "9randomChars9-",
            "randomChars9-",
            "9randomChars-",
            "randomChars-",
            "random_Chars_",
            "random-Chars_",

            "random_-Chars-KO",
            "9random_Chars-_KO",
    };

    /**
     * Static sample of correct values for "username".
     */
    public static final String[] VALID_USERNAME = {
            "9randomChars9",
            "randomChars9",
            "9randomChars",
            "randomChars",
            "random_Chars",
            "random-Chars",
            "random_Chars-OK",
            "9random_Chars-OK",
            "9random_Chars-OK9",
    };

    /**
     * Privates constructor to make class un-instantiable.
     */
    private StringUtil() {
    }

    /**
     * Creates a String of specified length with the character {@code j}.
     *
     * @param length Length of the String.
     * @return The String
     */
    public static String createJString(final int length) {
        return createString(length, 'j');
    }

    /**
     * Creates a String of specified length with the specified {@code character}.
     *
     * @param length    Length of the String.
     * @param character Character to be used to generate the string.
     * @return The String
     */
    public static String createString(final int length, final char character) {
        char[] charArray = new char[length];
        Arrays.fill(charArray, character);
        return new String(charArray);
    }

}
