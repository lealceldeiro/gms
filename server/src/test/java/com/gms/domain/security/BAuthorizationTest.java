package com.gms.domain.security;

import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.testutil.EntityUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BAuthorizationTest {

    private final Long uId = 999999999L;
    private final Long eId = 9999999999L;
    private final Long rId = 99999999999L;
    private final EUser user = EntityUtil.getSampleUser();
    private final EOwnedEntity oEntity = EntityUtil.getSampleEntity();
    private final BRole role = EntityUtil.getSampleRole();
    private final BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();

    private BAuthorization entity;
    private BAuthorization entity1;

    @Before
    public void setUp() {
        pk.setUserId(uId);
        pk.setEntityId(eId);
        pk.setRoleId(rId);

        user.setId(uId);
        oEntity.setId(eId);
        role.setId(rId);
    }

    @Test
    public void getBAuthorizationPk() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "bAuthorizationPk", pk);
        assertEquals(entity.getBAuthorizationPk(), pk);
    }

    @Test
    public void getUser() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "user", user);
        assertEquals(entity.getUser(), user);
    }

    @Test
    public void getEntity() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "entity", oEntity);
        assertEquals(entity.getEntity(), oEntity);
    }

    @Test
    public void getRole() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "role", role);
        assertEquals(entity.getRole(), role);
    }

    @Test
    public void setBAuthorizationPk() {
        cleanEntity();

        entity.setBAuthorizationPk(pk);
        assertEquals(pk, ReflectionTestUtils.getField(entity, "bAuthorizationPk"));
    }

    @Test
    public void setUser() {
        cleanEntity();

        entity.setUser(user);
        assertEquals(user, ReflectionTestUtils.getField(entity, "user"));
    }

    @Test
    public void setEntity() {
        cleanEntity();

        entity.setEntity(oEntity);
        assertEquals(oEntity, ReflectionTestUtils.getField(entity, "entity"));
    }

    @Test
    public void setRole() {
        cleanEntity();

        entity.setRole(role);
        assertEquals(role, ReflectionTestUtils.getField(entity, "role"));
    }

    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity, entity1);
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertEquals(entity.hashCode(), entity1.hashCode());
    }

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

    private void assertEntityCleanState(BAuthorization entity) {
        assertNull(ReflectionTestUtils.getField(entity, "bAuthorizationPk"));
        assertNull(ReflectionTestUtils.getField(entity, "user"));
        assertNull(ReflectionTestUtils.getField(entity, "entity"));
        assertNull(ReflectionTestUtils.getField(entity, "role"));
    }

    private void prepareEntitiesForEqualityTest() {
        cleanEntity();
        cleanEntity1();

        prepareEntityForEqualityTest(entity);
        prepareEntityForEqualityTest(entity1);
    }

    private void prepareEntityForEqualityTest(BAuthorization entity) {
        ReflectionTestUtils.setField(entity, "bAuthorizationPk", pk);
        ReflectionTestUtils.setField(entity, "user", user);
        ReflectionTestUtils.setField(entity, "entity", oEntity);
        ReflectionTestUtils.setField(entity, "role", role);
    }
}