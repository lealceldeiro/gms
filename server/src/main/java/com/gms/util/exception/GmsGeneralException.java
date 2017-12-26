package com.gms.util.exception;

/**
 * GmsGeneralException
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
public class GmsGeneralException extends Exception {

    private final Boolean finishedOK;


    public GmsGeneralException() {
        super("exception.general");
        this.finishedOK = true;
    }


    public GmsGeneralException(String msg) {
        super(msg);
        this.finishedOK = true;
    }

    public GmsGeneralException(Boolean finishedOK) {
        super("exception.general");
        this.finishedOK = finishedOK;
    }

    public GmsGeneralException(String msg, Boolean finishedOK) {
        super(msg);
        this.finishedOK = finishedOK;
    }

    public GmsGeneralException(String msg, Exception e, Boolean finishedOK) {
        super(msg, e);
        this.finishedOK = finishedOK;
    }

    public Boolean finishedOK () {
        return finishedOK;
    }

}
