package com.gms.domain.security;

import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * BAuthorizationTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 07, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BAuthorizationTest {

    private final Long uId = 999999999L;
    private final Long eId = 9999999999L;
    private final Long rId = 99999999999L;
    private final EUser user = new EUser("sampleU", "sample@email.com", "sampleN", "sampleL", "sampleP");
    private final EOwnedEntity oEntity = new EOwnedEntity("sampleOWN", "sampleOWU", "sampleOWD");
    private final BRole role = new BRole("sampleRL");
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
        assertTrue(entity.getBAuthorizationPk().equals(pk));
    }

    @Test
    public void getUser() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "user", user);
        assertTrue(entity.getUser().equals(user));
    }

    @Test
    public void getEntity() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "entity", oEntity);
        assertTrue(entity.getEntity().equals(oEntity));
    }

    @Test
    public void getRole() {
        cleanEntity();

        ReflectionTestUtils.setField(entity, "role", role);
        assertTrue(entity.getRole().equals(role));
    }

    @Test
    public void setBAuthorizationPk() {
        cleanEntity();

        entity.setBAuthorizationPk(pk);
        assertTrue(pk.equals(ReflectionTestUtils.getField(entity, "bAuthorizationPk")));
    }

    @Test
    public void setUser() {
        cleanEntity();

        entity.setUser(user);
        assertTrue(user.equals(ReflectionTestUtils.getField(entity, "user")));
    }

    @Test
    public void setEntity() {
        cleanEntity();

        entity.setEntity(oEntity);
        assertTrue(oEntity.equals(ReflectionTestUtils.getField(entity, "entity")));
    }

    @Test
    public void setRole() {
        cleanEntity();

        entity.setRole(role);
        assertTrue(role.equals(ReflectionTestUtils.getField(entity, "role")));
    }

    @Test
    public void equalsTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.equals(entity1));
    }

    @Test
    public void hashCodeTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.hashCode() == entity1.hashCode());
    }

    @Test
    public void toStringTest() {
        prepareEntitiesForEqualityTest();

        assertTrue(entity.toString().equals(entity1.toString()));
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