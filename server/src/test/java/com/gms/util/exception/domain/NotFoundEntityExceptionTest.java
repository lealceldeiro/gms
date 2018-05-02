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

    @Test
    public void testNotFoundEntityExceptionConstructor() {
        String msg = "test";
        Exception ex = new NullPointerException();

        NotFoundEntityException e = new NotFoundEntityException(msg);
        Assert.assertTrue(e.getMessage().equals(msg));

        e = new NotFoundEntityException(msg, ex);
        Assert.assertTrue(e.getMessage().equals(msg + " because of " + ex.toString()));
    }
}