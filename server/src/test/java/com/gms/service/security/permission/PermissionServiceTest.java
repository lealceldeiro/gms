package com.gms.service.security.permission;

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
import com.gms.testutil.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.permission.BPermissionConst;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PermissionServiceTest {

    /**
     * Instance of {@link PermissionService}.
     */
    @Autowired
    private PermissionService permissionService;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository permissionRepository;
    /**
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link BAuthorizationRepository}.
     */
    @Autowired
    private BAuthorizationRepository authRepository;

    /**
     * Instance of {@link GMSRandom}.
     */
    private final GMSRandom random = new GMSRandom();

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() {
        assertTrue(appService.isInitialLoadOK());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createDefaultPermissions() {
        final BPermissionConst[] values = BPermissionConst.values();
        for (BPermissionConst value : values) {
            assertNotNull(permissionRepository.findFirstByName(value.toString()));
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void findPermissionsByUserIdAndEntityId() {
        // create user and entity
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);

        // create permissions
        BPermission p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p1);
        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(p2);

        // create roles and assign permissions
        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p1, p2);
        roleRepository.save(r);

        // create authorization with info about user, entity and role(s)
        BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk(u.getId(), e.getId(), r.getId());
        assertNotNull(authRepository.save(new BAuthorization(pk, u, e, r)));

        // check if permissions are really associated to user over entity through BAuthorization (through BRole)
        final List<BPermission> permissions =
                permissionService.findPermissionsByUserIdAndEntityId(u.getId(), e.getId());
        assertTrue(permissions.contains(p1));
        assertTrue(permissions.contains(p2));
    }

}
