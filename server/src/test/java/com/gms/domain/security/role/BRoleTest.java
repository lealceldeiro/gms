package com.gms.domain.security.role;

import com.gms.domain.security.permission.BPermission;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BRoleTest {

    /**
     * "label" field value.
     */
    private final String labelS = "sampleL";
    /**
     * "description" field value.
     */
    private final String descriptionS = "sampleD";
    /**
     * "enabled" field value.
     */
    private final Boolean enabledS = true;
    /**
     * "permissions" field value.
     */
    private final Set<BPermission> permissionsS = new HashSet<>();
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_255 = 255;
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_10485760 = 10485760;
    /**
     * Instance of {@link BRole}.
     */
    private BRole entity0;
    /**
     * Instance of {@link BRole}.
     */
    private BRole entity1;
    /**
     * Instance of {@link BPermission}.
     */
    private final BPermission sampleP = EntityUtil.getSamplePermission();

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        permissionsS.add(EntityUtil.getSamplePermission());
    }

    //region persistence constraints validations

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = EntityUtil.getSampleRole();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotBlank() {
        propertyIsNot("", null, CodeI18N.FIELD_NOT_BLANK, "label property must not be blank");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotNull() {
        propertyIsNot(null, null, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotOutOfRange() {
        propertyIsNot(
                StringUtil.createJString(MAX_RANGE_255 + 1),
                null,
                CodeI18N.FIELD_SIZE,
                "label property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsNotWithInvalidPattern() {
        String[] invalidLabels = StringUtil.INVALID_USERNAME;
        for (String label : invalidLabels) {
            propertyIsNot(label, descriptionS, CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME,
                    "label property does not fulfill the username pattern restrictions");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void labelIsValidWithValidPattern() {
        BRole r;
        String[] validLabels = StringUtil.VALID_USERNAME;
        for (String label : validLabels) {
            r = new BRole(label);
            assertTrue("Role is not valid with a valid label: " + label, PersistenceValidation.validate(r).isEmpty());
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void descriptionIsNotOutOfRange() {
        propertyIsNot(
                labelS,
                StringUtil.createJString(MAX_RANGE_10485760 + 1),
                CodeI18N.FIELD_SIZE,
                "description property must not be of size lesser than 0 and larger than " + MAX_RANGE_10485760
                        + " characters"
        );
    }

    private void propertyIsNot(final String label, final String description, final String messageTest,
                               final String assertMessage) {
        BRole e = new BRole(label);
        if (description != null) {
            e.setDescription(description);
        }
        assertTrue(assertMessage, PersistenceValidation.isObjectInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void addPermission() {
        cleanEntity0();
        entity0.addPermission(sampleP);

        final Collection<?> permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && permissions.contains(sampleP));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void removePermission() {
        Collection<BPermission> auxP = new HashSet<>();
        auxP.add(sampleP);

        cleanEntity0();
        ReflectionTestUtils.setField(entity0, "permissions", auxP);
        Collection<?> permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && permissions.contains(sampleP));

        entity0.removePermission(sampleP);
        permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && !permissions.contains(sampleP));
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

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getLabel() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "label", labelS);
        assertEquals(entity0.getLabel(), labelS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getDescription() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "description", descriptionS);
        assertEquals(entity0.getDescription(), descriptionS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEnabled() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "enabled", enabledS);
        assertEquals(entity0.getEnabled(), enabledS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPermissions() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "permissions", permissionsS);
        assertEquals(entity0.getPermissions(), permissionsS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setDescription() {
        cleanEntity0();

        entity0.setDescription(descriptionS);
        assertEquals(descriptionS, ReflectionTestUtils.getField(entity0, "description"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setEnabled() {
        cleanEntity0();

        entity0.setEnabled(enabledS);
        assertEquals(enabledS, ReflectionTestUtils.getField(entity0, "enabled"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setPermissions() {
        cleanEntity0();

        entity0.setPermissions(permissionsS);
        assertEquals(permissionsS, ReflectionTestUtils.getField(entity0, "permissions"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0.toString(), entity1.toString());
    }

    private void cleanEntity0() {
        entity0 = new BRole();
        assertEntityCleanState(entity0);
    }

    private void cleanEntity1() {
        entity1 = new BRole();
        assertEntityCleanState(entity1);
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity0();
        cleanEntity1();

        prepareEntityForEqualityTest(entity0);
        prepareEntityForEqualityTest(entity1);
    }

    private void assertEntityCleanState(final BRole entity) {
        assertNull(ReflectionTestUtils.getField(entity, "label"));
        assertNull(ReflectionTestUtils.getField(entity, "description"));
        Object enabled = ReflectionTestUtils.getField(entity, "enabled");
        assertNotNull(enabled);
        assertFalse(Boolean.parseBoolean(enabled.toString()));
        assertNull(ReflectionTestUtils.getField(entity, "permissions"));
    }

    private void prepareEntityForEqualityTest(final BRole entity) {
        ReflectionTestUtils.setField(entity, "label", labelS);
        ReflectionTestUtils.setField(entity, "description", descriptionS);
        ReflectionTestUtils.setField(entity, "enabled", enabledS);
        ReflectionTestUtils.setField(entity, "permissions", permissionsS);
    }

}
