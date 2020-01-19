package com.gms.domain.security.ownedentity;

import com.gms.Application;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EOwnedEntityTest {

    /**
     * A sample "name" value.
     */
    private final String name = "sampleN";
    /**
     * A sample "username" value.
     */
    private final String username = "sampleU";
    /**
     * A sample "description" value.
     */
    private final String description = "sampleD";
    /**
     * A range number for generating strings with lengths in-between.
     */
    private static final int MAX_RANGE_255 = 255;
    /**
     * A range number for generating strings with lengths in-between.
     */
    private static final int MAX_RANGE_10485760 = 10485760;
    /**
     * Instance for testing purposes of {@link EOwnedEntity}.
     */
    private EOwnedEntity entity;
    /**
     * Instance for testing purposes of {@link EOwnedEntity}.
     */
    private EOwnedEntity entity3;

    //region persistence constraints validations

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkValidEntity() {
        cleanEntity();
        entity = EntityUtil.getSampleEntity();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity);
        assertTrue(cv.isEmpty());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotBlank() {
        propertyIsNot("", username, description, CodeI18N.FIELD_NOT_BLANK, "name property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotNull() {
        propertyIsNot(null, username, description, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotOutOfRange() {
        propertyIsNot(
                StringUtil.createJString(MAX_RANGE_255 + 1),
                username,
                description,
                CodeI18N.FIELD_SIZE,
                "name property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotBlank() {
        propertyIsNot(name, "", description, CodeI18N.FIELD_NOT_BLANK, "username property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotNull() {
        propertyIsNot(name, null, description, CodeI18N.FIELD_NOT_NULL, "username property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotOutOfRange() {
        propertyIsNot(
                name,
                StringUtil.createJString(MAX_RANGE_255 + 1),
                description,
                CodeI18N.FIELD_SIZE,
                "username property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotWithInvalidPattern() {
        String[] invalidUsername = StringUtil.INVALID_USERNAME;
        for (String iUsername : invalidUsername) {
            propertyIsNot(name, iUsername, description, CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME,
                    "username property does not fulfill the username pattern restrictions");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsValidWithValidPattern() {
        EOwnedEntity iEntity;
        String[] validUsername = StringUtil.VALID_USERNAME;
        for (String iUsername : validUsername) {
            iEntity = new EOwnedEntity(name, iUsername, description);
            assertTrue(
                    "Owned entity is not valid with a valid username: " + iUsername,
                    PersistenceValidation.validate(iEntity).isEmpty()
            );
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void descriptionIsNotBlank() {
        propertyIsNot(name, username, "", CodeI18N.FIELD_NOT_BLANK, "description property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void descriptionIsNotNull() {
        propertyIsNot(name, username, null, CodeI18N.FIELD_NOT_NULL, "description property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void descriptionIsNotOutOfRange() {
        propertyIsNot(
                name,
                username,
                StringUtil.createJString(MAX_RANGE_10485760 + 1),
                CodeI18N.FIELD_SIZE,
                "description property must not be of size lesser than 0 and larger than " + MAX_RANGE_10485760
                        + " characters"
        );
    }

    private void propertyIsNot(final String nameArg, final String usernameArg, final String descriptionArg,
                               final String messageTest, final String assertMessage) {
        EOwnedEntity e = new EOwnedEntity(nameArg, usernameArg, descriptionArg);
        assertTrue(assertMessage, PersistenceValidation.isObjectInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getName() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "name", name);
        assertEquals(entity.getName(), name);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUsername() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "username", username);
        assertEquals(entity.getUsername(), username);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getDescription() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "description", description);
        assertEquals(entity.getDescription(), description);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.hashCode(), entity3.hashCode());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity, entity3);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.toString(), entity3.toString());
    }

    private void cleanEntity() {
        entity = new EOwnedEntity();
        assertEntityValidity(entity);
    }

    private void cleanEntity3() {
        entity3 = new EOwnedEntity();
        assertEntityValidity(entity3);
    }

    private void assertEntityValidity(final EOwnedEntity entityArg) {
        assertNull(ReflectionTestUtils.getField(entityArg, "name"));
        assertNull(ReflectionTestUtils.getField(entityArg, "username"));
        assertNull(ReflectionTestUtils.getField(entityArg, "description"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity3();
        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity3);
    }

    private void prepareEntityForEqualityTest(final EOwnedEntity entityArg) {
        ReflectionTestUtils.setField(entityArg, "name", name);
        ReflectionTestUtils.setField(entityArg, "username", username);
        ReflectionTestUtils.setField(entityArg, "description", description);
    }

}
