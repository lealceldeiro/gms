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
import org.springframework.hateoas.Resource;
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
import static com.gms.testutil.StringUtil.*;
import static org.junit.Assert.assertEquals;
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

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private ConfigurationService configService;
    @Autowired private MessageResolver msg;
    @Autowired private EUserRepository userRepository;
    @Autowired private EOwnedEntityRepository entityRepository;

    private MockMvc mvc;

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private final GMSRandom random = new GMSRandom(10);

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc =  GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void handleNotFoundEntityException() throws Exception {
        doNotFound("user");
        doNotFound("entity");
    }

    private void doNotFound(String whichOne) throws Exception  {
        final Long INVALID_ID = -999999999999999999L;
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        Long userId = INVALID_ID;
        Long entityId = e.getId();

        switch (whichOne) {
            case "entity":
                entityId = INVALID_ID;
                userId = u.getId();
                break;
            default: //"user"
        }

        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/" + ResourcePath.ROLE + "s/{userId}/{entityId}", userId, entityId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(new ArrayList<>()))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void handleGmsSecurityException() throws Exception {
        RefreshTokenPayload payload = new RefreshTokenPayload(null);
        checkResult(doRequest(payload));
        payload = new RefreshTokenPayload("invalidToken");
        checkResult(doRequest(payload));
    }

    private void checkResult(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
        JSONObject res = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("HttpStatuses do not match.", res.getInt("status"), HttpStatus.UNAUTHORIZED.value());
        assertEquals("Error messages do not match.", res.getString("error"), msg.getMessage("security.unauthorized"));
        assertEquals("Paths do not match.", res.getString("path"), dc.getApiBasePath() + "/" + SecurityConst.ACCESS_TOKEN_URL);
    }

    private MvcResult doRequest(RefreshTokenPayload payload) throws Exception {
        return mvc.perform(
                post(apiPrefix + "/" + SecurityConst.ACCESS_TOKEN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void handleGmsGeneralException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // do not allow new user registration
        if (initial) {
            configService.setUserRegistrationAllowed(false);
        }

        Resource<EUser> resource = EntityUtil.getSampleUserResource();
        final MockHttpServletResponse result = mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andReturn().getResponse();

        assertTrue("Status expected: not <404> but was:<404>", result.getStatus() != HttpStatus.NOT_FOUND.value());

        JSONObject resObj = new JSONObject(result.getContentAsString());

        String suffix = msg.getMessage("request.finished.KO"); // request is not supposed to finish ok in this scenario
        assertTrue("Request is supposed to finish KO in this scenario", resObj.getString(dc.getResMessageHolder()).endsWith(suffix));

        // restart initial config
        if (initial) {
            configService.setUserRegistrationAllowed(true);
        }
    }

    @Test
    public void handleTransactionSystemException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            configService.setUserRegistrationAllowed(true);
        }

        final String r = random.nextString();
        EUser u = new EUser(null ,"a" + r + EXAMPLE_EMAIL, EXAMPLE_NAME + r,
                EXAMPLE_LAST_NAME, EXAMPLE_PASSWORD);
        Resource<EUser> resource = new Resource<>(u);
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());

        // restart initial config
        if (!initial) {
            configService.setUserRegistrationAllowed(false);
        }
    }

    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            configService.setUserRegistrationAllowed(true);
        }

        final String r = random.nextString();
        Resource<EUser> resource = getSampleUserResource(r);
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