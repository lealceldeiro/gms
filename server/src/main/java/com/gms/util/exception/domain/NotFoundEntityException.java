package com.gms.util.exception.domain;

import javassist.NotFoundException;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class NotFoundEntityException extends NotFoundException {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 4851392880749714321L;

    /**
     * Creates a new {@link NotFoundEntityException} with a given message.
     *
     * @param msg Message to indicate some additional information.
     */
    public NotFoundEntityException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new {@link NotFoundEntityException} with a given message and an exception indicating a possible reason
     * of this exception.
     *
     * @param msg Message to indicate additional information.
     * @param e   Possible exception that caused this.
     */
    public NotFoundEntityException(final String msg, final Exception e) {
        super(msg, e);
    }

}
