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
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.constant.BPermissionConst;
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
 * PermissionServiceTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 04, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PermissionServiceTest {

    @Autowired private PermissionService permissionService;

    @Autowired private AppService appService;
    @Autowired private BPermissionRepository permissionRepository;
    @Autowired private EUserRepository userRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private BAuthorizationRepository authRepository;

    private GMSRandom random = new GMSRandom();

    @Before
    public void setUp() {
        assertTrue(appService.isInitialLoadOK());
    }


    @Test
    public void createDefaultPermissions() {
        final BPermissionConst[] values = BPermissionConst.values();
        for (BPermissionConst value : values) {
            assertNotNull(permissionRepository.findFirstByName(value.toString()));
        }
    }

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
        BAuthorization.BAuthorizationPk pk = new BAuthorization.BAuthorizationPk();
        pk.setUserId(u.getId());
        pk.setEntityId(e.getId());
        pk.setRoleId(r.getId());

        BAuthorization auth = new BAuthorization();
        auth.setBAuthorizationPk(pk);
        auth.setUser(u);
        auth.setEntity(e);
        auth.setRole(r);

        auth = authRepository.save(auth);
        assertNotNull(auth);

        // check if permissions are really associated to user over entity through BAuthorization (through BRole)
        final List<BPermission> permissions = permissionService.findPermissionsByUserIdAndEntityId(u.getId(), e.getId());
        assertTrue(permissions.contains(p1));
        assertTrue(permissions.contains(p2));
    }
}