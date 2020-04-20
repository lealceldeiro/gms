package com.gms.controller.security.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.LinkPath;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConstant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
public class PermissionControllerTest {

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
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository repository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;

    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    // region vars
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
    private static final String REQ_STRING = ResourcePath.PERMISSION;
    // endregion

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
    public void getRoles() throws Exception {
        BPermission p = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        BRole r = EntityUtil.getSampleRole(random.nextString());
        BRole r2 = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p);
        r2.addPermission(p);
        roleRepository.save(r);
        roleRepository.save(r2);

        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}/" + ResourcePath.ROLE + "s", p.getId())
                .header(authHeader, tokenType + " " + accessToken).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].label")
                                                .description(BRoleMeta.LABEL_INFO),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].description")
                                                .description(BRoleMeta.DESCRIPTION_INFO),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].enabled")
                                                .description(BRoleMeta.ENABLED_INFO),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO)
                                )
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BPermissionMeta.ID_INFO))
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(defaultConstant)
                )));

    }

}
