package com.gms.service.security.user;

import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.GMSRandom;
import com.gms.util.StringUtil;
import com.gms.util.constant.DefaultConst;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    @Autowired private AppService appService;
    @Autowired private UserService userService;
    @Autowired private ConfigurationService configService;
    @Autowired DefaultConst dc;

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
    }

    @Test
    public void removeRolesFromUser() {
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