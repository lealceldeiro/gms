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

	public NotFoundEntityException(String msg) {
        super(msg);
    }

    public NotFoundEntityException(String msg, Exception e) {
        super(msg, e);
    }

}
