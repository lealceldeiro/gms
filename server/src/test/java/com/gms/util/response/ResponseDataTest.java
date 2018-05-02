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
public class ResponseDataTest {

    private final String key1 = "key1";
    private final String value1 = "value1";
    private ResponseData<String> entity;
    private ResponseData<String> entity2;

    @Test
    public void ResponseDataAllArgsConstructor() {
        ResponseData<String> rd = new ResponseData<>(key1, value1);
        assertEquals(ReflectionTestUtils.getField(rd, "key"), key1);
        assertEquals(ReflectionTestUtils.getField(rd, "value"), value1);
    }

    @Test
    public void getKey() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "key", key1);
        assertTrue(entity.getKey().equals(key1));
    }

    @Test
    public void getValue() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "value", value1);
        assertTrue(entity.getValue().equals(value1));
    }

    @Test
    public void setKey() {
        cleanEntity();

        entity.setKey(key1);
        assertTrue(key1.equals(String.valueOf(ReflectionTestUtils.getField(entity, "key"))));
    }

    @Test
    public void setValue() {
        cleanEntity();

        entity.setValue(value1);
        assertTrue(value1.equals(String.valueOf(ReflectionTestUtils.getField(entity, "value"))));
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
        entity = new ResponseData<>();
        assertEntityValidity(entity);
    }

    private void cleanEntity2() {
        entity2 = new ResponseData<>();
        assertEntityValidity(entity2);
    }

    private void assertEntityValidity(ResponseData entity) {
        assertNull(ReflectionTestUtils.getField(entity, "key"));
        assertNull(ReflectionTestUtils.getField(entity, "value"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity2);
    }

    private void prepareEntityForEqualityTest(ResponseData e) {
        ReflectionTestUtils.setField(e, "key", key1);
        ReflectionTestUtils.setField(e, "value", value1);
    }
}