package com.gms.util.exception.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
public class NotFoundEntityExceptionTest {

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void testNotFoundEntityExceptionConstructor() {
        String msg = "test";
        Exception ex = new NullPointerException();

        NotFoundEntityException e = new NotFoundEntityException(msg);
        Assert.assertEquals(msg, e.getMessage());

        e = new NotFoundEntityException(msg, ex);
        Assert.assertEquals(msg + " because of " + ex, e.getMessage());
    }

}
