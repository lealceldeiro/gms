package com.gms.repository.security.authorization;

import com.gms.Application;
import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.util.GMSRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class BAuthorizationRepositoryTest {

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;
    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link BAuthorizationRepository}.
     */
    @Autowired
    private BAuthorizationRepository authorizationRepository;

    /**
     * Instance of {@link EUser}.
     */
    private EUser user;
    /**
     * Instance of {@link BRole}.
     */
    private BRole role;
    /**
     * Instance of {@link BRole}.
     */
    private BRole role2;
    /**
     * Instance of {@link EOwnedEntity}.
     */
    private EOwnedEntity entity;

    /**
     * Instance of {@link GMSRandom}.
     */
    private final GMSRandom random = new GMSRandom();

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void findFirstByUserAndEntityNotNullAndRoleEnabled() {
        createSampleAuthorization();

        final BAuthorization authFound =
                authorizationRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(user, true);

        assertEquals("Current user and the one found in the authorization do not match", authFound.getUser(), user);
        assertEquals("Current role and the one found in the authorization do not match", authFound.getRole(), role);
        assertEquals(
                "Current entity and the one found in the authorization do not match",
                authFound.getEntity(), entity
        );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRolesForUserOverAllEntities() {
        final EOwnedEntity e1 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        final EOwnedEntity e2 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        createSampleAuthorization(e1, e2);

        final Map<String, List<BRole>> data = authorizationRepository.getRolesForUserOverAllEntities(user.getId());

        assertTrue("Roles not found for entity " + e1.getUsername(), data.containsKey(e1.getUsername()));
        assertTrue("Roles not found for entity " + e2.getUsername(), data.containsKey(e2.getUsername()));

        assertTrue("Role " + role.getLabel() + "(role) is not assigned to " + e1.getUsername() + " (e1)",
                isRoleInList(data.get(e1.getUsername()), role));
        assertTrue("Role " + role2.getLabel() + "(role2) is not assigned to " + e1.getUsername() + " (e1)",
                isRoleInList(data.get(e1.getUsername()), role));
        assertTrue("Role " + role.getLabel() + "(role) is not assigned to " + e2.getUsername() + " (e2)",
                isRoleInList(data.get(e2.getUsername()), role));
        assertTrue("Role " + role2.getLabel() + "(role2) is not assigned to " + e2.getUsername() + " (e2)",
                isRoleInList(data.get(e2.getUsername()), role));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRolesForUserOverEntity() {
        final BRole iRole1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        final BRole iRole2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));

        createSampleAuthorization(iRole1, iRole2);

        final List<BRole> foundRoles = authorizationRepository.getRolesForUserOverEntity(user.getId(), entity.getId());
        boolean oldFound = isRoleInList(foundRoles, iRole1);
        boolean newFound = isRoleInList(foundRoles, iRole2);

        assertTrue("The role " + iRole1.getLabel() + "(old) is not assigned to user over the entity", oldFound);
        assertTrue("The role " + iRole2.getLabel() + "(new) is not assigned to user over the entity", newFound);

    }

    private boolean isRoleInList(final List<BRole> list, final BRole roleArg) {
        boolean found = false;
        int i = 0;
        while (!found && i < list.size()) {
            found = list.get(i).getId().equals(roleArg.getId());
            i++;
        }
        return found;
    }

    private void createSampleAuthorization(final BRole... rolesSavedInRepository) {
        user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        entity = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        for (BRole iRole : rolesSavedInRepository) {
            createAuthorization(user, iRole, entity);
        }
    }

    private void createSampleAuthorization(final EOwnedEntity... entitiesSavedInRepository) {
        user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        role = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        role2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        for (EOwnedEntity iEntity : entitiesSavedInRepository) {
            createAuthorization(user, role, iEntity);
            createAuthorization(user, role2, iEntity);
        }
    }

    private void createSampleAuthorization() {
        role = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        createSampleAuthorization(role);
    }

    private void createAuthorization(final EUser userArg, final BRole roleArg, final EOwnedEntity entityArg) {
        BAuthorization.BAuthorizationPk pk =
                new BAuthorization.BAuthorizationPk(userArg.getId(), entityArg.getId(), roleArg.getId());
        assertNotNull(authorizationRepository.save(new BAuthorization(pk, userArg, entityArg, roleArg)));
    }

}
