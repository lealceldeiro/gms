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
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigurationControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EUserRepository userRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private BConfigurationRepository configurationRepository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private static final String reqString = ResourcePath.CONFIGURATION;
    private static final String IS_MULTI_ENTITY_DESC = "Indicates whether the application will handle multiple owned " +
            "entities or not (enterprises, businesses,etc)";
    private static final String IS_USER_REGISTRATION_ALLOWED_DESC = "Indicates whether new users registration will be " +
            "allowed or not via user sign-up";
    private static final String LANGUAGE_DESC = "User language. Language which user is currently using for interacting " +
            "with the app";
    private static final String LAST_ACCESSED_ENT_DESC = "Identifier of the last entity the user had access over";
    private static final String USER_ID_DESC = "User's identifier";
    //endregion

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
    public void getE() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath(ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString())
                                        .description(IS_MULTI_ENTITY_DESC),
                                fieldWithPath(ConfigKey.IS_USER_REGISTRATION_ALLOWED_IN_SERVER.toString())
                                        .description(IS_USER_REGISTRATION_ALLOWED_DESC)
                        )
                ));
    }

    @Test
    public void getByKey() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("key", ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString().toLowerCase())
        ).andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(parameterWithName("key").description(BConfigurationMeta.key))
                ));
    }

    @Test
    public void getByKeyAndUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        configurationRepository.save(new BConfiguration(ConfigKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()));
        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("key", ConfigKey.LANGUAGE.toString().toLowerCase())
                .param("id", u.getId().toString()))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(
                                parameterWithName("key").description(BConfigurationMeta.key),
                                parameterWithName("id").description(BConfigurationMeta.userId)
                        )
                ));
    }

    @Test
    public void getByUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        configurationRepository.save(new BConfiguration(ConfigKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()));
        configurationRepository.save(new BConfiguration(ConfigKey.LAST_ACCESSED_ENTITY.toString(), e.getId().toString(), u.getId()));
        mvc.perform(get(apiPrefix + "/" + reqString + "/{userId}", u.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath(ConfigKey.LANGUAGE.toString()).description(LANGUAGE_DESC),
                                fieldWithPath(ConfigKey.LAST_ACCESSED_ENTITY.toString()).description(LAST_ACCESSED_ENT_DESC)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("userId").description(BConfigurationMeta.userId))
                ));
    }

    @Test
    public void isUserRegistrationAllowed() throws Exception{
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/sign-up")
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void isMultiEntity() throws Exception{
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/multientity")
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception {
        SampleConfigurationPayload payload = new SampleConfigurationPayload(true, false);
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(post(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("is_multi_entity_app_in_server").ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("is_user_registration_allowed_in_server").ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("language").ignored().optional().description(LANGUAGE_DESC),
                                fields.withPath("last_accessed_entity").ignored().optional().description(LAST_ACCESSED_ENT_DESC),
                                fields.withPath("userId").ignored().optional().description(USER_ID_DESC)
                        )
                ));
    }

    @Test
    public void saveForUser() throws Exception {
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        SampleConfigurationPayload payload = new SampleConfigurationPayload(Locale.ENGLISH.toString(), e.getId());
        payload.setUser(u.getId());
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(post(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("is_multi_entity_app_in_server").ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("is_user_registration_allowed_in_server").ignored().optional().description(IS_MULTI_ENTITY_DESC),
                                fields.withPath("language").ignored().optional().description(LANGUAGE_DESC),
                                fields.withPath("last_accessed_entity").ignored().optional().description(LAST_ACCESSED_ENT_DESC),
                                fields.withPath("user").ignored().optional().description(USER_ID_DESC)
                        )
                ));
    }

    @Test
    public void saveForUserKO() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("language", "es");
        payload.put("user", "invalidNumber");

        mvc.perform(post(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnprocessableEntity());
    }
}