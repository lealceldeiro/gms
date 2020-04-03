package com.gms.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.security.RefreshTokenPayload;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.gms.testutil.EntityUtil.getSampleUserResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecurityControllerTest {

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
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    /**
     * The refresh token used to get access to some protected resource.
     */
    private String refreshToken;
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

        mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();

        refreshToken = GmsSecurityUtil.createSuperAdminRefreshToken(dc, sc, mvc, objectMapper, false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void signUp() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();
        // allow new user registration
        if (!initial) {
            configService.setUserRegistrationAllowed(true);
        }
        final String r = random.nextString();
        EUser u = EntityUtil.getSampleUser(r);
        u.setEnabled(false);

        EntityModel<EUser> resource = new EntityModel<>(u);

        final ConstrainedFields fields = new ConstrainedFields(EUser.class);

        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("username").description(EUserMeta.USERNAME_INFO),
                                        fields.withPath("email").description(EUserMeta.EMAIL_INFO),
                                        fields.withPath("name").description(EUserMeta.NAME_INFO),
                                        fields.withPath("lastName").description(EUserMeta.LAST_NAME_INFO),
                                        fields.withPath("password").description(EUserMeta.PASSWORD_INFO),
                                        fields.withPath("enabled").optional().description(EUserMeta.ENABLED_INFO),
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
            configService.setUserRegistrationAllowed(false);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void signUpKO() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();

        if (initial) {
            configService.setUserRegistrationAllowed(false);
        }
        EntityModel<EUser> resource = getSampleUserResource(random.nextString());
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isConflict());

        if (initial) {
            configService.setUserRegistrationAllowed(true);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void refreshToken() throws Exception {
        final RefreshTokenPayload payload = new RefreshTokenPayload(refreshToken);
        final ConstrainedFields fields = new ConstrainedFields(RefreshTokenPayload.class);
        mvc.perform(
                post(apiPrefix + "/" + SecurityConst.ACCESS_TOKEN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isOk())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("refreshToken")
                                                .description("The refresh token provided when login was "
                                                        + "previously performed")
                                )
                        )
                );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void refreshTokenNull() throws Exception {
        testRefreshTokenKO(null);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void refreshTokenInvalid() throws Exception {
        testRefreshTokenKO("invalidRefreshToken");
    }

    private void testRefreshTokenKO(final String refreshTokenArg) throws Exception {
        final RefreshTokenPayload payload = new RefreshTokenPayload(refreshTokenArg);
        String temp = mvc.perform(
                post(apiPrefix + "/" + SecurityConst.ACCESS_TOKEN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

        JSONObject json = new JSONObject(temp);

        int status = json.getInt("status");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), status);

        temp = json.getString("error");
        assertEquals(temp, msg.getMessage("security.unauthorized"));

        temp = json.getString("path");
        assertEquals(temp, dc.getApiBasePath() + "/" + SecurityConst.ACCESS_TOKEN_URL);

        temp = json.getString(dc.getResMessageHolder());

        String message = msg.getMessage(
                refreshTokenArg == null
                        ? "security.token.refresh.required"
                        : "security.token.refresh.invalid"
        );
        assertEquals(temp, message);
    }

}
