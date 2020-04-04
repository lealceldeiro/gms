package com.gms.domain.security.user;

import com.gms.testutil.EntityUtil;
import com.gms.testutil.StringUtil;
import com.gms.testutil.validation.PersistenceValidation;
import com.gms.util.i18n.CodeI18N;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class EUserTest {

    /**
     * "username" field value.
     */
    private final String usernameS = "usernameS";
    /**
     * "email" field value.
     */
    private final String emailS = "email@sample.com";
    /**
     * "name" field value.
     */
    private final String nameS = "nameS";
    /**
     * "lastName" field value.
     */
    private final String lastNameS = "lastnameS";
    /**
     * "password" field value.
     */
    private final String passwordS = "passwordS";
    /**
     * "enabled" field value.
     */
    private final Boolean enabledS = true;
    /**
     * "emailVerified" field value.
     */
    private final Boolean emailVerifiedS = true;
    /**
     * "accountNonExpired" field value.
     */
    private final Boolean accountNonExpiredS = true;
    /**
     * "accountNonLocked" field value.
     */
    private final Boolean accountNonLockedS = true;
    /**
     * "credentialsNonExpired" field value.
     */
    private final Boolean credentialsNonExpiredS = true;
    /**
     * "authorities" field value.
     */
    private final HashSet<GrantedAuthority> authoritiesS = new HashSet<>();
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_255 = 255;
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_254 = 254;
    /**
     * Max length range for creating a string.
     */
    private static final int MAX_RANGE_10485760 = 10485760;
    /**
     * Instance of {@link EUser}.
     */
    private EUser entity0;
    /**
     * Instance of {@link EUser}.
     */
    private EUser entity1;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        authoritiesS.add(new SimpleGrantedAuthority("sampleAuth"));
    }

    //region persistence constraints validations

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkValidEntity() {
        cleanEntity0();
        entity0 = EntityUtil.getSampleUser();
        final Set<ConstraintViolation<Object>> cv = PersistenceValidation.validate(entity0);
        assertTrue(cv.isEmpty());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotBlank() {
        propertyIsNot(
                "",
                emailS,
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_BLANK,
                "username property must not be blank"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotNull() {
        propertyIsNot(
                null,
                emailS,
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_NULL,
                "username property must not be null"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotOutOfRange() {
        propertyIsNot(
                StringUtil.createJString(MAX_RANGE_255 + 1),
                emailS,
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_SIZE,
                "username property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsNotWithInvalidPattern() {
        String[] invalidUsernames = StringUtil.INVALID_USERNAME;
        for (String username : invalidUsernames) {
            propertyIsNot(username, emailS, nameS, lastNameS, passwordS, CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME,
                    "username property does not fulfill the username pattern restrictions");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void usernameIsValidWithValidPattern() {
        EUser user;
        String[] validUsernames = StringUtil.VALID_USERNAME;
        for (String username : validUsernames) {
            user = new EUser(username, emailS, nameS, lastNameS, passwordS);
            assertTrue(
                    "User is not valid with a valid label: " + username,
                    PersistenceValidation.validate(user).isEmpty()
            );
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void emailIsNotBlank() {
        propertyIsNot(
                usernameS,
                "",
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_BLANK,
                "email property must not be blank"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void emailIsNotNull() {
        propertyIsNot(
                usernameS,
                null,
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_NULL,
                "email property must not be null"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void emailIsNotOutOfRange() {
        propertyIsNot(
                nameS,
                StringUtil.createJString(MAX_RANGE_254 + 1),
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_SIZE,
                "email property must not be of size lesser than 0 and larger than " + MAX_RANGE_254 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void emailIsAValidEmail() {
        propertyIsNot(
                usernameS,
                "invalid1",
                nameS,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_WELL_FORMED,
                "email property must be a valid email address (see for more information:"
                        + "http://bit.ly/emailSpecRef and http://bit.ly/emailErrataRef"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotBlank() {
        propertyIsNot(
                usernameS,
                emailS,
                "",
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_BLANK,
                "name property must not be blank"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotNull() {
        propertyIsNot(
                usernameS,
                emailS,
                null,
                lastNameS,
                passwordS,
                CodeI18N.FIELD_NOT_NULL,
                "name property must not be null"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void nameIsNotOutOfRange() {
        propertyIsNot(
                nameS,
                emailS,
                StringUtil.createJString(MAX_RANGE_255 + 1),
                lastNameS,
                passwordS,
                CodeI18N.FIELD_SIZE,
                "name property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void lastNameIsNotBlank() {
        propertyIsNot(
                usernameS,
                emailS,
                nameS,
                "",
                passwordS,
                CodeI18N.FIELD_NOT_BLANK,
                "lastName property must not be blank"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void lastNameIsNotNull() {
        propertyIsNot(
                usernameS,
                emailS,
                nameS,
                null,
                passwordS,
                CodeI18N.FIELD_NOT_NULL,
                "lastName property must not be null"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void lastNameIsNotOutOfRange() {
        propertyIsNot(
                nameS,
                emailS,
                nameS,
                StringUtil.createJString(MAX_RANGE_255 + 1),
                passwordS, CodeI18N.FIELD_SIZE,
                "lastName property must not be of size lesser than 0 and larger than " + MAX_RANGE_255 + " characters"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void passwordIsNotBlank() {
        propertyIsNot(
                usernameS,
                emailS,
                nameS,
                lastNameS,
                "",
                CodeI18N.FIELD_NOT_BLANK,
                "password property must not be blank"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void passwordIsNotNull() {
        propertyIsNot(
                usernameS,
                emailS,
                nameS,
                lastNameS,
                null,
                CodeI18N.FIELD_NOT_NULL,
                "password property must not be null"
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void passwordIsNotOutOfRange() {
        propertyIsNot(
                nameS,
                emailS,
                nameS,
                StringUtil.createJString(MAX_RANGE_10485760 + 1),
                passwordS,
                CodeI18N.FIELD_SIZE,
                "password property must not be of size lesser than 0 and larger than " + MAX_RANGE_10485760
                        + " characters"
        );
    }

    private void propertyIsNot(
            final String username,
            final String email,
            final String name,
            final String lastName,
            final String password,
            final String messageTest,
            final String assertMessage
    ) {
        EUser e = new EUser(username, email, name, lastName, password);
        assertTrue(assertMessage, PersistenceValidation.isObjectInvalidWithErrorMessage(e, messageTest));
    }
    //endregion

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getAuthorities() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "authorities", authoritiesS);
        assertArrayEquals(entity0.getAuthorities().toArray(), authoritiesS.toArray());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isAccountNonExpired() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "accountNonExpired", accountNonExpiredS);
        assertEquals(entity0.isAccountNonExpired(), accountNonExpiredS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isAccountNonLocked() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "accountNonLocked", accountNonLockedS);
        assertEquals(entity0.isAccountNonLocked(), accountNonLockedS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isCredentialsNonExpired() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "credentialsNonExpired", credentialsNonExpiredS);
        assertEquals(entity0.isCredentialsNonExpired(), credentialsNonExpiredS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isEnabled() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "enabled", enabledS);
        assertEquals(entity0.isEnabled(), enabledS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isEmailVerified() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "emailVerified", emailVerifiedS);
        assertEquals(entity0.isEmailVerified(), emailVerifiedS);
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
    public void getUsername() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "username", usernameS);
        assertEquals(entity0.getUsername(), usernameS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEmail() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "email", emailS);
        assertEquals(entity0.getEmail(), emailS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "name", nameS);
        assertEquals(entity0.getName(), nameS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getLastName() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "lastName", lastNameS);
        assertEquals(entity0.getLastName(), lastNameS);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPassword() {
        cleanEntity0();

        ReflectionTestUtils.setField(entity0, "password", passwordS);
        assertEquals(entity0.getPassword(), passwordS);
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
    public void setEmailVerified() {
        cleanEntity0();

        entity0.setEmailVerified(emailVerifiedS);
        assertEquals(emailVerifiedS, ReflectionTestUtils.getField(entity0, "emailVerified"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setAccountNonExpired() {
        cleanEntity0();

        entity0.setAccountNonExpired(accountNonExpiredS);
        assertEquals(accountNonExpiredS, ReflectionTestUtils.getField(entity0, "accountNonExpired"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setAccountNonLocked() {
        cleanEntity0();

        entity0.setAccountNonLocked(accountNonLockedS);
        assertEquals(accountNonLockedS, ReflectionTestUtils.getField(entity0, "accountNonLocked"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setCredentialsNonExpired() {
        cleanEntity0();

        entity0.setCredentialsNonExpired(credentialsNonExpiredS);
        assertEquals(credentialsNonExpiredS, ReflectionTestUtils.getField(entity0, "credentialsNonExpired"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setAuthorities() {
        cleanEntity0();

        entity0.setAuthorities(authoritiesS);
        assertEquals(authoritiesS, ReflectionTestUtils.getField(entity0, "authorities"));
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

    private void assertEntityCleanState(final EUser entity) {
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

    private void assertBooleanFalseIfNotNull(final Object field) {
        if (field != null) {
            assertFalse(Boolean.parseBoolean(field.toString()));
        }
    }

    private void prepareEntityForEqualityTest(final EUser entity) {
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
