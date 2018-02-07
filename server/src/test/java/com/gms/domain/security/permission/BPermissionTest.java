package com.gms.domain.security.permission;

import com.gms.Application;
import com.gms.domain.security.role.BRole;
import com.gms.util.StringUtil;
import com.gms.util.i18n.CodeI18N;
import com.gms.util.validation.PersistenceValidation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * BPermissionTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 03, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BPermissionTest {

    private final String name = "sampleN";
    private final String label = "sampleL";
    private final Set<BRole> roles = new HashSet<>();
    private final int MAX_RANGE_255 = 255;
    private BPermission entity0;
    private BPermission entity1;

    @Before
    public void setUp() {
        roles.add(new BRole("sampleL"));
    }

    //region persistence constraints validations
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = new BPermission(name, label);
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    @Test
    public void nameIsNotBlank() {
        propertyIsNot("", label, CodeI18N.FIELD_NOT_BLANK, "name property must not be blank");
    }

    @Test
    public void nameIsNotNull() {
        propertyIsNot(null, label, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    @Test
    public void labelIsNotBlank() {
        propertyIsNot(name, "", CodeI18N.FIELD_NOT_BLANK, "label property must not be blank");
    }

    @Test
    public void labelIsNotNull() {
        propertyIsNot(name, null, CodeI18N.FIELD_NOT_NULL, "label property must not be null");
    }

    @Test
    public void nameIsNotOutOfRange() {
        propertyIsNot(StringUtil.createJString(MAX_RANGE_255 + 1), label, CodeI18N.FIELD_SIZE, "name property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void labelIsNotOutOfRange() {
        propertyIsNot(name, StringUtil.createJString(MAX_RANGE_255 + 1), CodeI18N.FIELD_SIZE, "label property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    public void propertyIsNot(String name, String label, String messageTest, String assertMessage) {
        BPermission e = new BPermission(name, label);
        assertTrue(assertMessage, PersistenceValidation.objectIsInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    @Test
    public void getName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "name", name);
        assertTrue(entity0.getName().equals(name));
    }

    @Test
    public void getLabel() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "label", label);
        assertTrue(entity0.getLabel().equals(label));
    }

    @Test
    public void getRoles() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "roles", roles);
        assertTrue(entity0.getRoles().equals(roles));
    }

    @Test
    public void setRoles() {
        cleanEntity0();

        entity0.setRoles(roles);
        assertTrue(roles.equals(ReflectionTestUtils.getField(entity0, "roles")));
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity0.toString().equals(entity1.toString()));
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


    private void cleanEntity1() {
        entity1 = new BPermission();
        assertNull(ReflectionTestUtils.getField(entity1, "name"));
        assertNull(ReflectionTestUtils.getField(entity1, "label"));
        assertTrue(ReflectionTestUtils.getField(entity1, "roles").equals(new HashSet<BRole>()));
    }

    private void cleanEntity0() {
        entity0 = new BPermission();
        assertNull(ReflectionTestUtils.getField(entity0, "name"));
        assertNull(ReflectionTestUtils.getField(entity0, "label"));
        assertTrue(ReflectionTestUtils.getField(entity0, "roles").equals(new HashSet<BRole>()));
    }


    private void prepareEntitiesForEqualityTest() {
        cleanEntity0();
        cleanEntity1();

        ReflectionTestUtils.setField(entity0, "name", name);
        ReflectionTestUtils.setField(entity0, "label", label);
        ReflectionTestUtils.setField(entity0, "roles", roles);
        ReflectionTestUtils.setField(entity1, "name", name);
        ReflectionTestUtils.setField(entity1, "label", label);
        ReflectionTestUtils.setField(entity1, "roles", roles);
    }
}