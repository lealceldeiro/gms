package com.gms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import com.gms.util.request.mapping.user.RolesForUserOverEntity;
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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BaseControllerTest {

    @Autowired private WebApplicationContext context;

    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private ConfigurationService configService;
    @Autowired private MessageResolver msg;

    private MockMvc mvc;

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private final GMSRandom random = new GMSRandom(10);

    @Before
    public void setUp() throws Exception {
        org.junit.Assert.assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(this.springSecurityFilterChain)
                .alwaysExpect(forwardedUrl(null))
                .build();
        apiPrefix = dc.getApiBasePath();

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void handleNotFoundEntityException() throws Exception {
        long testId = -11141851135L;
        RolesForUserOverEntity payload = new RolesForUserOverEntity();
        payload.setUserId(testId);
        payload.setEntityId(testId);
        mvc.perform(
                post(apiPrefix + "/roles/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(payload))
        )
                .andExpect(status().isNotFound());
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
        assertTrue("HttpStatus do not match.", res.getInt("status") == HttpStatus.UNAUTHORIZED.value());
        assertTrue("Error messages do not match.", res.getString("error").equals(msg.getMessage("security.unauthorized")));
        assertTrue("Paths do not match.", res.getString("path").equals(dc.getApiBasePath() + "/access_token"));
    }

    private MvcResult doRequest(RefreshTokenPayload payload) throws Exception {
        return mvc.perform(
                post(apiPrefix + "/access_token").contentType(MediaType.APPLICATION_JSON)
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

        String fd = random.nextString();
        EUser u = new EUser(fd, "bcTest" + fd + "@test.com", fd, fd, fd);
        Resource<EUser> resource = new Resource<>(u);
        final String response = mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andReturn().getResponse().getContentAsString();
        JSONObject resObj = new JSONObject(response);

        String suffix = msg.getMessage("request.finished.KO"); // request is not supposed to finish ok in this scenario
        assertTrue("", resObj.getString(dc.getResMessageHolder()).endsWith(suffix));

        // restart initial config
        if (initial) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
    }
}