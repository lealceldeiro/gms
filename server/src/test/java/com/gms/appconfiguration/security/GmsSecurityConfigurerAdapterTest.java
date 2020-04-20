package com.gms.appconfiguration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.service.security.user.UserService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.StringUtil;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.SecurityConstant;
import com.gms.util.exception.domain.NotFoundEntityException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class GmsSecurityConfigurerAdapterTest {

    /**
     * Instance of {@link JUnitRestDocumentation} to create the documentation for Asciidoc from the resutls of the
     * mocked webrequests.
     */
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    /**
     * Instance of {@link WebApplicationContext}.
     */
    @Autowired
    private WebApplicationContext context;
    /**
     * Instance of {@link ObjectMapper}.
     */
    private final ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    /**
     * Instance of {@link FilterChainProxy}.
     */
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    /**
     * Instance of {@link SecurityConstant}.
     */
    @Autowired
    private SecurityConstant securityConstant;
    /**
     * Instance of {@link DefaultConstant}.
     */
    @Autowired
    private DefaultConstant defaultConstant;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link UserService}.
     */
    @Autowired
    private UserService userService;

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
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;

    /**
     * Instance of {@link MockMvc} to be used to perform web request.
     */
    private MockMvc mvc;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    /**
     * Header to be set with the authentication token.
     */
    private String authHeader;
    /**
     * Token type.
     */
    private String tokenType;
    /**
     * The access token.
     */
    private String accessToken;

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMockForwardUrlNotNull(context, restDocumentation, restDocResHandler,
                                                      springSecurityFilterChain);

        authHeader = securityConstant.getATokenHeader();
        tokenType = securityConstant.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                securityConstant,
                                                                mvc,
                                                                objectMapper,
                                                                restDocResHandler,
                                                                false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void login() throws Exception { // for documentation purposes
        assertNotEquals("",
                        GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                  securityConstant,
                                                                  mvc,
                                                                  objectMapper,
                                                                  restDocResHandler,
                                                                  false));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void loginKO() throws Exception {
        assertEquals("", GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                   securityConstant,
                                                                   mvc,
                                                                   objectMapper,
                                                                   true));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void loginOKNoAuthorities() throws Exception {
        // region role
        BRole roleWithoutPermissions = roleRepository.save(EntityUtil.getSampleRole());
        assertNotNull(roleWithoutPermissions);
        // endregion

        // region entity
        EOwnedEntity entity = entityRepository.save(EntityUtil.getSampleEntity());
        assertNotNull(entity);
        // endregion

        // region user
        String r = new GMSRandom().nextString();
        String password = StringUtil.EXAMPLE_PASSWORD;
        EUser user = new EUser(StringUtil.EXAMPLE_USERNAME + r, "sc" + r + "@test.com", StringUtil.EXAMPLE_NAME + r,
                               StringUtil.EXAMPLE_LAST_NAME + r, password);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setAccountNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        assertNotNull(userRepository.save(user));
        // endregion

        Collection<Long> ids = new LinkedList<>();
        ids.add(roleWithoutPermissions.getId());

        try {
            assertEquals("Role could not be added to user",
                         userService.addRolesToUser(user.getId(), entity.getId(), ids).size(), ids.size());

            Map<String, String> loginData = new HashMap<>();
            loginData.put(securityConstant.getReqUsernameHolder(), user.getUsername());
            loginData.put(securityConstant.getReqPasswordHolder(), password);

            mvc.perform(post(defaultConstant.getApiBasePath() + securityConstant.getSignInUrl())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginData)))
                    .andExpect(status().isUnauthorized());
        } catch (NotFoundEntityException e) {
            LoggerFactory.getLogger(GmsSecurityConfigurerAdapterTest.class).error(e.getLocalizedMessage());
            fail("Role could not be added to user");
        }

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void baseUrlPermitAll() throws Exception {
        int s = mvc.perform(get("/")).andReturn().getResponse().getStatus();
        assertTrue(s != HttpStatus.FORBIDDEN.value());
        assertTrue(s != HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void apiUrlsAreProtected() throws Exception {
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnAccessTokenIntegrityCompromised() throws Exception {
        // region completely wrong token
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permissions")
                            .header(securityConstant.getATokenHeader(), "sampleOfWrongToken"))
                .andExpect(status().isUnauthorized());
        // endregion

        // region token manipulated (string appended)
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permissions")
                            .header(securityConstant.getATokenHeader(), accessToken + "manipulated"))
                .andExpect(status().isUnauthorized());
        // endregion
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnAccessTokenDoesNotStartProperly() throws Exception {
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permissions")
                            .header(securityConstant.getATokenHeader(), new GMSRandom().nextString() + accessToken))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnNotAccessTokenHeaderProvided() throws Exception {
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void successOnCorrectAccessTokenProvided() throws Exception {
        mvc.perform(get(defaultConstant.getApiBasePath() + "/permission")
                            .header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk());
    }

}
