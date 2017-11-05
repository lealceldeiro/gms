package com.gmsboilerplatesbng.exception;

public class GmsGeneralException extends Exception {

    private final Boolean finishedOK;

    public GmsGeneralException() {
        super("exception.general");
        this.finishedOK = true;
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
        return this.finishedOK;
    }

}
