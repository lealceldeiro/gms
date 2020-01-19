package com.gms.service;

import com.gms.Application;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.authorization.BAuthorizationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AppServiceTest {

    /**
     * Instance of {@link BAuthorizationRepository}.
     */
    @Autowired
    private BAuthorizationRepository authRepository;
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
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository permissionRepository;
    /**
     * Instance of {@link BConfigurationRepository}.
     */
    @Autowired
    private BConfigurationRepository configRepository;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isInitialLoadOKTest() {
        // clean
        authRepository.deleteAll();
        userRepository.deleteAll();
        entityRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
        configRepository.deleteAll();

        ReflectionTestUtils.setField(appService, "initialLoadOK", null);

        // create default config
        Assert.assertTrue(appService.isInitialLoadOK());
    }

}
