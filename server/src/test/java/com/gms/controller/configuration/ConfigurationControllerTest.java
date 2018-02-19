package com.gms.controller.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.configuration.BConfiguration;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.user.EUser;
import com.gms.repository.configuration.BConfigurationRepository;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
import com.gms.util.configuration.ConfigKey;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
import com.gms.util.validation.ConstrainedFields;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ConfigurationControllerTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 19, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ConfigurationControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private SecurityConst sc;
    @Autowired
    private DefaultConst dc;

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

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

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
    public void getConfig() throws Exception {
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
    public void getConfigByKey() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("key", ConfigKey.IS_MULTI_ENTITY_APP_IN_SERVER.toString().toLowerCase())
        ).andExpect(status().isOk());
    }

    @Test
    public void getConfigByKeyAndUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        configurationRepository.save(new BConfiguration(ConfigKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()));
        mvc.perform(
                get(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("key", ConfigKey.LANGUAGE.toString().toLowerCase())
                        .param("id", u.getId().toString())
        ).andExpect(status().isOk());
    }

    @Test
    public void getConfigByUser() throws Exception {
        EUser u = userRepository.save(EntityUtil.getSampleUser(random.nextString()));
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        configurationRepository.save(new BConfiguration(ConfigKey.LANGUAGE.toString(), Locale.ENGLISH.toString(), u.getId()));
        configurationRepository.save(new BConfiguration(ConfigKey.LAST_ACCESSED_ENTITY.toString(), e.getId().toString(), u.getId()));
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + u.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(restDocResHandler.document(responseFields(
                        fieldWithPath(ConfigKey.LANGUAGE.toString()).description(LANGUAGE_DESC),
                        fieldWithPath(ConfigKey.LAST_ACCESSED_ENTITY.toString()).description(LAST_ACCESSED_ENT_DESC)
                )));
    }

    @Test
    public void saveConfig() throws Exception {
        SampleConfigurationPayload payload = new SampleConfigurationPayload(true, false);
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(
                post(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNoContent())
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
    public void saveConfigForUser() throws Exception {
        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(random.nextString()));
        SampleConfigurationPayload payload = new SampleConfigurationPayload(Locale.ENGLISH.toString(), e.getId());
        ConstrainedFields fields = new ConstrainedFields(SampleConfigurationPayload.class);

        mvc.perform(
                post(apiPrefix + "/" + reqString)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNoContent())
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
}