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
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * BAuthorizationRepositoryTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 17, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BAuthorizationRepositoryTest {

    @Autowired private AppService appService;
    @Autowired private EUserRepository userRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private BAuthorizationRepository authorizationRepository;

    private EUser user;
    private BRole role;
    private BRole role2;
    private EOwnedEntity entity;

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());
    }

    @Test
    public void findFirstByUserAndEntityNotNullAndRoleEnabled() {
        createSampleAuthorization();

        final BAuthorization authFound = authorizationRepository.findFirstByUserAndEntityNotNullAndRoleEnabled(user, true);

        assertTrue("Current user and the one found in the authorization do not match", authFound.getUser().equals(user));
        assertTrue("Current role and the one found in the authorization do not match", authFound.getRole().equals(role));
        assertTrue("Current entity and the one found in the authorization do not match", authFound.getEntity().equals(entity));
    }

    @Test
    public void getRolesForUserOverAllEntities() {
        final EOwnedEntity e1 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        final EOwnedEntity e2 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        createSampleAuthorization(e1, e2);

        final Map<String, List<BRole>> data = authorizationRepository.getRolesForUserOverAllEntities(user.getId());

        assertTrue("Roles not found for entity " + e1.getUsername(), data.keySet().contains(e1.getUsername()));
        assertTrue("Roles not found for entity " + e2.getUsername(), data.keySet().contains(e2.getUsername()));

        assertTrue("Role " + role.getLabel() + "(role) is not assigned to " + e1.getUsername() + " (e1)",
                isRoleInList(data.get(e1.getUsername()), role));
        assertTrue("Role " + role2.getLabel() + "(role2) is not assigned to " + e1.getUsername() + " (e1)",
                isRoleInList(data.get(e1.getUsername()), role));
        assertTrue("Role " + role.getLabel() + "(role) is not assigned to " + e2.getUsername() + " (e2)",
                isRoleInList(data.get(e2.getUsername()), role));
        assertTrue("Role " + role2.getLabel() + "(role2) is not assigned to " + e2.getUsername() + " (e2)",
                isRoleInList(data.get(e2.getUsername()), role));
    }

    @Test
    public void getRolesForUserOverEntity() {
        final BRole role1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        final BRole role2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));

        createSampleAuthorization(role1, role2);

        final List<BRole> foundRoles = authorizationRepository.getRolesForUserOverEntity(user.getId(), entity.getId());
        boolean oldFound = isRoleInList(foundRoles, role1);
        boolean newFound = isRoleInList(foundRoles, role2);

        assertTrue("The role " + role1.getLabel() + "(old) is not assigned to user over the entity", oldFound);
        assertTrue("The role " + role2.getLabel() + "(new) is not assigned to user over the entity", newFound);

    }

    private boolean isRoleInList(List<BRole> list, BRole role) {
        boolean found = false;
        int i = 0;
        while (!found && i < list.size()) {
            found = list.get(i++).getId().equals(role.getId());
        }
        return found;
    }

    private void createSampleAuthorization(BRole... rolesSavedInRepository) {
        user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        entity = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        for (BRole role: rolesSavedInRepository) {
            createAuthorization(user, role, entity);
        }
    }

    private void createSampleAuthorization(EOwnedEntity... entitiesSavedInRepository) {
        user = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        role = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        role2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        for (EOwnedEntity entity: entitiesSavedInRepository) {
            createAuthorization(user, role, entity);
            createAuthorization(user, role2, entity);
        }
    }

    private void createSampleAuthorization() {
        role = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        createSampleAuthorization(role);
    }

    private void createAuthorization(EUser user, BRole role, EOwnedEntity entity) {
        BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();
        pk.setUserId(user.getId());
        pk.setRoleId(role.getId());
        pk.setEntityId(entity.getId());

        BAuthorization authorization = new BAuthorization();
        authorization.setBAuthorizationPk(pk);
        authorization.setUser(user);
        authorization.setRole(role);
        authorization.setEntity(entity);

        assertNotNull(authorizationRepository.save(authorization));
    }
}