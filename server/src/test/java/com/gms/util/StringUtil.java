package com.gms.util;

import java.util.Arrays;

/**
 * StringUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 07, 2018
 */
public class StringUtil {

    public static final String EXAMPLE_USERNAME = "ExampleUsername-";
    public static final String EXAMPLE_NAME = "ExampleName-";
    public static final String EXAMPLE_LAST_NAME = "ExampleLastName-";
    public static final String EXAMPLE_PASSWORD = "ExamplePassword-";
    public static final String EXAMPLE_LABEL = "ExampleLabel-";
    public static final String EXAMPLE_EMAIL = "example-email@test.com";
    public static final String EXAMPLE_DESCRIPTION = "ExampleDescription-";

    private StringUtil() {
    }

    public static String createJString(int length) {
        return createString(length, 'j');
    }

    public static String createString(int length, char character) {
        char[] charArray = new char[length];
        Arrays.fill(charArray, character);
        return new String(charArray);
    }
}
