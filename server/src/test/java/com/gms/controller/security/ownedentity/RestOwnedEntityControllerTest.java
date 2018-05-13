package com.gms.controller.security.ownedentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.*;
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
public class RestOwnedEntityControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private ConfigurationService configService;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private static final String reqString = ResourcePath.OWNED_ENTITY;
    //endregion

    private final GMSRandom random = new GMSRandom();

    @SuppressWarnings("Duplicates")
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
        final boolean multiEntity = configService.isMultiEntity();
        if (!multiEntity) {
            configService.setIsMultiEntity(true);
        }
        EOwnedEntity e = EntityUtil.getSampleEntity(random.nextString());
        ConstrainedFields fields = new ConstrainedFields(EOwnedEntity.class);

        mvc.perform(post(apiPrefix + "/" + reqString)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("name").description("Natural name which is used commonly for referring to the entity"),
                                fields.withPath("username").description("A unique string representation of the {@LINK #name}. Useful when there are other entities with the same {@LINK #name}"),
                                fields.withPath("description").description("A brief description of the entity")
                        )
                ));
        if (!multiEntity) {
            configService.setIsMultiEntity(false);
        }
    }

    @Test
    public void createKO() throws Exception {
        final boolean multiEntity = configService.isMultiEntity();
        if (multiEntity) {
            configService.setIsMultiEntity(false);
        }

        EOwnedEntity e = EntityUtil.getSampleEntity(random.nextString());
        mvc.perform(
                post(apiPrefix + "/" + reqString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(e))
        ).andExpect(status().isConflict());

        if (multiEntity) {
            configService.setIsMultiEntity(true);
        }
    }

    @Test
    public void handleTransactionSystemException() throws Exception {
        boolean initial = configService.isMultiEntity();
        // allow new owned entities registration
        if (!initial) {
            configService.setIsMultiEntity(true);
        }

        final String r = random.nextString();
        EOwnedEntity e = new EOwnedEntity(null, StringUtil.EXAMPLE_USERNAME + r,
                StringUtil.EXAMPLE_DESCRIPTION + r);
        Resource<EOwnedEntity> resource = new Resource<>(e);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.OWNED_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(sc.getATokenHeader(), tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());

        // restart initial config
        if (!initial) {
            configService.setUserRegistrationAllowed(false);
        }
    }

    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        boolean initial = configService.isMultiEntity();
        // allow new owned entities registration
        if (!initial) {
            configService.setIsMultiEntity(true);
        }

        final String r = random.nextString();
        Resource<EOwnedEntity> resource = EntityUtil.getSampleEntityResource(r);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.OWNED_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(sc.getATokenHeader(), tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isCreated());

        resource = EntityUtil.getSampleEntityResource(r);
        mvc.perform(
                post(apiPrefix + "/" + ResourcePath.OWNED_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(sc.getATokenHeader(), tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(resource))
        ).andExpect(status().isUnprocessableEntity());

        // restart initial config
        if (!initial) {
            configService.setIsMultiEntity(false);
        }
    }
}