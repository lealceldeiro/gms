package com.gms.service.security.user;

import com.gms.Application;
import com.gms.domain.security.BAuthorization;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.permission.BPermissionRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Autowired private EUserRepository userRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private BPermissionRepository permissionRepository;
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
            configService.setUserRegistrationAllowed(true);
        }
        EUser u = getUser();
        u = userService.signUp(u, true);
        assertNotNull(u);

        EUser ru = userRepository.findFirstByUsername(u.getUsername());

        assertEquals(u, ru);
        if (!registration) {
            configService.setUserRegistrationAllowed(false);
        }
    }

    @Test
    public void signServiceUpKO() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            configService.setUserRegistrationAllowed(false);
        }

        assertNull(userService.signUp(getUser(), true));

        if (registration) {
            configService.setUserRegistrationAllowed(true);
        }
    }

    @Test
    public void signUpSuperRegistrationConditionallyOK() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            configService.setUserRegistrationAllowed(false);
        }
        EUser u = getUser();
        u = userService.signUp(u, true, true);
        assertNotNull(u);

        EUser ru = userRepository.findFirstByUsername(u.getUsername());
        assertEquals(u, ru);

        if (registration) {
            configService.setUserRegistrationAllowed(true);
        }
    }

    @Test
    public void signUpSuperRegistrationConditionallyKO() {
        final boolean registration = configService.isUserRegistrationAllowed();
        if (registration) {
            configService.setUserRegistrationAllowed(false);
        }
        EUser u = getUser();
        assertNull(userService.signUp(u, true, false));

        if (registration) {
            configService.setUserRegistrationAllowed(true);
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
            added = userService.addRolesToUser(u.getId(), e.getId(), ids);
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
             removed = userService.removeRolesFromUser(u.getId(), e.getId(), ids);
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
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        UserDetails su = userService.loadUserByUsername(u.getUsername());
        assertNotNull(su);
        assertTrue(su.equals(u));
    }

    @Test
    public void getUserAuthoritiesForToken() {
        // first set of permissions
        BPermission p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p1);
        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p2);

        BRole r1 = EntityUtil.getSampleRole(random.nextString());
        r1.addPermission(p1, p2);
        assertNotNull(roleRepository.save(r1));

        // second set of permissions
        BPermission p3 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p3);
        BPermission p4 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p4);
        BRole r2 = EntityUtil.getSampleRole(random.nextString());
        r2.addPermission(p3, p4);
        assertNotNull(roleRepository.save(r2));

        // user
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);

        // authorities
        // 1
        BAuthorization.BAuthorizationPk pk1 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r1.getId());
        BAuthorization auth1 = new BAuthorization(pk1, u, e, r1);
        assertNotNull(authorizationRepository.save(auth1));
        // 2
        BAuthorization.BAuthorizationPk pk2 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r2.getId());
        BAuthorization auth2 = new BAuthorization(pk2, u, e, r2);
        assertNotNull(authorizationRepository.save(auth2));

        // test
        String separator = "--<<" + random.nextString() + ">>--"; // attempt to make a unique separator
        String authForToken = userService.getUserAuthoritiesForToken(u.getUsername(), separator);
        assertNotNull(authForToken);
        assertTrue(!authForToken.equals(""));

        List<String> permissionNames = Arrays.asList(authForToken.split(separator));
        assertTrue(permissionNames.contains(p1.getName()));
        assertTrue(permissionNames.contains(p2.getName()));
        assertTrue(permissionNames.contains(p3.getName()));
        assertTrue(permissionNames.contains(p4.getName()));

    }

    @Test
    public void getRolesForUser() {
        // user
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        // entities
        EOwnedEntity e1 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e1);
        EOwnedEntity e2 = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e2);

        // roles
        BRole r1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r1);
        BRole r2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r2);
        BRole r3 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r3);
        BRole r4 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r4);

        // authorities
        // entity 1, role 1 and 2
        BAuthorization.BAuthorizationPk pk1 = new BAuthorization.BAuthorizationPk(u.getId(), e1.getId(), r1.getId());
        BAuthorization auth1 = authorizationRepository.save(new BAuthorization(pk1, u, e1, r1));
        assertNotNull(auth1);

        BAuthorization.BAuthorizationPk pk2 = new BAuthorization.BAuthorizationPk(u.getId(), e1.getId(), r2.getId());
        BAuthorization auth2 = authorizationRepository.save(new BAuthorization(pk2, u, e1, r2));
        assertNotNull(auth2);

        // entity 2, role 3 and 4
        BAuthorization.BAuthorizationPk pk3 = new BAuthorization.BAuthorizationPk(u.getId(), e2.getId(), r3.getId());
        BAuthorization auth3 = authorizationRepository.save(new BAuthorization(pk3, u, e2, r3));
        assertNotNull(auth3);

        BAuthorization.BAuthorizationPk pk4 = new BAuthorization.BAuthorizationPk(u.getId(), e2.getId(), r4.getId());
        BAuthorization auth4 = authorizationRepository.save(new BAuthorization(pk4, u, e2, r4));
        assertNotNull(auth4);

        try {
            final Map<String, List<BRole>> rolesForUser = userService.getRolesForUser(u.getId());
            assertNotNull(rolesForUser);
            assertTrue(!rolesForUser.isEmpty());

            final Set<String> keySet = rolesForUser.keySet();

            assertTrue(keySet.contains(e1.getUsername()));
            assertTrue(keySet.contains(e2.getUsername()));

            assertTrue(rolesForUser.get(e1.getUsername()).contains(r1));
            assertTrue(rolesForUser.get(e1.getUsername()).contains(r2));

            assertTrue(rolesForUser.get(e2.getUsername()).contains(r3));
            assertTrue(rolesForUser.get(e2.getUsername()).contains(r4));
        } catch (NotFoundEntityException e) {
            e.printStackTrace();
            fail("The username was not found");
        }
    }

    @Test
    public void getRolesForUserOverEntity() {
        // user
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        // entities
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);

        // roles
        BRole r1 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r1);
        BRole r2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r2);

        // authorities
        // entity 1, role 1 and 2
        BAuthorization.BAuthorizationPk pk1 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r1.getId());
        BAuthorization auth1 = authorizationRepository.save(new BAuthorization(pk1, u, e, r1));
        assertNotNull(auth1);

        BAuthorization.BAuthorizationPk pk2 = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r2.getId());
        BAuthorization auth2 = authorizationRepository.save(new BAuthorization(pk2, u, e, r2));
        assertNotNull(auth2);

        try {
            final List<BRole> roles = userService.getRolesForUserOverEntity(u.getId(), e.getId());
            assertNotNull(roles);
            assertTrue(!roles.isEmpty());

            assertTrue(roles.contains(r1));
            assertTrue(roles.contains(r2));
        } catch (NotFoundEntityException ex) {
            ex.printStackTrace();
            fail("Either the username or the entity was not found");
        }
    }

    private EUser getUser() {
        return new EUser(StringUtil.EXAMPLE_USERNAME + random.nextString(), StringUtil.EXAMPLE_EMAIL + random.nextString(),
                StringUtil.EXAMPLE_NAME + random.nextString(), StringUtil.EXAMPLE_LAST_NAME + random.nextString(),
                StringUtil.EXAMPLE_PASSWORD + random.nextString());
    }
}