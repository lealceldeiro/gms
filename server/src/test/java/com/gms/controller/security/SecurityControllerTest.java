package com.gms.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import com.gms.util.validation.ConstrainedFields;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.gms.util.EntityUtil.getSampleUserResource;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecurityControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private ConfigurationService configService;
    @Autowired private MessageResolver msg;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    private String refreshToken;
    private String apiPrefix;

    private static final String CONFIG_NOT_RESET = "Configuration could not be re-set to its original state";

    private final GMSRandom random = new GMSRandom(10);

    @Before
    public void setUp() throws Exception{
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocResHandler)
                .addFilter(springSecurityFilterChain)
                .alwaysExpect(forwardedUrl(null))
                .build();
        apiPrefix = dc.getApiBasePath();

        refreshToken = GmsSecurityUtil.createSuperAdminRefreshToken(dc, sc, mvc, objectMapper, false);
    }

    //C
    @Test
    public void signUpUserOK() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            assertTrue(configService.setUserRegistrationAllowed(true));
        }
        final String r = random.nextString();
        EUser u = EntityUtil.getSampleUser(r);
        u.setEnabled(false);

        Resource<EUser> resource = new Resource<>(u);
        ReflectionTestUtils.setField(resource, "links", null);

        final ConstrainedFields fields = new ConstrainedFields(EUser.class);

        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("username").description(EUserMeta.username),
                                        fields.withPath("email").description(EUserMeta.email),
                                        fields.withPath("name").description(EUserMeta.name),
                                        fields.withPath("lastName").description(EUserMeta.lastName),
                                        fields.withPath("password").description(EUserMeta.password),
                                        fields.withPath("enabled").optional().description(EUserMeta.enabled),
                                        fields.withPath("authorities").optional().ignored(),
                                        fields.withPath("emailVerified").optional().ignored(),
                                        fields.withPath("accountNonExpired").optional().ignored(),
                                        fields.withPath("accountNonLocked").optional().ignored(),
                                        fields.withPath("credentialsNonExpired").optional().ignored(),
                                        fields.withPath("links").optional().ignored()
                                )
                        )
                );

        if (!initial) {
            assertTrue(CONFIG_NOT_RESET, configService.setUserRegistrationAllowed(false));
        }
    }

    @Test
    public void signUpUserKO() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();

        if (initial) {
            assertTrue(configService.setUserRegistrationAllowed(false));
        }
        Resource<EUser> resource = getSampleUserResource(random.nextString());
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isConflict());

        if (initial) {
            assertTrue(CONFIG_NOT_RESET, configService.setUserRegistrationAllowed(true));
        }
    }

    @Test
    public void refreshTokenOK() throws Exception {
        final RefreshTokenPayload payload = new RefreshTokenPayload(refreshToken);
        final ConstrainedFields fields = new ConstrainedFields(RefreshTokenPayload.class);
        mvc.perform(
                post(apiPrefix + "/access_token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isOk())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("refreshToken")
                                                .description("The refresh token provided when login was previously performed")
                                )
                        )
                );
    }

    @Test
    public void refreshTokenNull() throws Exception {
        testRefreshTokenKO(null);
    }

    @Test
    public void refreshTokenInvalid() throws Exception{
        testRefreshTokenKO("invalidRefreshToken");
    }

    private void testRefreshTokenKO(String refreshToken) throws Exception{
        final RefreshTokenPayload payload = new RefreshTokenPayload(refreshToken);
        String temp = mvc.perform(
                post(apiPrefix + "/access_token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

        JSONObject json = new JSONObject(temp);

        int status = json.getInt("status");
        assert status == HttpStatus.UNAUTHORIZED.value();

        temp = json.getString("error");
        assert temp.equals(msg.getMessage("security.unauthorized"));

        temp = json.getString("path");
        assert temp.equals(dc.getApiBasePath() + "/access_token");

        temp = json.getString(dc.getResMessageHolder());

        assert temp.equals(msg.getMessage(refreshToken == null ? "security.token.refresh.required" : "security.token.refresh.invalid"));
    }
}