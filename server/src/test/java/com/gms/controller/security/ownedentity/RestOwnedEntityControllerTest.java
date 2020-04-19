package com.gms.controller.security.ownedentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.AppService;
import com.gms.service.configuration.ConfigurationService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.StringUtil;
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
import org.springframework.hateoas.EntityModel;
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
    private static final String REQ_STRING = ResourcePath.OWNED_ENTITY;
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

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void create() throws Exception {
        final boolean multiEntity = configService.isMultiEntity();
        if (!multiEntity) {
            configService.setIsMultiEntity(true);
        }
        EOwnedEntity e = EntityUtil.getSampleEntity(random.nextString());
        ConstrainedFields fields = new ConstrainedFields(EOwnedEntity.class);

        mvc.perform(post(apiPrefix + "/" + REQ_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("name")
                                        .description("Natural name which is used commonly for referring to the entity"),
                                fields.withPath("username")
                                        .description("A unique string representation of the {@LINK #name}. Useful "
                                                + "when there are other entities with the same {@LINK #name}"),
                                fields.withPath("description")
                                        .description("A brief description of the entity")
                        )
                ));
        if (!multiEntity) {
            configService.setIsMultiEntity(false);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void createKO() throws Exception {
        final boolean multiEntity = configService.isMultiEntity();
        if (multiEntity) {
            configService.setIsMultiEntity(false);
        }

        EOwnedEntity e = EntityUtil.getSampleEntity(random.nextString());
        mvc.perform(
                post(apiPrefix + "/" + REQ_STRING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(e))
        ).andExpect(status().isConflict());

        if (multiEntity) {
            configService.setIsMultiEntity(true);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
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
        EntityModel<EOwnedEntity> resource = new EntityModel<>(e);
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

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void handleDataIntegrityViolationException() throws Exception {
        boolean initial = configService.isMultiEntity();
        // allow new owned entities registration
        if (!initial) {
            configService.setIsMultiEntity(true);
        }

        final String r = random.nextString();
        EntityModel<EOwnedEntity> resource = EntityUtil.getSampleEntityResource(r);
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
