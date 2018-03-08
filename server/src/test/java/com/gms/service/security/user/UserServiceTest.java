package com.gms.service.security.user;

import com.gms.Application;
import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.StringUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * UserServiceTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 06, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Autowired private EUserRepository userRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private BAuthorizationRepository authorizationRepository;
    @Autowired private AppService appService;
    @Autowired private UserService userService;
    @Autowired private ConfigurationService configService;
    @Autowired private DefaultConst dc;

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());
    }


    @Test
    public void createDefaultUser() {
        assertNotNull(userRepository.findFirstByUsername(dc.getUserAdminDefaultUsername()));
    }

    @Test
    public void signServiceUpOK() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (!registration) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
        EUser u = getUser();
        u = userService.signUp(u, true);
        assertNotNull(u);

        EUser ru = userRepository.findFirstByUsername(u.getUsername());

        assertEquals(u, ru);
        if (!registration) {
            assertTrue(configService.setUserRegistrationAllowed(false));
        }
    }

    @Test
    public void signServiceUpKO() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(false));
        }

        assertNull(userService.signUp(getUser(), true));

        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
    }

    @Test
    public void signUpSuperRegistrationConditionallyOK() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(false));
        }
        EUser u = getUser();
        u = userService.signUp(u, true, true);
        assertNotNull(u);

        EUser ru = userRepository.findFirstByUsername(u.getUsername());
        assertEquals(u, ru);

        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
    }

    @Test
    public void signUpSuperRegistrationConditionallyKO() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(false));
        }
        EUser u = getUser();
        assertNull(userService.signUp(u, true, false));

        if (registration) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
    }

    @Test
    public void addRolesToUser() {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        BRole r1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r1);
        BRole r2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r2);
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        List<Long> ids = new LinkedList<>();
        ids.add(r1.getId());
        ids.add(r2.getId());
        assertTrue(ids.size() == 2);    // make sure later a java.lang.ArrayIndexOutOfBoundsException is not thrown
        List<Long> added = null;
        try {
            added = userService.addRolesToUser(u.getUsername(), e.getUsername(), ids);
            assertNotNull(added);
            assertTrue(added.size() == ids.size());
            assertTrue(added.contains(ids.get(0)));
            assertTrue(added.contains(ids.get(1)));
        } catch (NotFoundEntityException e1) {
            e1.printStackTrace();
            fail("Roles could not be saved");
        }

        final List<BRole> roles = authorizationRepository.getRolesForUserOverEntity(u.getId(), e.getId());
        assertNotNull(roles);
        assertTrue(roles.size() == ids.size());
        assertNotNull(added);
        assertTrue(added.size() == roles.size());
        assertTrue(added.contains(roles.get(0).getId()));
        assertTrue(added.contains(roles.get(1).getId()));
    }

    @Test
    public void removeRolesFromUser() {
        // add roles to user via repository
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);

        BRole r1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r1);

        BRole r2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r2);

        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);

        BAuthorization.BAuthorizationPk pk1 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r1.getId());
        assertNotNull(pk1);
        BAuthorization auth1 = authorizationRepository.save(new BAuthorization(pk1, u, e, r1));
        assertNotNull(auth1);
        BAuthorization.BAuthorizationPk pk2 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r2.getId());
        assertNotNull(pk1);
        BAuthorization auth2 = authorizationRepository.save(new BAuthorization(pk2, u, e, r2));
        assertNotNull(auth2);

        List<Long> ids = new LinkedList<>();
        ids.add(r1.getId());
        ids.add(r2.getId());

        //check the roles where successfully added
        List<BRole> roles = authorizationRepository.getRolesForUserOverEntity(u.getId(), e.getId());
        assertNotNull(roles);
        assertTrue(roles.size() == ids.size());
        assertTrue(ids.contains(roles.get(0).getId()));
        assertTrue(ids.contains(roles.get(1).getId()));

        // check the service method
        List<Long> removed = null;
        try {
             removed = userService.removeRolesFromUser(u.getUsername(), e.getUsername(), ids);
        } catch (NotFoundEntityException e1) {
            e1.printStackTrace();
            fail("Roles could not be removed");
        }
        assertNotNull(removed);
        assertTrue(removed.size() == roles.size());
        assertTrue(removed.contains(roles.get(0).getId()));
        assertTrue(removed.contains(roles.get(1).getId()));

        // check via repository the roles were successfully removed
        roles = authorizationRepository.getRolesForUserOverEntity(u.getId(), e.getId());
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void loadUserByUsername() {
    }

    @Test
    public void getUserAuthoritiesForToken() {
    }

    @Test
    public void getRolesForUser() {
    }

    @Test
    public void getRolesForUserOverEntity() {
    }

    private EUser getUser() {
        return new EUser(StringUtil.EXAMPLE_USERNAME + random.nextString(), StringUtil.EXAMPLE_EMAIL + random.nextString(),
                StringUtil.EXAMPLE_NAME + random.nextString(), StringUtil.EXAMPLE_LAST_NAME + random.nextString(),
                StringUtil.EXAMPLE_PASSWORD + random.nextString());
    }
}