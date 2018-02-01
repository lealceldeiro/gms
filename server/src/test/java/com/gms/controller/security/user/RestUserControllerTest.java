package com.gms.controller.security.user;

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
import com.gms.util.validation.ConstrainedFields;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RestUserControllerTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Jan 30, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RestUserControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private SecurityConst sc;
    @Autowired
    private DefaultConst dc;

    @Autowired
    private AppService appService;
    @Autowired
    private ConfigurationService configService;
    @Autowired
    private MessageResolver msg;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocResHandler)
                .addFilter(springSecurityFilterChain)
                .alwaysExpect(forwardedUrl(null))
                .build();
        apiPrefix = dc.getApiBasePath();

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }


    @Test
    public void register() throws Exception {
        EUser u = new EUser("uu" + random.nextString(), "test" + random.nextString() + "@test.com",
                "un" + random.nextString(), "ul" + random.nextString(), "pass");
        u.setEnabled(true);
        Resource<EUser> resource = new Resource<>(u);
        ReflectionTestUtils.setField(resource, "links", null);

        ConstrainedFields fields = new ConstrainedFields(EUser.class);

        final MvcResult mvcResult = mvc.perform(post(apiPrefix + "/user").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("username").description("User's username"),
                                        fields.withPath("email").description("User's email"),
                                        fields.withPath("name").description("User's name"),
                                        fields.withPath("lastName").description("User's last name"),
                                        fields.withPath("password").description("User's password"),
                                        fields.withPath("enabled").optional().description("Whether the user will be enabled or not"),
                                        fields.withPath("authorities").optional().ignored(),
                                        fields.withPath("emailVerified").optional().ignored(),
                                        fields.withPath("accountNonExpired").optional().ignored(),
                                        fields.withPath("accountNonLocked").optional().ignored(),
                                        fields.withPath("credentialsNonExpired").optional().ignored(),
                                        fields.withPath("links").optional().ignored()
                                )
                        )
                )
                .andDo(
                        restDocResHandler.document(
                                responseFields(
                                        fieldWithPath("username").description("Just created user's username"),
                                        fieldWithPath("email").description("Just created user's email"),
                                        fieldWithPath("name").description("Just created user's name"),
                                        fieldWithPath("lastName").description("Just created user's last name"),
                                        fieldWithPath("password").description("Just created user's password"),
                                        fieldWithPath("enabled").description("Whether the just created user is enabled or not"),
                                        fieldWithPath("emailVerified")
                                                .description("Indicates whether the user has verified his/her email or not"),
                                        fields.withPath("authorities").optional().ignored(),
                                        fields.withPath("accountNonExpired").description("Whether the account has not expired yet or it already expired"),
                                        fields.withPath("accountNonLocked").description("Whether the account is not locked or it has been locked"),
                                        fields.withPath("credentialsNonExpired").description("Whether the credentials has not expired yet or they already expired"),
                                        fieldWithPath("_links")
                                                .description("Available links for requesting other webservices related to user")
                                )
                        )
                ).andReturn();
        u = objectMapper.reader().forType(EUser.class).readValue(mvcResult.getResponse().getContentAsString());

        assertNotNull("User object is null", u);
        assertTrue("When registering a user, his/her email must be verified by default", u.isEmailVerified());
    }

    @Test
    public void handleTransactionSystemException() throws Exception {
        String fd = random.nextString();
        EUser u = new EUser(null, "rucTest" + fd + "@test.com", fd, fd, fd);
        Resource<EUser> resource = new Resource<>(u);
        mvc.perform(
                post(apiPrefix + "/user").contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        String fd = random.nextString();
        String email = "rucTest" + fd + "@test.com";
        EUser u = new EUser(fd, email, fd, fd, fd);
        Resource<EUser> resource = new Resource<>(u);
        mvc.perform(
                post(apiPrefix + "/user").contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated());

        u = new EUser(u.getUsername() + fd, email, u.getName() + fd, u.getLastName() + fd, u.getPassword() + fd);
        resource = new Resource<>(u);
        mvc.perform(
                post(apiPrefix + sc.getSignUpUrl()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());
    }
}