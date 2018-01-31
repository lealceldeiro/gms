package com.gms.domain.configuration;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * BConfigurationTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 31, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BConfigurationTest {

    private final String k = "sampleK";
    private final String v = "sampleV";
    private final Long id = 1L;
    private BConfiguration entity;
    private BConfiguration entity1;

    @Test
    public void setKey() {
        cleanEntity();

        entity.setKey(k);
        assertTrue(k.equals(String.valueOf(ReflectionTestUtils.getField(entity, "key"))));
    }

    @Test
    public void setValue() {
        cleanEntity();

        entity.setValue(v);
        assertTrue(v.equals(String.valueOf(ReflectionTestUtils.getField(entity, "value"))));
    }

    @Test
    public void setUserId() {
        cleanEntity();

        entity.setUserId(id);
        assertTrue(id.equals(Long.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "userId")))));
    }

    @Test
    public void getKey() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "key", k);
        assertTrue(entity.getKey().equals(k));
    }

    @Test
    public void getValue() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "value", v);
        assertTrue(entity.getValue().equals(v));
    }

    @Test
    public void getUserId() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "userId", id);
        assertTrue(entity.getUserId().equals(id));
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.hashCode() == entity1.hashCode());
    }

    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.equals(entity1));
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.toString().equals(entity1.toString()));
    }

    private void cleanEntity() {
        entity = new BConfiguration();
        assertNull(ReflectionTestUtils.getField(entity, "key"));
        assertNull(ReflectionTestUtils.getField(entity, "value"));
        assertNull(ReflectionTestUtils.getField(entity, "userId"));
    }

    private void cleanEntity1() {
        entity1 = new BConfiguration();
        assertNull(ReflectionTestUtils.getField(entity1, "key"));
        assertNull(ReflectionTestUtils.getField(entity1, "value"));
        assertNull(ReflectionTestUtils.getField(entity1, "userId"));
    }


    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity1();

        ReflectionTestUtils.setField(entity, "key", k);
        ReflectionTestUtils.setField(entity, "value", v);
        ReflectionTestUtils.setField(entity, "userId", id);
        ReflectionTestUtils.setField(entity1, "key", k);
        ReflectionTestUtils.setField(entity1, "value", v);
        ReflectionTestUtils.setField(entity1, "userId", id);
    }
}