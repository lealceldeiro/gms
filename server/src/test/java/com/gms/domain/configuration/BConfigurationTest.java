package com.gms.domain.configuration;

import com.gms.Application;
import com.gms.util.StringUtil;
import com.gms.util.i18n.CodeI18N;
import com.gms.util.validation.PersistenceValidation;
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
    private final int MAX_RANGE_255 = 255;

    //region persistence constraints validations
    @Test
    public void keyIsNotBlank() {
        propertyIsNot("", v, CodeI18N.FIELD_NOT_BLANK, "key property must not be blank");
    }

    @Test
    public void keyIsNotNull() {
        propertyIsNot(null, v, CodeI18N.FIELD_NOT_NULL, "key property must not be null");
    }

    @Test
    public void keyIsNotOutOfRange() {
        propertyIsNot(StringUtil.createJString(MAX_RANGE_255 + 1), v, CodeI18N.FIELD_SIZE, "key property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void valueIsNotBlank() {
        propertyIsNot(k, "", CodeI18N.FIELD_NOT_BLANK, "value property must not be blank");
    }

    @Test
    public void valueIsNotNull() {
        propertyIsNot(k, null, CodeI18N.FIELD_NOT_NULL, "value property must not be null");
    }

    @Test
    public void valueIsNotOutOfRange() {
        propertyIsNot(k, StringUtil.createJString(MAX_RANGE_255 + 1), CodeI18N.FIELD_SIZE, "value property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    public void propertyIsNot(String keyVal, String valueVal, String messageTest, String assertMessage) {
        BConfiguration c = new BConfiguration(keyVal, valueVal);
        assertTrue(assertMessage, PersistenceValidation.objectIsInvalidWithErrorMessage(c, messageTest));
    }
    //endregion

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
        assertEntityValidity(entity);
    }

    private void cleanEntity1() {
        entity1 = new BConfiguration();
        assertEntityValidity(entity1);
    }

    private void assertEntityValidity(BConfiguration entity) {
        assertNull(ReflectionTestUtils.getField(entity, "key"));
        assertNull(ReflectionTestUtils.getField(entity, "value"));
        assertNull(ReflectionTestUtils.getField(entity, "userId"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity1();
        setEntityFields(entity);
        setEntityFields(entity1);
    }
    private void setEntityFields(BConfiguration entity) {
        ReflectionTestUtils.setField(entity, "key", k);
        ReflectionTestUtils.setField(entity, "value", v);
        ReflectionTestUtils.setField(entity, "userId", id);
    }
}