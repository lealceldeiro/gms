package com.gms.service.security.role;

import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.domain.NotFoundEntityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class RoleServiceTest {

    /**
     * Instance of {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceTest.class);

    /**
     * Instance of {@link RoleService}.
     */
    @Autowired
    private RoleService roleService;
    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository permissionRepository;
    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link DefaultConstant}.
     */
    @Autowired
    private DefaultConstant defaultConstant;

    /**
     * Instance of {@link GMSRandom}.
     */
    private final GMSRandom random = new GMSRandom();
    /**
     * An invalid id.
     */
    private static final long INVALID_ID = -99999999999L;

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
    public void createDefaultRole() {
        BRole r = roleRepository.findFirstByLabel(defaultConstant.getRoleAdminDefaultLabel());
        assertNotNull("Default role not created", r);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void addPermissionsToRole() {
        BRole r = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r);

        BPermission p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p1);

        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p2);

        Collection<Long> pIDs = new LinkedList<>();
        pIDs.add(p1.getId());
        pIDs.add(p2.getId());
        try {
            List<Long> added = roleService.addPermissionsToRole(r.getId(), pIDs);
            assertEquals(added.size(), pIDs.size());
            // assert the list contains the id of the two added permissions
            assertTrue(added.contains(p1.getId()));
            assertTrue(added.contains(p2.getId()));
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("Could not add permissions to  role");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void addPermissionsNotFoundToRole() {
        BRole r = roleRepository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r);

        Collection<Long> pIDs = new LinkedList<>();
        pIDs.add(INVALID_ID);

        boolean success = false;
        try {
            roleService.addPermissionsToRole(r.getId(), pIDs);
        } catch (NotFoundEntityException e) {
            success = true;
            assertEquals("role.add.permissions.found.none", e.getMessage());
        }
        assertTrue(success);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRoleNotFound() {
        boolean success = false;
        try {
            roleService.getRole(INVALID_ID);
        } catch (NotFoundEntityException e) {
            success = true;
            assertEquals(RoleService.ROLE_NOT_FOUND_MESSAGE_KEY, e.getMessage());
        }
        assertTrue(success);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void removePermissionsFromRole() {
        BPermission p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p1);

        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p2);

        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p1, p2);
        roleRepository.save(r);
        assertNotNull(r);
        Collection<Long> pIDs = new LinkedList<>();
        pIDs.add(p1.getId());
        pIDs.add(p2.getId());

        try {
            List<Long> deleted = roleService.removePermissionsFromRole(r.getId(), pIDs);
            assertNotNull(deleted);
            assertEquals(deleted.size(), pIDs.size());
            assertTrue(deleted.contains(p1.getId()));
            assertTrue(deleted.contains(p2.getId()));
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("Could not remove element");
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    @Transactional
    public void updatePermissionsInRole() {
        BPermission p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p1);
        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p2);

        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p1, p2);
        assertNotNull(roleRepository.save(r));
        assertTrue(r.getPermissions().contains(p1));
        assertTrue(r.getPermissions().contains(p2));

        BPermission p3 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p3);
        BPermission p4 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p4);
        Collection<Long> pIDs = new LinkedList<>();
        pIDs.add(p3.getId());
        pIDs.add(p4.getId());

        try {
            final List<Long> updated = roleService.updatePermissionsInRole(r.getId(), pIDs);
            assertNotNull(updated);
            // assert the role permissions list contains only the new permissions (p3 and p4)
            assertEquals(updated.size(), pIDs.size());
            assertTrue(updated.contains(p3.getId()));
            assertTrue(updated.contains(p4.getId()));
            assertFalse(updated.contains(p1.getId()));
            assertFalse(updated.contains(p2.getId()));

            Optional<BRole> role = roleRepository.findById(r.getId());
            assertTrue(role.isPresent());

            final Set<BPermission> rPermissions = role.get().getPermissions();
            assertFalse(rPermissions.contains(p1));
            assertFalse(rPermissions.contains(p2));
            assertTrue(rPermissions.contains(p3));
            assertTrue(rPermissions.contains(p4));
        } catch (NotFoundEntityException e) {
            LOGGER.error(e.getLocalizedMessage());
            fail("Could not update the role permissions");
        }
    }

}
