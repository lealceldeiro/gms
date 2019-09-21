package com.gms.util.exception;

import com.gms.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsSecurityExceptionTest {

    private static String testPath = "/testPath";
    private static final String msg = "testMsg";
    private static final Throwable th = new NullPointerException();

    @Before
    public void setUp() {
        assertNotEquals(msg, GmsSecurityException.DEFAULT_MESSAGE);
    }

    @Test
    public void GmsSecurityExceptionPath() {
        testPath = "/sample1";
        GmsSecurityException e = new GmsSecurityException(testPath);
        pathIs(e, testPath);
        messageIs(e, GmsSecurityException.DEFAULT_MESSAGE);
    }

    @Test
    public void GmsSecurityExceptionPathMessage() {
        testPath = "/sample2";
        GmsSecurityException e = new GmsSecurityException(testPath, msg);
        pathIs(e, testPath);
        messageIs(e, msg);
    }

    @Test
    public void GmsSecurityExceptionPathMessageThrowable() {
        testPath = "/sample3";
        GmsSecurityException e = new GmsSecurityException(testPath, msg, th);
        pathIs(e, testPath);
        messageIs(e, msg);
        exceptionIs(e, th);
    }

    @Test
    public void getPath() {
        testPath = "/sample34";
        GmsSecurityException e = new GmsSecurityException("somePath");
        ReflectionTestUtils.setField(e, "path", testPath);
        assertEquals(testPath, e.getPath());
    }

    private void messageIs(GmsSecurityException e, String message) {
        assertEquals(e.getMessage(), message);
    }

    private void pathIs(GmsSecurityException e, String path) {
        assertEquals(ReflectionTestUtils.getField(e, "path"), path);
    }

    private void exceptionIs(GmsSecurityException e, Throwable ex) {
        assertEquals(e.getCause(), ex);
    }
}