package com.gms.domain;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsEntityTest {

    /**
     * "id" field value.
     */
    private final Long id = 1L;
    /**
     * "version" field value.
     */
    private final Integer version = 1;
    /**
     * Instance of {@link GmsEntity}.
     */
    private GmsEntity entity;
    /**
     * Instance of {@link GmsEntity}.
     */
    private GmsEntity entity2;

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getId() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "id", id);
        assertEquals(entity.getId(), id);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getVersion() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "version", version);
        assertEquals(entity.getVersion(), version);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setId() {
        cleanEntity();

        entity.setId(id);
        assertEquals(id, Long.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "id"))));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setVersion() {
        cleanEntity();

        entity.setVersion(version);
        assertEquals(version, Integer.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "version"))));

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void equalsTest() {
        cleanEntity();
        prepareEntitiesForEqualityTest();

        assertEquals(entity, entity2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.hashCode(), entity2.hashCode());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();
        assertEquals(entity.toString(), entity2.toString());
    }

    private void cleanEntity() {
        entity = new GmsEntity();
        assertEntityValidity(entity);
    }

    private void cleanEntity2() {
        entity2 = new GmsEntity();
        assertEntityValidity(entity2);
    }

    private void assertEntityValidity(final GmsEntity entityArg) {
        assertNull(ReflectionTestUtils.getField(entityArg, "id"));
        assertNull(ReflectionTestUtils.getField(entityArg, "version"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity2);
    }

    private void prepareEntityForEqualityTest(final GmsEntity entityArg) {
        ReflectionTestUtils.setField(entityArg, "id", id);
        ReflectionTestUtils.setField(entityArg, "version", version);
    }

}
