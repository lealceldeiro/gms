package com.gms.util.exception;

import com.gms.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class GmsGeneralExceptionTest {

    private static final String msg = "testmsg";
    private static final Boolean finishedOk = false;
    private static final Exception ex = new NullPointerException();
    private static final HttpStatus status = HttpStatus.BAD_GATEWAY;

    @Before
    public void setUp() {
        // guarantee test values differ from default class values
        assertNotEquals(status, GmsGeneralException.DEFAULT_HTTP_STATUS);
        assertNotEquals(msg, GmsGeneralException.DEFAULT_MSG);
        assertNotEquals(finishedOk, GmsGeneralException.DEFAULT_FINISHED_OK);
    }

    @Test
    public void GmsGeneralExceptionNoArgs() {
        GmsGeneralException e = new GmsGeneralException();
        finishedOkIs(e, GmsGeneralException.DEFAULT_FINISHED_OK);
        messageIs(e, GmsGeneralException.DEFAULT_MSG);
        httpStatusIs(e, GmsGeneralException.DEFAULT_HTTP_STATUS);
    }

    @Test
    public void GmsGeneralExceptionMsg() {
        GmsGeneralException e = new GmsGeneralException(msg);
        finishedOkIs(e, GmsGeneralException.DEFAULT_FINISHED_OK);
        messageIs(e, msg);
        httpStatusIs(e, GmsGeneralException.DEFAULT_HTTP_STATUS);
    }

    @Test
    public void GmsGeneralExceptionFinishedOK() {
        GmsGeneralException e = new GmsGeneralException(finishedOk);
        finishedOkIs(e, finishedOk);
        messageIs(e, GmsGeneralException.DEFAULT_MSG);
        httpStatusIs(e, GmsGeneralException.DEFAULT_HTTP_STATUS);
    }

    @Test
    public void GmsGeneralExceptionMsgFinishedOK() {
        GmsGeneralException e = new GmsGeneralException(msg, finishedOk);
        finishedOkIs(e, finishedOk);
        messageIs(e, msg);
        httpStatusIs(e, GmsGeneralException.DEFAULT_HTTP_STATUS);
    }

    @Test
    public void GmsGeneralExceptionMsgExFinishedOK() {
        GmsGeneralException e = new GmsGeneralException(msg, ex, finishedOk);
        finishedOkIs(e, finishedOk);
        messageIs(e, msg);
        httpStatusIs(e, GmsGeneralException.DEFAULT_HTTP_STATUS);
        exceptionIs(e, ex);
    }

    @Test
    public void GmsGeneralExceptionHttpStatus() {
        GmsGeneralException e = new GmsGeneralException(status);
        finishedOkIs(e, GmsGeneralException.DEFAULT_FINISHED_OK);
        messageIs(e, GmsGeneralException.DEFAULT_MSG);
        httpStatusIs(e, status);
    }

    @Test
    public void GmsGeneralExceptionFinishedOKHttpStatus() {
        GmsGeneralException e = new GmsGeneralException(finishedOk, status);
        finishedOkIs(e, finishedOk);
        messageIs(e, GmsGeneralException.DEFAULT_MSG);
        httpStatusIs(e, status);
    }

    @Test
    public void GmsGeneralExceptionMsgFinishedOKHttpStatus() {
        GmsGeneralException e = new GmsGeneralException(msg, finishedOk, status);
        finishedOkIs(e, finishedOk);
        messageIs(e, msg);
        httpStatusIs(e, status);
    }

    @Test
    public void GmsGeneralExceptionMsgExFinishedOKHttpStatus() {
        GmsGeneralException e = new GmsGeneralException(msg, ex, finishedOk, status);
        finishedOkIs(e, finishedOk);
        messageIs(e, msg);
        httpStatusIs(e, status);
        exceptionIs(e, ex);
    }

    @Test
    public void GmsGeneralExceptionMsgHttpStatus() {
        GmsGeneralException e = new GmsGeneralException(msg, status);
        finishedOkIs(e, GmsGeneralException.DEFAULT_FINISHED_OK);
        messageIs(e, msg);
        httpStatusIs(e, status);
    }

    @Test
    public void finishedOK() {
        GmsGeneralException e = new GmsGeneralException();
        ReflectionTestUtils.setField(e, "finishedOK", finishedOk);
        assertEquals(finishedOk, e.finishedOK());
    }

    @Test
    public void getHttpStatus() {
        GmsGeneralException e = new GmsGeneralException();
        ReflectionTestUtils.setField(e, "httpStatus", status);
        assertEquals(status, e.getHttpStatus());
    }

    private void finishedOkIs(GmsGeneralException e, Boolean what) {
        assertEquals(Boolean.valueOf(ReflectionTestUtils.getField(e, "finishedOK").toString()), what);
    }

    private void httpStatusIs(GmsGeneralException e, HttpStatus status) {
        assertEquals(ReflectionTestUtils.getField(e, "httpStatus"), status);
    }

    private void messageIs(GmsGeneralException e, String message) {
        assertEquals(e.getMessage(), message);
    }

    private void exceptionIs(GmsGeneralException e, Exception ex) {
        assertEquals(e.getCause(), ex);
    }
}