package com.gms.controller.security.user;

import static com.gms.testutil.StringUtil.EXAMPLE_EMAIL;
import static com.gms.testutil.StringUtil.EXAMPLE_LAST_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_PASSWORD;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RestUserControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void create() throws Exception {
        final String r = random.nextString();
        EUser u = EntityUtil.getSampleUser(r);
        Resource<EUser> resource = new Resource<>(u);
        ReflectionTestUtils.setField(resource, "links", null);

        ConstrainedFields fields = new ConstrainedFields(EUser.class);

        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER).contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated())
                .andDo(restDocResHandler.document(
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
                ));
    }

    @Test
    public void handleTransactionSystemException() throws Exception {
        String r = random.nextString();
        EUser u = new EUser(null ,"a" + r + EXAMPLE_EMAIL, EXAMPLE_NAME + r,
                EXAMPLE_LAST_NAME, EXAMPLE_PASSWORD);
        Resource<EUser> resource = new Resource<>(u);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.USER).contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        String r = random.nextString();
        Resource<EUser> resource = EntityUtil.getSampleUserResource(r);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.USER).contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated());
        resource = EntityUtil.getSampleUserResource(r);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.USER).contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());
    }
}