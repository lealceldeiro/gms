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

    @Autowired private BAuthorizationRepository authReporitory;
    @Autowired private EUserRepository userRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private BPermissionRepository permissionRepository;
    @Autowired private BConfigurationRepository configRepository;

    @Autowired private AppService appService;

    @Test
    public void isInitialLoadOKTest() {
        // clean
        authReporitory.deleteAll();
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