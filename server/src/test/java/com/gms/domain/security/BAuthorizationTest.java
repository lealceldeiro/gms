package com.gms.domain.security;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.testutil.EntityUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BAuthorizationTest {

    /**
     * "userId" field value.
     */
    private static final Long USER_ID = 999999999L;
    /**
     * "entityId" field value.
     */
    private static final Long ENTITY_ID = 9999999999L;
    /**
     * "roleId" field value.
     */
    private static final Long ROLE_ID = 99999999999L;
    /**
     * Instance of {@link EUser}.
     */
    private final EUser user = EntityUtil.getSampleUser();
    /**
     * Instance of {@link EOwnedEntity}.
     */
    private final EOwnedEntity oEntity = EntityUtil.getSampleEntity();
    /**
     * Instance of {@link BRole}.
     */
    private final BRole role = EntityUtil.getSampleRole();
    /**
     * Instance of {@link BAuthorization.BAuthorizationPk}.
     */
    private final BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();

    /**
     * Instance of {@link BAuthorization}.
     */
    private BAuthorization entity;
    /**
     * Instance of {@link BAuthorization}.
     */
    private BAuthorization entity1;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        pk.setUserId(USER_ID);
        pk.setEntityId(ENTITY_ID);
        pk.setRoleId(ROLE_ID);

        user.setId(USER_ID);
        oEntity.setId(ENTITY_ID);
        role.setId(ROLE_ID);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getBAuthorizationPk() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "bAuthorizationPk", pk);
        assertEquals(entity.getBAuthorizationPk(), pk);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUser() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "user", user);
        assertEquals(entity.getUser(), user);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEntity() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "entity", oEntity);
        assertEquals(entity.getEntity(), oEntity);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRole() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "role", role);
        assertEquals(entity.getRole(), role);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setBAuthorizationPk() {
        cleanEntity();

        entity.setBAuthorizationPk(pk);
        assertEquals(pk, ReflectionTestUtils.getField(entity, "bAuthorizationPk"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setUser() {
        cleanEntity();

        entity.setUser(user);
        assertEquals(user, ReflectionTestUtils.getField(entity, "user"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setEntity() {
        cleanEntity();

        entity.setEntity(oEntity);
        assertEquals(oEntity, ReflectionTestUtils.getField(entity, "entity"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void setRole() {
        cleanEntity();

        entity.setRole(role);
        assertEquals(role, ReflectionTestUtils.getField(entity, "role"));
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
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.hashCode(), entity1.hashCode());
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
        entity = new BAuthorization();
        assertEntityCleanState(entity);
    }

    private void cleanEntity1() {
        entity1 = new BAuthorization();
        assertEntityCleanState(entity1);
    }

    private void assertEntityCleanState(final BAuthorization entityArg) {
        assertNull(ReflectionTestUtils.getField(entityArg, "bAuthorizationPk"));
        assertNull(ReflectionTestUtils.getField(entityArg, "user"));
        assertNull(ReflectionTestUtils.getField(entityArg, "entity"));
        assertNull(ReflectionTestUtils.getField(entityArg, "role"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity1();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity1);
    }

    private void prepareEntityForEqualityTest(final BAuthorization entityArg) {
        ReflectionTestUtils.setField(entityArg, "bAuthorizationPk", pk);
        ReflectionTestUtils.setField(entityArg, "user", user);
        ReflectionTestUtils.setField(entityArg, "entity", oEntity);
        ReflectionTestUtils.setField(entityArg, "role", role);
    }

}
