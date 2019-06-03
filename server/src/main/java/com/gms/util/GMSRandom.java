package com.gms.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A string random generator class borrowed from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
 */
public class GMSRandom {

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf).concat(System.currentTimeMillis() + "");
    }

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);

    public static final String DIGITS = "0123456789";

    public static final String ALPHANUMERIC = UPPER + LOWER + DIGITS;

    private Random random;

    private char[] symbols;

    private char[] buf;

    public GMSRandom(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public GMSRandom(int length, Random random) {
        this(length, random, ALPHANUMERIC);
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
