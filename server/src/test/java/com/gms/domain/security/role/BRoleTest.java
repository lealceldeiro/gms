package com.gms.domain.security.role;

import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BRoleTest {

    private final String labelS = "sampleL";
    private final String descriptionS = "sampleD";
    private final Boolean enabledS = true;
    private final Set<BPermission> permissionsS = new HashSet<>();
    private final int MAX_RANGE_255 = 255;
    private final int MAX_RANGE_10485760 = 10485760;
    private BRole entity0;
    private BRole entity1;
    private BPermission sampleP = EntityUtil.getSamplePermission();

    @Before
    public void setUp() {
        permissionsS.add(EntityUtil.getSamplePermission());
    }

    //region persistence constraints validations
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = EntityUtil.getSampleRole();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    @Test
    public void labelIsNotBlank() {
        propertyIsNot("", null, CodeI18N.FIELD_NOT_BLANK, "label property must not be blank");
    }

    @Test
    public void labelIsNotNull() {
        propertyIsNot(null, null, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    @Test
    public void labelIsNotOutOfRange() {
        propertyIsNot(StringUtil.createJString(MAX_RANGE_255 + 1), null, CodeI18N.FIELD_SIZE, "label property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void labelIsNotWithInvalidPattern() {
        String[] invalidLabels = StringUtil.INVALID_USERNAME;
        for (String label: invalidLabels) {
            propertyIsNot(label, descriptionS, CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME,
                    "label property does not fulfill the username pattern restrictions");
        }
    }

    @Test
    public void labelIsValidWithValidPattern() {
        BRole r;
        String[] validLabels = StringUtil.VALID_USERNAME;
        for (String label: validLabels) {
            r = new BRole(label);
            assertTrue("Role is not valid with a valid label: " + label, PersistenceValidation.validate(r).isEmpty());
        }
    }

    @Test
    public void descriptionIsNotOutOfRange() {
        propertyIsNot(labelS, StringUtil.createJString(MAX_RANGE_10485760 + 1), CodeI18N.FIELD_SIZE, "description property must not be of size lesser than 0 and larger than " + MAX_RANGE_10485760 + " characters");
    }

    public void propertyIsNot(String label, String description, String messageTest, String assertMessage) {
        BRole e = new BRole(label);
        if (description != null) {
            e.setDescription(description);
        }
        assertTrue(assertMessage, PersistenceValidation.objectIsInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    @Test
    public void addPermission() {
        cleanEntity0();
        entity0.addPermission(sampleP);

        final HashSet<?> permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && permissions.contains(sampleP));
    }

    @Test
    public void removePermission() {
        Set<BPermission> auxP = new HashSet<>();
        auxP.add(sampleP);

        cleanEntity0();
        ReflectionTestUtils.setField(entity0, "permissions", auxP);
        HashSet<?> permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && permissions.contains(sampleP));

        entity0.removePermission(sampleP);
        permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions != null && !permissions.contains(sampleP));
    }

    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0, entity1);
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity0.hashCode(), entity1.hashCode());
    }

    @Test
    public void getLabel() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "label", labelS);
        assertEquals(entity0.getLabel(), labelS);
    }

    @Test
    public void getDescription() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "description", descriptionS);
        assertEquals(entity0.getDescription(), descriptionS);
    }

    @Test
    public void getEnabled() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "enabled", enabledS);
        assertEquals(entity0.getEnabled(), enabledS);
    }

    @Test
    public void getPermissions() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "permissions", permissionsS);
        assertEquals(entity0.getPermissions(), permissionsS);
    }

    @Test
    public void setDescription() {
        cleanEntity0();

        entity0.setDescription(descriptionS);
        assertEquals(descriptionS, ReflectionTestUtils.getField(entity0, "description"));
    }

    @Test
    public void setEnabled() {
        cleanEntity0();

        entity0.setEnabled(enabledS);
        assertEquals(enabledS, ReflectionTestUtils.getField(entity0, "enabled"));
    }

    @Test
    public void setPermissions() {
        cleanEntity0();

        entity0.setPermissions(permissionsS);
        assertEquals(permissionsS, ReflectionTestUtils.getField(entity0, "permissions"));
    }

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

    private void assertEntityCleanState(BRole entity) {
        assertNull(ReflectionTestUtils.getField(entity, "label"));
        assertNull(ReflectionTestUtils.getField(entity, "description"));
        Object enabled = ReflectionTestUtils.getField(entity, "enabled");
        assertNotNull(enabled);
        assertFalse(Boolean.parseBoolean(enabled.toString()));
        assertNull(ReflectionTestUtils.getField(entity, "permissions"));
    }

    private void prepareEntityForEqualityTest(BRole entity) {
        ReflectionTestUtils.setField(entity, "label", labelS);
        ReflectionTestUtils.setField(entity, "description", descriptionS);
        ReflectionTestUtils.setField(entity, "enabled", enabledS);
        ReflectionTestUtils.setField(entity, "permissions", permissionsS);
    }
}