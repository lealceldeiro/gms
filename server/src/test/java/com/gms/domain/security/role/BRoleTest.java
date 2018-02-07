package com.gms.domain.security.role;

import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.util.StringUtil;
import com.gms.util.i18n.CodeI18N;
import com.gms.util.validation.PersistenceValidation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * BRoleTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 07, 2018
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
    private BPermission sampleP = new BPermission("samplePN", "samplePL");

    @Before
    public void setUp() {
        permissionsS.add(new BPermission("sampleN", "sampleL"));
    }


    //region persistence constraints validations
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
        propertyIsNot(StringUtil.createJString(MAX_RANGE_255 + 1), null, CodeI18N.FIELD_SIZE, "label property must not be of size lesser than 0 and larger than 255 characters");
    }

    @Test
    public void descriptionIsNotOutOfRange() {
        propertyIsNot(labelS, StringUtil.createJString(MAX_RANGE_10485760 + 1), CodeI18N.FIELD_SIZE, "description property must not be of size lesser than 0 and larger than 10485760 characters");
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
        assertTrue(permissions.contains(sampleP));
    }

    @Test
    public void removePermission() {
        Set<BPermission> auxP = new HashSet<>();
        auxP.add(sampleP);

        cleanEntity0();
        ReflectionTestUtils.setField(entity0, "permissions", auxP);
        HashSet<?> permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(permissions.contains(sampleP));

        entity0.removePermission(sampleP);
        permissions = (HashSet<?>) ReflectionTestUtils.getField(entity0, "permissions");
        assertTrue(!permissions.contains(sampleP));
    }

    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity0.equals(entity1));
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity0.hashCode() == entity1.hashCode());
    }

    @Test
    public void getLabel() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "label", labelS);
        assertTrue(entity0.getLabel().equals(labelS));
    }

    @Test
    public void getDescription() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "description", descriptionS);
        assertTrue(entity0.getDescription().equals(descriptionS));
    }

    @Test
    public void getEnabled() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "enabled", enabledS);
        assertTrue(entity0.getEnabled().equals(enabledS));
    }

    @Test
    public void getPermissions() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "permissions", permissionsS);
        assertTrue(entity0.getPermissions().equals(permissionsS));
    }

    @Test
    public void setDescription() {
        cleanEntity0();

        entity0.setPermissions(permissionsS);
        assertTrue(permissionsS.equals(ReflectionTestUtils.getField(entity0, "permissions")));
    }

    @Test
    public void setEnabled() {
        cleanEntity0();

        entity0.setEnabled(enabledS);
        assertTrue(enabledS.equals(ReflectionTestUtils.getField(entity0, "enabled")));
    }

    @Test
    public void setPermissions() {
        cleanEntity0();

        entity0.setPermissions(permissionsS);
        assertTrue(permissionsS.equals(ReflectionTestUtils.getField(entity0, "permissions")));
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity0.toString().equals(entity1.toString()));
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
        assertFalse(Boolean.parseBoolean(ReflectionTestUtils.getField(entity, "enabled").toString()));
        assertTrue(ReflectionTestUtils.getField(entity, "permissions").equals(new HashSet<BPermission>()));
    }

    private void prepareEntityForEqualityTest(BRole entity) {
        ReflectionTestUtils.setField(entity, "label", labelS);
        ReflectionTestUtils.setField(entity, "description", descriptionS);
        ReflectionTestUtils.setField(entity, "enabled", enabledS);
        ReflectionTestUtils.setField(entity, "permissions", permissionsS);
    }
}