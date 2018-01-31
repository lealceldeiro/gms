package com.gms.domain;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * GmsEntityTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 31, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsEntityTest {

    private final Long id = 1L;
    private final Integer v = 1;
    private GmsEntity entity;
    private GmsEntity entity2;

    @Test
    public void getId() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "id", id);
        assertTrue(entity.getId().equals(id));
    }

    @Test
    public void getVersion() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "version", v);
        assertTrue(entity.getVersion().equals(v));
    }

    @Test
    public void setId() {
        cleanEntity();

        entity.setId(id);
        assertTrue(id.equals(Long.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "id")))));
    }

    @Test
    public void setVersion() {
        cleanEntity();

        entity.setVersion(v);
        assertTrue(v.equals(Integer.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "version")))));

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
        entity = new GmsEntity();
        assertNull(ReflectionTestUtils.getField(entity, "id"));
        assertNull(ReflectionTestUtils.getField(entity, "version"));
    }

    private void cleanEntity2() {
        entity2 = new GmsEntity();
        assertNull(ReflectionTestUtils.getField(entity2, "id"));
        assertNull(ReflectionTestUtils.getField(entity2, "version"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        ReflectionTestUtils.setField(entity, "id", id);
        ReflectionTestUtils.setField(entity, "version", v);
        ReflectionTestUtils.setField(entity2, "id", id);
        ReflectionTestUtils.setField(entity2, "version", v);
    }
}