package com.gmsboilerplatesbng.util.exception.domain;

import javassist.NotFoundException;

public class NotFoundEntityException extends NotFoundException {

    public NotFoundEntityException(String msg) {
        super(msg);
    }

    public NotFoundEntityException(String msg, Exception e) {
        super(msg, e);
    }
}
