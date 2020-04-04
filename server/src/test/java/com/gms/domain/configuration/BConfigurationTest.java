package com.gms.domain.configuration;

import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BConfigurationTest {

    /**
     * Key field value.
     */
    private final String key = "sampleK";
    /**
     * Value field value.
     */
    private final String value = "sampleV";
    /**
     * "id" value.
     */
    private final Long id = 1L;
    /**
     * Instance of {@link BConfiguration}.
     */
    private BConfiguration entity;
    /**
     * Instance of {@link BConfiguration}.
     */
    private BConfiguration entity1;
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_255 = 255;

    //region persistence constraints validations

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void keyIsNotBlank() {
        propertyIsNot("", value, CodeI18N.FIELD_NOT_BLANK, "key property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void keyIsNotNull() {
        propertyIsNot(null, value, CodeI18N.FIELD_NOT_NULL, "key property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void keyIsNotOutOfRange() {
        propertyIsNot(
                StringUtil.createJString(MAX_RANGE_255 + 1),
                value,
                CodeI18N.FIELD_SIZE,
                "key property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void valueIsNotBlank() {
        propertyIsNot(key, "", CodeI18N.FIELD_NOT_BLANK, "value property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void valueIsNotNull() {
        propertyIsNot(key, null, CodeI18N.FIELD_NOT_NULL, "value property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void valueIsNotOutOfRange() {
        propertyIsNot(
                key,
                StringUtil.createJString(MAX_RANGE_255 + 1),
                CodeI18N.FIELD_SIZE,
                "value property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    private void propertyIsNot(final String keyVal, final String valueVal, final String messageTest,
                               final String assertMessage) {
        BConfiguration c = new BConfiguration(keyVal, valueVal);
        assertTrue(assertMessage, PersistenceValidation.isObjectInvalidWithErrorMessage(c, messageTest));
    }
    //endregion

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setKey() {
        cleanEntity();

        entity.setKey(key);
        assertEquals(key, String.valueOf(ReflectionTestUtils.getField(entity, "key")));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setValue() {
        cleanEntity();

        entity.setValue(value);
        assertEquals(value, String.valueOf(ReflectionTestUtils.getField(entity, "value")));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setUserId() {
        cleanEntity();

        entity.setUserId(id);
        assertEquals(id, Long.valueOf(String.valueOf(ReflectionTestUtils.getField(entity, "userId"))));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getKey() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "key", key);
        assertEquals(entity.getKey(), key);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getValue() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "value", value);
        assertEquals(entity.getValue(), value);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserId() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "userId", id);
        assertEquals(entity.getUserId(), id);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.hashCode(), entity1.hashCode());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity, entity1);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.toString(), entity1.toString());
    }

    private void cleanEntity() {
        entity = new BConfiguration();
        assertEntityValidity(entity);
    }

    private void cleanEntity1() {
        entity1 = new BConfiguration();
        assertEntityValidity(entity1);
    }

    private void assertEntityValidity(final BConfiguration entityArg) {
        assertNull(ReflectionTestUtils.getField(entityArg, "key"));
        assertNull(ReflectionTestUtils.getField(entityArg, "value"));
        assertNull(ReflectionTestUtils.getField(entityArg, "userId"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity1();
        setEntityFields(entity);
        setEntityFields(entity1);
    }

    private void setEntityFields(final BConfiguration entityArg) {
        ReflectionTestUtils.setField(entityArg, "key", key);
        ReflectionTestUtils.setField(entityArg, "value", value);
        ReflectionTestUtils.setField(entityArg, "userId", id);
    }

}
