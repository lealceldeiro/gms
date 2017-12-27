package com.gms.util.exception;

/**
 * GmsSecurityException
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Dec 21, 2017
 */
public class GmsSecurityException extends SecurityException{

    private final String path;

    public GmsSecurityException(String path) {
        super("security.auth.required");
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
