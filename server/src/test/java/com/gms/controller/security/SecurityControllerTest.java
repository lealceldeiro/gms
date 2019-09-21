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
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.gms.testutil.EntityUtil.getSampleUserResource;
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

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

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

    private final GMSRandom random = new GMSRandom(10);

    @Before
    public void setUp() throws Exception{
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();

        refreshToken = GmsSecurityUtil.createSuperAdminRefreshToken(dc, sc, mvc, objectMapper, false);
    }

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
            configService.setUserRegistrationAllowed(false);
        }
    }

    @Test
    public void signUpKO() throws Exception {
        boolean initial = configService.isUserRegistrationAllowed();

        if (initial) {
            configService.setUserRegistrationAllowed(false);
        }
        Resource<EUser> resource = getSampleUserResource(random.nextString());
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isConflict());

        if (initial) {
            configService.setUserRegistrationAllowed(true);
        }
    }

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
                post(apiPrefix + "/" + SecurityConst.ACCESS_TOKEN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

        JSONObject json = new JSONObject(temp);

        int status = json.getInt("status");
        assert status == HttpStatus.UNAUTHORIZED.value();

        temp = json.getString("error");
        assert temp.equals(msg.getMessage("security.unauthorized"));

        temp = json.getString("path");
        assert temp.equals(dc.getApiBasePath() + "/" + SecurityConst.ACCESS_TOKEN_URL);

        temp = json.getString(dc.getResMessageHolder());

        assert temp.equals(msg.getMessage(refreshToken == null ? "security.token.refresh.required" : "security.token.refresh.invalid"));
    }
}