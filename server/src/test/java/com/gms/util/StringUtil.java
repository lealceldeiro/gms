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
