package com.gms.util.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsGeneralException extends Exception {

    private final Boolean finishedOK;
    private final HttpStatus httpStatus;
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static final String DEFAULT_MSG = "exception.general";
    public static final Boolean DEFAULT_FINISHED_OK = true;


    public GmsGeneralException() {
        super(DEFAULT_MSG);
        this.finishedOK = DEFAULT_FINISHED_OK;
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public GmsGeneralException(String msg) {
        super(msg);
        this.finishedOK = DEFAULT_FINISHED_OK;
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public GmsGeneralException(Boolean finishedOK) {
        super(DEFAULT_MSG);
        this.finishedOK = finishedOK;
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public GmsGeneralException(String msg, Boolean finishedOK) {
        super(msg);
        this.finishedOK = finishedOK;
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public GmsGeneralException(String msg, Exception e, Boolean finishedOK) {
        super(msg, e);
        this.finishedOK = finishedOK;
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public GmsGeneralException(HttpStatus httpStatus) {
        super(DEFAULT_MSG);
        this.finishedOK = DEFAULT_FINISHED_OK;
        this.httpStatus = httpStatus;
    }

    public GmsGeneralException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.finishedOK = DEFAULT_FINISHED_OK;
        this.httpStatus = httpStatus;
    }

    public GmsGeneralException(Boolean finishedOK, HttpStatus httpStatus) {
        super(DEFAULT_MSG);
        this.finishedOK = finishedOK;
        this.httpStatus = httpStatus;
    }

    public GmsGeneralException(String msg, Boolean finishedOK, HttpStatus httpStatus) {
        super(msg);
        this.finishedOK = finishedOK;
        this.httpStatus = httpStatus;
    }

    public GmsGeneralException(String msg, Exception e, Boolean finishedOK, HttpStatus httpStatus) {
        super(msg, e);
        this.finishedOK = finishedOK;
        this.httpStatus = httpStatus;
    }

    public Boolean finishedOK () {
        return finishedOK;
    }

    public HttpStatus getHttpStatus () {
        return httpStatus;
    }

}
