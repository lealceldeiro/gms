package com.gms.controller.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.configuration.BConfiguration;
import com.gms.domain.configuration.BConfigurationMeta;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.user.EUser;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.GMSRandom;
import com.gms.util.configuration.BusinessConfigurationKey;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConstant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class ConfigurationControllerTest {

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
     * Instance of {@link BConfigurationRepository}.
     */
    @Autowired
    private BConfigurationRepository configurationRepository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    /**
     * Base path for the API.
     */
    private String apiPrefix;
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
     * Base path for requests in this test suite (after the base API path).
     */
    private static final String REQ_STRING = ResourcePath.CONFIGURATION;
    /**
     * API generated documentation information.
     */
    private static final String IS_MULTI_ENTITY_DESC = "Indicates whether the application will handle multiple owned "
            + "entities or not (enterprises, businesses,etc)";
    /**
     * API generated documentation information.
     */
    private static final String IS_USER_REGISTRATION_ALLOWED_DESC = "Indicates whether new users registration will "
            + "be allowed or not via user sign-up";
    /**
     * API generated documentation information.
     */
    private static final String LANGUAGE_DESC = "User language. Language which user is currently using for "
            + "interacting with the app";
    /**
     * API generated documentation information.
     */
    private static final String LAST_ACCESSED_ENT_DESC = "Identifier of the last entity the user had access over";
    /**
     * API generated documentation information.
     */
    private static final String USER_ID_DESC = "User's identifier";
    //endregion

    /**
     * An alphanumeric random generator.
     */
    private final GMSRandom random = new GMSRandom();

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        apiPrefix = defaultConstant.getApiBasePath();
        authHeader = securityConstant.getATokenHeader();
        tokenType = securityConstant.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                securityConstant,
                                                                mvc,
                                                                objectMapper,
                                                                false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getE() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + REQ_STRING)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath(BusinessConfigurationKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString())
                                        .description(IS_MULTI_ENTITY_DESC),
                                fieldWithPath(
                                        BusinessConfigurationKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString()
                                )
                                        .description(IS_USER_REGISTRATION_ALLOWED_DESC)
                        )
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getByKey() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + REQ_STRING)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("key",
                               BusinessConfigurationKey.IS_MULTI_ENTITY_APP_IN_SERVER
                                       .toString()
                                       .toLowerCase(Locale.ENGLISH)
                        )
        ).andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(parameterWithName("key").description(BConfigurationMeta.KEY_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getByKeyAndUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        configurationRepository.save(new BConfiguration(
                BusinessConfigurationKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()
        ));
        mvc.perform(get(apiPrefix + "/" + REQ_STRING)
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("key", BusinessConfigurationKey.LANGUAGE.toString().toLowerCase(Locale.ENGLISH))
                            .param("id", String.valueOf(u.getId())))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(
                                parameterWithName("key").description(BConfigurationMeta.KEY_INFO),
                                parameterWithName("id").description(BConfigurationMeta.USER_ID_INFO)
                        )
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getByUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));

        configurationRepository.save(new BConfiguration(
                BusinessConfigurationKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()
        ));
        configurationRepository.save(new BConfiguration(
                BusinessConfigurationKey.LAST_ACCESSED_ENTITY.toString(), String.valueOf(e.getId()), u.getId()
        ));

        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{userId}", u.getId())
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath(BusinessConfigurationKey.LANGUAGE.toString()).description(LANGUAGE_DESC),
                                fieldWithPath(BusinessConfigurationKey.LAST_ACCESSED_ENTITY.toString())
                                        .description(LAST_ACCESSED_ENT_DESC)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("userId").description(BConfigurationMeta.USER_ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isUserRegistrationAllowed() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/sign-up")
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isMultiEntity() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/multientity")
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void save() throws Exception {
        SampleConfigurationPayload payload = new SampleConfigurationPayload(true, false);
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(post(apiPrefix + "/" + REQ_STRING)
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("is_multi_entity_app_in_server")
                                        .ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("is_user_registration_allowed_in_server")
                                        .ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("language")
                                        .ignored().optional().description(LANGUAGE_DESC),
                                fields.withPath("last_accessed_entity")
                                        .ignored().optional().description(LAST_ACCESSED_ENT_DESC),
                                fields.withPath("userId")
                                        .ignored().optional().description(USER_ID_DESC)
                        )
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveForUser() throws Exception {
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        SampleConfigurationPayload payload = new SampleConfigurationPayload(Locale.ENGLISH.toString(), e.getId());
        payload.setUser(u.getId());
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(post(apiPrefix + "/" + REQ_STRING)
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("is_multi_entity_app_in_server")
                                        .ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("is_user_registration_allowed_in_server")
                                        .ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("language")
                                        .ignored().optional().description(LANGUAGE_DESC),
                                fields.withPath("last_accessed_entity")
                                        .ignored().optional().description(LAST_ACCESSED_ENT_DESC),
                                fields.withPath("user")
                                        .ignored().optional().description(USER_ID_DESC)
                        )
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveForUserKO() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("language", "es");
        payload.put("user", "invalidNumber");

        mvc.perform(post(apiPrefix + "/" + REQ_STRING)
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnprocessableEntity());
    }

}
