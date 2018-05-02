package com.gms.util.response;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsResponseTest {

    private final String message = "message1";
    private final ResponseData[] rd = { new ResponseData<String>() };
    private GmsResponse entity;
    private GmsResponse entity2;

    @Test
    public void GmsResponseResponseData() {
        ResponseData<String> rd = new ResponseData<>();
        rd.setKey("key1");
        rd.setValue("value1");
        ResponseData[] data = { rd };
        GmsResponse e = new GmsResponse(data);
        assertEquals(ReflectionTestUtils.getField(e, "data"), data);
        assertEquals("", ReflectionTestUtils.getField(e, "message"));
    }

    @Test
    public void getMessage() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "message", message);
        assertTrue(entity.getMessage().equals(message));
    }

    @Test
    public void getData() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "data", rd);
        assertArrayEquals(entity.getData(),rd);
    }

    @Test
    public void setMessage() {
        cleanEntity();

        entity.setMessage(message);
        assertTrue(message.equals(String.valueOf(ReflectionTestUtils.getField(entity, "message"))));

    }

    @Test
    public void equalsTest() {
        cleanEntity();
        prepareEntitiesForEqualityTest();

        assertTrue(entity.equals(entity2));
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.hashCode() == entity2.hashCode());
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();
        assertTrue(entity.toString().equals(entity2.toString()));
    }

    private void cleanEntity() {
        entity = new GmsResponse("", null);
        assertEntityValidity(entity);
    }

    private void cleanEntity2() {
        entity2 = new GmsResponse("", null);
        assertEntityValidity(entity2);
    }

    private void assertEntityValidity(GmsResponse entity) {
        assertTrue(ReflectionTestUtils.getField(entity, "message").toString().equals(""));
        assertNull(ReflectionTestUtils.getField(entity, "data"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity2);
    }

    private void prepareEntityForEqualityTest(GmsResponse e) {
        ReflectionTestUtils.setField(e, "message", message);
        ReflectionTestUtils.setField(e, "data", rd);
    }
}