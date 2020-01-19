package com.gms.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A string random generator class borrowed from https://stackoverflow.com/q/41107/5640649 .
 */
public class GMSRandom {

    /**
     * Default length of the generated String.
     */
    private static final int DEFAULT_LENGTH = 21;
    /**
     * Letters in uppercase.
     */
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * Letters in lowercase.
     */
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    /**
     * Numbers as a String.
     */
    private static final String DIGITS = "0123456789";
    /**
     * Alphanumeric string.
     */
    private static final String ALPHANUMERIC = UPPER + LOWER + DIGITS;

    /**
     * A {@link Random} instance.
     */
    private final Random random;
    /**
     * The symbols to be used.
     */
    private final char[] symbols;
    /**
     * The buffer.
     */
    private final char[] buf;

    /**
     * Creates a new {@link GMSRandom} alphanumeric random generator for a given length of string to be generated,
     * with a custom {@link Random} instance and custom symbols.
     *
     * @param length  Length of the string to be generated.
     * @param random  The custom {@link Random} instance to be used.
     * @param symbols The custom {@code symbols} to be used.
     */
    public GMSRandom(final int length, final Random random, final String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least 1");
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException("length of symbols must be at least 2");
        }

        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Creates a new {@link GMSRandom} alphanumeric random generator for a given length of string to be generated and
     * with a custom {@link Random} instance.
     *
     * @param length Length of the string to be generated.
     * @param random The custom {@link Random} instance to be used.
     */
    public GMSRandom(final int length, final Random random) {
        this(length, random, ALPHANUMERIC);
    }

    /**
     * Creates a new {@link GMSRandom} alphanumeric random generator for a given length of string to be generated.
     *
     * @param length Length of the string to be generated.
     */
    public GMSRandom(final int length) {
        this(length, new SecureRandom());
    }

    /**
     * Creates a new {@link GMSRandom} alphanumeric random generator.
     */
    public GMSRandom() {
        this(DEFAULT_LENGTH);
    }

    /**
     * Generate a random string.
     *
     * @return A randomly generated alphanumeric {@link String}.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }

        return new String(buf) + System.currentTimeMillis() + "";
    }

}
