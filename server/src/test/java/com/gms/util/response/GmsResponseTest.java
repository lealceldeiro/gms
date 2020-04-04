package com.gms.util.response;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
public class GmsResponseTest {

    /**
     * A sample message value.
     */
    private static final String MESSAGE_VALUE = "message1";
    /**
     * A sample message text.
     */
    private static final String MESSAGE_TEXT = "message";
    /**
     * A sample {@link ResponseData}.
     */
    private final ResponseData<?>[] responseData = {new ResponseData<String>()};
    /**
     * Instance of {@link GmsResponse}.
     */
    private GmsResponse entity;
    /**
     * Instance of {@link GmsResponse}.
     */
    private GmsResponse entity2;

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void gmsResponseResponseData() {
        ResponseData<String> responseData1 = new ResponseData<>();
        responseData1.setKey("key1");
        responseData1.setValue("value1");
        ResponseData<?>[] data = {responseData1};
        GmsResponse e = new GmsResponse(data);
        assertEquals(ReflectionTestUtils.getField(e, "data"), data);
        assertEquals("", ReflectionTestUtils.getField(e, MESSAGE_TEXT));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getMessage() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, MESSAGE_TEXT, MESSAGE_VALUE);
        assertEquals(MESSAGE_VALUE, entity.getMessage());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getData() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "data", responseData);
        assertArrayEquals(entity.getData(), responseData);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setMessage() {
        cleanEntity();

        entity.setMessage(MESSAGE_VALUE);
        assertEquals(MESSAGE_VALUE, String.valueOf(ReflectionTestUtils.getField(entity, MESSAGE_TEXT)));

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
        entity = new GmsResponse("", null);
        assertEntityValidity(entity);
    }

    private void cleanEntity2() {
        entity2 = new GmsResponse("", null);
        assertEntityValidity(entity2);
    }

    private void assertEntityValidity(final GmsResponse response) {
        Object field = ReflectionTestUtils.getField(response, "message");
        assertNotNull(field);
        assertEquals("", field.toString());
        assertNull(ReflectionTestUtils.getField(response, "data"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity2);
    }

    private void prepareEntityForEqualityTest(final GmsResponse response) {
        ReflectionTestUtils.setField(response, "message", MESSAGE_VALUE);
        ReflectionTestUtils.setField(response, "data", responseData);
    }

}
