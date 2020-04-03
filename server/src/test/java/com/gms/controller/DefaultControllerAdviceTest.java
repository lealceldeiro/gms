package com.gms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.gms.testutil.EntityUtil.getSampleUserResource;
import static com.gms.testutil.StringUtil.EXAMPLE_EMAIL;
import static com.gms.testutil.StringUtil.EXAMPLE_LAST_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DefaultControllerAdviceTest {

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
     * Instance of {@link SecurityConst}.
     */
    @Autowired
    private SecurityConst sc;
    /**
     * Instance of {@link DefaultConst}.
     */
    @Autowired
    private DefaultConst dc;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link ConfigurationService}.
     */
    @Autowired
    private ConfigurationService configService;
    /**
     * Instance of {@link MessageResolver}.
     */
    @Autowired
    private MessageResolver msg;
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
     * Instance of {@link MockMvc}.
     */
    private MockMvc mvc;

    /**
     * Header to be used for sending authentication credentials.
     */
    private String authHeader;
    /**
     * Type of the authorization token.
     */
    private String tokenType;
    /**
     * Access token used to get access to secured resources.
     */
    private String accessToken;
    /**
     * Base path for the API.
     */
    private String apiPrefix;

    /**
     * An alphanumeric random generator.
     */
    private final GMSRandom random = new GMSRandom(10);

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleNotFoundEntityException() throws Exception {
        doNotFound("user");
        doNotFound("entity");
    }

    private void doNotFound(final String whichOne) throws Exception {
        final long invalidId = -999999999999999999L;
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        Long userId = invalidId;
        Long entityId = e.getId();

        //"user"
        if ("entity".equals(whichOne)) {
            entityId = invalidId;
            userId = u.getId();
        }

        mvc.perform(post(
                apiPrefix + "/" + ResourcePath.USER + "/" + ResourcePath.ROLE + "s/{userId}/{entityId}",
                userId,
                entityId
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(new ArrayList<>()))
        ).andExpect(status().isNotFound());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleGmsSecurityException() throws Exception {
        RefreshTokenPayload payload = new RefreshTokenPayload(null);
        checkResult(doRequest(payload));
        payload = new RefreshTokenPayload("invalidToken");
        checkResult(doRequest(payload));
    }

    private void checkResult(final MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
        JSONObject res = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("HttpStatuses do not match.", HttpStatus.UNAUTHORIZED.value(), res.getInt("status"));
        assertEquals(
                "Error messages do not match.",
                res.getString("error"),
                msg.getMessage("security.unauthorized")
        );
        assertEquals(
                "Paths do not match.",
                res.getString("path"),
                dc.getApiBasePath() + "/" + SecurityConst.ACCESS_TOKEN_URL
        );
    }

    private MvcResult doRequest(final RefreshTokenPayload payload) throws Exception {
        return mvc.perform(
                post(apiPrefix + "/" + SecurityConst.ACCESS_TOKEN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isUnauthorized()).andReturn();
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleGmsGeneralException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // do not allow new user registration
        if (initial) {
            configService.setUserRegistrationAllowed(false);
        }

        EntityModel<EUser> resource = getSampleUserResource();
        final MockHttpServletResponse result = mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andReturn().getResponse();

        assertNotEquals("Status expected: not <404> but was:<404>", HttpStatus.NOT_FOUND.value(), result.getStatus());

        JSONObject resObj = new JSONObject(result.getContentAsString());

        String suffix = msg.getMessage("request.finished.KO"); // request is not supposed to finish ok in this scenario
        assertTrue("Request is supposed to finish KO in this scenario",
                resObj.getString(dc.getResMessageHolder()).endsWith(suffix));

        // restart initial config
        if (initial) {
            configService.setUserRegistrationAllowed(true);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleTransactionSystemException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            configService.setUserRegistrationAllowed(true);
        }

        final String r = random.nextString();
        EUser u = new EUser(null, "a" + r + EXAMPLE_EMAIL, EXAMPLE_NAME + r,
                EXAMPLE_LAST_NAME, EXAMPLE_PASSWORD);
        EntityModel<EUser> resource = new EntityModel<>(u);
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());

        // restart initial config
        if (!initial) {
            configService.setUserRegistrationAllowed(false);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            configService.setUserRegistrationAllowed(true);
        }

        final String r = random.nextString();
        EntityModel<EUser> resource = getSampleUserResource(r);
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated());

        resource = getSampleUserResource(r);
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());

        // restart initial config
        if (!initial) {
            configService.setUserRegistrationAllowed(false);
        }
    }

}
