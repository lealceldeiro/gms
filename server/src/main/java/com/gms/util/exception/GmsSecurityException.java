package com.gms.util.exception;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsSecurityException extends SecurityException {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 4679170124696702679L;

    /**
     * Path where the request was performed when the exception was thrown.
     */
    private final String path;

    /**
     * i18n code for the default error to be shown when the exception occurs.
     */
    public static final String DEFAULT_MESSAGE = "security.auth.required";

    /**
     * Creates a new {@link GmsSecurityException} from the given argument.
     *
     * @param path A {@link String} representing the path from whicht he exception was caused
     */
    public GmsSecurityException(final String path) {
        super(DEFAULT_MESSAGE);
        this.path = path;
    }

    /**
     * Creates a new {@link GmsSecurityException} from the given argument.
     *
     * @param path    A {@link String} representing the path from which he exception was caused.
     * @param message i18n code to retrieve the error to be shown when the exception occurs.
     */
    public GmsSecurityException(final String path, final String message) {
        super(message);
        this.path = path;
    }

    /**
     * Creates a new {@link GmsSecurityException} from the given argument.
     *
     * @param path      A {@link String} representing the path from which he exception was caused
     * @param message   i18n code to retrieve the error to be shown when the exception occurs.
     * @param throwable Cause of this exception.
     */
    public GmsSecurityException(final String path, final String message, final Throwable throwable) {
        super(message, throwable);
        this.path = path;
    }

    /**
     * Returns the path from which he exception was caused.
     *
     * @return A {@link String} with the path.
     */
    public String getPath() {
        return path;
    }

}
