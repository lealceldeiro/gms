package com.gms.util.exception.domain;

import com.gms.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
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
