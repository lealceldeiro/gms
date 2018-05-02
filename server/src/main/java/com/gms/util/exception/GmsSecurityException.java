package com.gms.util.exception;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsSecurityException extends SecurityException{

    private final String path;
    public static final String DEFAULT_MESSAGE = "security.auth.required";

    public GmsSecurityException(String path) {
        super(DEFAULT_MESSAGE);
        this.path = path;
    }

    public GmsSecurityException(String path, String message) {
        super(message);
        this.path = path;
    }
    public GmsSecurityException(String path, String message, Throwable throwable) {
        super(message, throwable);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
