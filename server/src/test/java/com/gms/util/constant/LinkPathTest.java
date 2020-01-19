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

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getWhat() {
        Object field = ReflectionTestUtils.getField(LinkPath.class, "LINK");
        String link = field != null ? field.toString() : "";
        field = ReflectionTestUtils.getField(LinkPath.class, "HREF");
        String href = field != null ? field.toString() : "";
        Assert.assertEquals(LinkPath.get("test"), String.format("%s.%s.%s", link, "test", href));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getSelf() {
        Object field = ReflectionTestUtils.getField(LinkPath.class, "LINK");
        String link = field != null ? field.toString() : "";
        field = ReflectionTestUtils.getField(LinkPath.class, "HREF");
        String href = field != null ? field.toString() : "";
        field = ReflectionTestUtils.getField(LinkPath.class, "SELF");
        String self = field != null ? field.toString() : "";
        Assert.assertEquals(LinkPath.get(self), String.format("%s.%s.%s", link, self, href));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void get() {
        Object field = ReflectionTestUtils.getField(LinkPath.class, "LINK");
        String link = field != null ? field.toString() : "";
        field = ReflectionTestUtils.getField(LinkPath.class, "HREF");
        String href = field != null ? field.toString() : "";
        field = ReflectionTestUtils.getField(LinkPath.class, "SELF");
        String self = field != null ? field.toString() : "";
        Assert.assertEquals(LinkPath.get(self), String.format("%s.%s.%s", link, self, href));
    }

}
