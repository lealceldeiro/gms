package com.gms.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A string random generator class borrowed from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
 */
@SuppressWarnings("WeakerAccess")
public class GMSRandom {


    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf).concat(System.currentTimeMillis() + "");
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private static Random random;

    private static char[] symbols;

    private static char[] buf;

    public GMSRandom(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        GMSRandom.random = Objects.requireNonNull(random);
        GMSRandom.symbols = symbols.toCharArray();
        GMSRandom.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public GMSRandom(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public GMSRandom(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public GMSRandom() {
        this(21);
    }


}
