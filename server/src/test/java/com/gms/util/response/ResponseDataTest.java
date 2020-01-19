package com.gms.util.response;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ResponseDataTest {

    /**
     * A sample key.
     */
    private static final String KEY1 = "key1";
    /**
     * A sample value.
     */
    private static final String VALUE1 = "value1";
    /**
     * A sample key.
     */
    private static final String KEY_TEXT = "key";
    /**
     * A sample value.
     */
    private static final String VALUE_TEXT = "value";
    /**
     * Instance of {@link ResponseData} of type {@link String}.
     */
    private ResponseData<String> entity;
    /**
     * Instance of {@link ResponseData} of type {@link String}.
     */
    private ResponseData<String> entity2;

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void responseDataAllArgsConstructor() {
        ResponseData<String> rd = new ResponseData<>(KEY1, VALUE1);
        assertEquals(KEY1, ReflectionTestUtils.getField(rd, KEY_TEXT));
        assertEquals(VALUE1, ReflectionTestUtils.getField(rd, VALUE_TEXT));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getKey() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "key", KEY1);
        assertEquals(KEY1, entity.getKey());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getValue() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "value", VALUE1);
        assertEquals(VALUE1, entity.getValue());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setKey() {
        cleanEntity();

        entity.setKey(KEY1);
        assertEquals(KEY1, String.valueOf(ReflectionTestUtils.getField(entity, "key")));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setValue() {
        cleanEntity();

        entity.setValue(VALUE1);
        assertEquals(VALUE1, String.valueOf(ReflectionTestUtils.getField(entity, "value")));
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
        entity = new ResponseData<>();
        assertEntityValidity(entity);
    }

    private void cleanEntity2() {
        entity2 = new ResponseData<>();
        assertEntityValidity(entity2);
    }

    private void assertEntityValidity(final ResponseData<?> responseData) {
        assertNull(ReflectionTestUtils.getField(responseData, KEY_TEXT));
        assertNull(ReflectionTestUtils.getField(responseData, VALUE_TEXT));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity2();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity2);
    }

    private void prepareEntityForEqualityTest(final ResponseData<?> responseData) {
        ReflectionTestUtils.setField(responseData, KEY_TEXT, KEY1);
        ReflectionTestUtils.setField(responseData, VALUE_TEXT, VALUE1);
    }

}
