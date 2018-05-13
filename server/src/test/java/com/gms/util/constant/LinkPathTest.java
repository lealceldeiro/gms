package com.gms.util.constant;

import com.gms.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LinkPathTest {

    @Test
    public void getWhat() {
        String link = ReflectionTestUtils.getField(LinkPath.class, "LINK").toString();
        String href = ReflectionTestUtils.getField(LinkPath.class, "HREF").toString();
        Assert.assertEquals(LinkPath.get("test"), String.format("%s.%s.%s", link, "test", href) );
    }

    @Test
    public void getSelf() {
        String link = ReflectionTestUtils.getField(LinkPath.class, "LINK").toString();
        String href = ReflectionTestUtils.getField(LinkPath.class, "HREF").toString();
        String self = ReflectionTestUtils.getField(LinkPath.class, "SELF").toString();
        Assert.assertEquals(LinkPath.get(self), String.format("%s.%s.%s", link, self, href) );
    }

    @Test
    public void get() {
        String link = ReflectionTestUtils.getField(LinkPath.class, "LINK").toString();
        String href = ReflectionTestUtils.getField(LinkPath.class, "HREF").toString();
        String self = ReflectionTestUtils.getField(LinkPath.class, "SELF").toString();
        Assert.assertEquals(LinkPath.get(), String.format("%s.%s.%s", link, self, href) );
    }
}