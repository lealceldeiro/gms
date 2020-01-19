package com.gms.util.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Used to create general exceptions.
 * <p>IMPORTANT!</p>
 * <p>Due to the number of different combinations of arguments this exception may get to
 * construct a meaningful message this class must be instantiated using a provided builder.</p>
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
@Builder
@Getter
@Setter
public final class GmsGeneralException extends Exception {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 5034123990490698557L;

    /**
     * Indicates whether the action finished OK or not despite the exception thrown.
     */
    @Builder.Default
    private boolean finishedOK = true;

    /**
     * HttpStatus to be returned because the of the thrown exception.
     */
    @Builder.Default
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    /**
     * i18n code for the default error to be shown when the exception occurs.
     */
    @Builder.Default
    private String messageI18N = "exception.general";

    /**
     * Cause of this exception.
     */
    private Throwable cause;

    @Override
    public String getMessage() {
        return this.messageI18N;
    }

    @Override
    public synchronized Throwable getCause() {
        return cause;
    }

}
