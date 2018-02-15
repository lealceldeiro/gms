package com.gms.domain.security.user;

import com.gms.Application;
import com.gms.util.EntityUtil;
import com.gms.util.StringUtil;
import com.gms.util.i18n.CodeI18N;
import com.gms.util.validation.PersistenceValidation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * EUserTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 07, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EUserTest {

    private final String usernameS = "usernameS";
    private final String emailS = "email@sample.com";
    private final String nameS = "nameS";
    private final String lastNameS = "lastnameS";
    private final String passwordS = "passwordS";
    private final Boolean enabledS = true;
    private final Boolean emailVerifiedS = true;
    private final Boolean accountNonExpiredS = true;
    private final Boolean accountNonLockedS = true;
    private final Boolean credentialsNonExpiredS = true;
    private final HashSet<GrantedAuthority> authoritiesS = new HashSet<>();
    private final int MAX_RANGE_255 = 255;
    private final int MAX_RANGE_254 = 254;
    private final int MAX_RANGE_10485760 = 10485760;
    private EUser entity0;
    private EUser entity1;

    @Before
    public void setUp() {
        authoritiesS.add(new SimpleGrantedAuthority("sampleAuth"));
    }

    //region persistence constraints validations
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = EntityUtil.getSampleUser();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    @Test
    public void usernameIsNotBlank() {
        propertyIsNot("", emailS, nameS, lastNameS, passwordS, CodeI18N.FIELD_NOT_BLANK, "username property must not be blank");
    }

    @Test
    public void usernameIsNotNull() {
        propertyIsNot(null, emailS, nameS, lastNameS, passwordS, CodeI18N.FIELD_NOT_NULL, "username property must not be null");
    }

    @Test
    public void usernameIsNotOutOfRange() {
        propertyIsNot(StringUtil.createJString(MAX_RANGE_255 + 1), emailS, nameS, lastNameS, passwordS, CodeI18N.FIELD_SIZE, "username property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void emailIsNotBlank() {
        propertyIsNot(usernameS, "", nameS, lastNameS, passwordS, CodeI18N.FIELD_NOT_BLANK, "email property must not be blank");
    }

    @Test
    public void emailIsNotNull() {
        propertyIsNot(usernameS, null, nameS, lastNameS, passwordS, CodeI18N.FIELD_NOT_NULL, "email property must not be null");
    }

    @Test
    public void emailIsNotOutOfRange() {
        propertyIsNot(nameS, StringUtil.createJString(MAX_RANGE_254 + 1), nameS, lastNameS, passwordS, CodeI18N.FIELD_SIZE, "email property must not be of size lesser than 0 and larger than " + MAX_RANGE_254 + " characters");
    }

    @Test
    public void emailIsAValidEmail() {
        propertyIsNot(usernameS, "invalid1", nameS, lastNameS, passwordS, CodeI18N.FIELD_NOT_WELL_FORMED, "email property must be a valid email address (see for more information: http://docs.jboss.org/ejb3/app-server/HibernateAnnotations/api/org/hibernate/validator/Email.html and http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690)");
    }

    @Test
    public void nameIsNotBlank() {
        propertyIsNot(usernameS, emailS, "", lastNameS, passwordS, CodeI18N.FIELD_NOT_BLANK, "name property must not be blank");
    }

    @Test
    public void nameIsNotNull() {
        propertyIsNot(usernameS, emailS, null, lastNameS, passwordS, CodeI18N.FIELD_NOT_NULL, "name property must not be null");
    }

    @Test
    public void nameIsNotOutOfRange() {
        propertyIsNot(nameS, emailS, StringUtil.createJString(MAX_RANGE_255 + 1), lastNameS, passwordS, CodeI18N.FIELD_SIZE, "name property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void lastNameIsNotBlank() {
        propertyIsNot(usernameS, emailS, nameS, "", passwordS, CodeI18N.FIELD_NOT_BLANK, "lastName property must not be blank");
    }

    @Test
    public void lastNameIsNotNull() {
        propertyIsNot(usernameS, emailS, nameS, null, passwordS, CodeI18N.FIELD_NOT_NULL, "lastName property must not be null");
    }

    @Test
    public void lastNameIsNotOutOfRange() {
        propertyIsNot(nameS, emailS, nameS, StringUtil.createJString(MAX_RANGE_255 + 1), passwordS, CodeI18N.FIELD_SIZE, "lastName property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters");
    }

    @Test
    public void passwordIsNotBlank() {
        propertyIsNot(usernameS, emailS, nameS, lastNameS, "", CodeI18N.FIELD_NOT_BLANK, "password property must not be blank");
    }

    @Test
    public void passwordIsNotNull() {
        propertyIsNot(usernameS, emailS, nameS, lastNameS, null, CodeI18N.FIELD_NOT_NULL, "password property must not be null");
    }

    @Test
    public void passwordIsNotOutOfRange() {
        propertyIsNot(nameS, emailS, nameS, StringUtil.createJString(MAX_RANGE_10485760 + 1), passwordS, CodeI18N.FIELD_SIZE, "password property must not be of size lesser than 0 and larger than " + MAX_RANGE_10485760 + " characters");
    }

    public void propertyIsNot(String username, String email, String name, String lastName, String password, String messageTest, String assertMessage) {
        EUser e = new EUser(username, email, name, lastName, password);
        assertTrue(assertMessage, PersistenceValidation.objectIsInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    @Test
    public void getAuthorities() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "authorities", authoritiesS);
        assertTrue(entity0.getAuthorities().equals(authoritiesS));
    }

    @Test
    public void isAccountNonExpired() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "accountNonExpired", accountNonExpiredS);
        assertTrue(entity0.isAccountNonExpired() == accountNonExpiredS);
    }

    @Test
    public void isAccountNonLocked() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "accountNonLocked", accountNonLockedS);
        assertTrue(entity0.isAccountNonLocked() == accountNonLockedS);
    }

    @Test
    public void isCredentialsNonExpired() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "credentialsNonExpired", credentialsNonExpiredS);
        assertTrue(entity0.isCredentialsNonExpired() == credentialsNonExpiredS);
    }

    @Test
    public void isEnabled() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "enabled", enabledS);
        assertTrue(entity0.isEnabled() == enabledS);
    }

    @Test
    public void isEmailVerified() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "emailVerified", emailVerifiedS);
        assertTrue(entity0.isEmailVerified() == emailVerifiedS);
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
    public void getUsername() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "username", usernameS);
        assertTrue(entity0.getUsername().equals(usernameS));
    }

    @Test
    public void getEmail() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "email", emailS);
        assertTrue(entity0.getEmail().equals(emailS));
    }

    @Test
    public void getName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "name", nameS);
        assertTrue(entity0.getName().equals(nameS));
    }

    @Test
    public void getLastName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "lastName", lastNameS);
        assertTrue(entity0.getLastName().equals(lastNameS));
    }

    @Test
    public void getPassword() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "password", passwordS);
        assertTrue(entity0.getPassword().equals(passwordS));
    }

    @Test
    public void setEnabled() {
        cleanEntity0();

        entity0.setEnabled(enabledS);
        assertTrue(enabledS.equals(ReflectionTestUtils.getField(entity0, "enabled")));
    }

    @Test
    public void setEmailVerified() {
        cleanEntity0();

        entity0.setEmailVerified(emailVerifiedS);
        assertTrue(emailVerifiedS.equals(ReflectionTestUtils.getField(entity0, "emailVerified")));
    }

    @Test
    public void setAccountNonExpired() {
        cleanEntity0();

        entity0.setAccountNonExpired(accountNonExpiredS);
        assertTrue(accountNonExpiredS.equals(ReflectionTestUtils.getField(entity0, "accountNonExpired")));
    }

    @Test
    public void setAccountNonLocked() {
        cleanEntity0();

        entity0.setAccountNonLocked(accountNonLockedS);
        assertTrue(accountNonLockedS.equals(ReflectionTestUtils.getField(entity0, "accountNonLocked")));
    }

    @Test
    public void setCredentialsNonExpired() {
        cleanEntity0();

        entity0.setCredentialsNonExpired(credentialsNonExpiredS);
        assertTrue(credentialsNonExpiredS.equals(ReflectionTestUtils.getField(entity0, "credentialsNonExpired")));
    }

    @Test
    public void setAuthorities() {
        cleanEntity0();

        entity0.setAuthorities(authoritiesS);
        assertTrue(authoritiesS.equals(ReflectionTestUtils.getField(entity0, "authorities")));
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity0.toString().equals(entity1.toString()));
    }

    private void cleanEntity0() {
        entity0 = new EUser();
        assertEntityCleanState(entity0);
    }

    private void cleanEntity1() {
        entity1 = new EUser();
        assertEntityCleanState(entity1);
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity0();
        cleanEntity1();

        prepareEntityForEqualityTest(entity0);
        prepareEntityForEqualityTest(entity1);
    }

    private void assertEntityCleanState(EUser entity) {
        assertNull(ReflectionTestUtils.getField(entity, "username"));
        assertNull(ReflectionTestUtils.getField(entity, "email"));
        assertNull(ReflectionTestUtils.getField(entity, "name"));
        assertNull(ReflectionTestUtils.getField(entity, "lastName"));
        assertNull(ReflectionTestUtils.getField(entity, "password"));
        assertBooleanFalseIfNotNull(ReflectionTestUtils.getField(entity, "enabled"));
        assertBooleanFalseIfNotNull(ReflectionTestUtils.getField(entity, "emailVerified"));
        assertBooleanFalseIfNotNull(ReflectionTestUtils.getField(entity, "accountNonExpired"));
        assertBooleanFalseIfNotNull(ReflectionTestUtils.getField(entity, "accountNonLocked"));
        assertBooleanFalseIfNotNull(ReflectionTestUtils.getField(entity, "credentialsNonExpired"));

        assertNull(ReflectionTestUtils.getField(entity, "authorities"));
    }

    private void assertBooleanFalseIfNotNull(Object field) {
        if (field != null) {
            assertFalse(Boolean.valueOf(field.toString()));
        }
    }

    private void prepareEntityForEqualityTest(EUser entity) {
        ReflectionTestUtils.setField(entity, "username", usernameS);
        ReflectionTestUtils.setField(entity, "email", emailS);
        ReflectionTestUtils.setField(entity, "name", nameS);
        ReflectionTestUtils.setField(entity, "lastName", lastNameS);
        ReflectionTestUtils.setField(entity, "password", passwordS);
        ReflectionTestUtils.setField(entity, "enabled", enabledS);
        ReflectionTestUtils.setField(entity, "emailVerified", emailVerifiedS);
        ReflectionTestUtils.setField(entity, "accountNonExpired", accountNonExpiredS);
        ReflectionTestUtils.setField(entity, "accountNonLocked", accountNonLockedS);
        ReflectionTestUtils.setField(entity, "credentialsNonExpired", credentialsNonExpiredS);
        ReflectionTestUtils.setField(entity, "authorities", authoritiesS);
    }
}