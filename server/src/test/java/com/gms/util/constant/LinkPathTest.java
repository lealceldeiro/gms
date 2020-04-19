package com.gms.util.constant;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
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
