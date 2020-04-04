package com.gms.domain.security.permission;

import com.gms.domain.security.role.BRole;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BPermissionTest {

    /**
     * "name" field value.
     */
    private final String name = "sampleN";
    /**
     * "label" field value.
     */
    private final String label = "sampleL";
    /**
     * "roles" field value.
     */
    private final Set<BRole> roles = new HashSet<>();
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_255 = 255;
    /**
     * Instance of {@link BPermission}.
     */
    private BPermission entity0;
    /**
     * Instance of {@link BPermission}.
     */
    private BPermission entity1;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        roles.add(EntityUtil.getSampleRole());
    }

    //region persistence constraints validations

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = EntityUtil.getSamplePermission();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotBlank() {
        propertyIsNot("", label, CodeI18N.FIELD_NOT_BLANK, "name property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotNull() {
        propertyIsNot(null, label, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotWithInvalidPattern() {
        String[] invalidNames = StringUtil.INVALID_USERNAME;
        for (String iName : invalidNames) {
            propertyIsNot(iName, label, CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME,
                    "name property does not fulfill the username pattern restrictions");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsValidWithValidPattern() {
        BPermission p;
        String[] validNames = StringUtil.VALID_USERNAME;
        for (String iName : validNames) {
            p = new BPermission(iName, label);
            assertTrue(
                    "Permission is not valid with a valid name: " + iName,
                    PersistenceValidation.validate(p).isEmpty()
            );
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotBlank() {
        propertyIsNot(name, "", CodeI18N.FIELD_NOT_BLANK, "label property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotNull() {
        propertyIsNot(name, null, CodeI18N.FIELD_NOT_NULL, "label property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotOutOfRange() {
        propertyIsNot(
                StringUtil.createJString(MAX_RANGE_255 + 1),
                label,
                CodeI18N.FIELD_SIZE,
                "name property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotOutOfRange() {
        propertyIsNot(
                name,
                StringUtil.createJString(MAX_RANGE_255 + 1),
                CodeI18N.FIELD_SIZE,
                "label property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    private void propertyIsNot(final String nameArg, final String labelArg, final String messageTest,
                               final String assertMessage) {
        BPermission e = new BPermission(nameArg, labelArg);
        assertTrue(assertMessage, PersistenceValidation.isObjectInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "name", name);
        assertEquals(entity0.getName(), name);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getLabel() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "label", label);
        assertEquals(entity0.getLabel(), label);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRoles() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "roles", roles);
        assertEquals(entity0.getRoles(), roles);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setRoles() {
        cleanEntity0();

        entity0.setRoles(roles);
        assertEquals(roles, ReflectionTestUtils.getField(entity0, "roles"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0.toString(), entity1.toString());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0, entity1);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0.hashCode(), entity1.hashCode());
    }


    private void cleanEntity1() {
        entity1 = new BPermission();
        assertEntityValidity(entity1);
    }

    private void cleanEntity0() {
        entity0 = new BPermission();
        assertEntityValidity(entity0);
    }

    private void assertEntityValidity(final BPermission entity) {
        assertNull(ReflectionTestUtils.getField(entity, "name"));
        assertNull(ReflectionTestUtils.getField(entity, "label"));
        assertNull(ReflectionTestUtils.getField(entity, "roles"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity0();
        cleanEntity1();
        prepareEntityForEqualityTest(entity0);
        prepareEntityForEqualityTest(entity1);
    }

    private void prepareEntityForEqualityTest(final BPermission e) {
        ReflectionTestUtils.setField(e, "name", name);
        ReflectionTestUtils.setField(e, "label", label);
        ReflectionTestUtils.setField(e, "roles", roles);
    }

}
