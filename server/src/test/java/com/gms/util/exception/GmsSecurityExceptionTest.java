package com.gms.util.exception;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
public class GmsSecurityExceptionTest {

    /**
     * A sample request path.
     */
    private String testPath = "/testPath";
    /**
     * A sample request message.
     */
    private static final String MESSAGE = "testMsg";
    /**
     * A sample {@link Throwable}.
     */
    private static final Throwable THROWABLE = new NullPointerException();

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        assertNotEquals(MESSAGE, GmsSecurityException.DEFAULT_MESSAGE);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void gmsSecurityExceptionPath() {
        testPath = "/sample1";
        GmsSecurityException e = new GmsSecurityException(testPath);
        pathIs(e, testPath);
        messageIs(e, GmsSecurityException.DEFAULT_MESSAGE);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void gmsSecurityExceptionPathMessage() {
        testPath = "/sample2";
        GmsSecurityException e = new GmsSecurityException(testPath, MESSAGE);
        pathIs(e, testPath);
        messageIs(e, MESSAGE);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void gmsSecurityExceptionPathMessageThrowable() {
        testPath = "/sample3";
        GmsSecurityException e = new GmsSecurityException(testPath, MESSAGE, THROWABLE);
        pathIs(e, testPath);
        messageIs(e, MESSAGE);
        exceptionIs(e, THROWABLE);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPath() {
        testPath = "/sample34";
        GmsSecurityException e = new GmsSecurityException("somePath");
        ReflectionTestUtils.setField(e, "path", testPath);
        assertEquals(testPath, e.getPath());
    }

    private void messageIs(final GmsSecurityException e, final String message) {
        assertEquals(e.getMessage(), message);
    }

    private void pathIs(final GmsSecurityException e, final String path) {
        assertEquals(ReflectionTestUtils.getField(e, "path"), path);
    }

    private void exceptionIs(final GmsSecurityException e, final Throwable ex) {
        assertEquals(e.getCause(), ex);
    }

}
