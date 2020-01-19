package com.gms.controller.security.role;

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
import com.gms.testutil.validation.ConstrainedFields;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.LinkPath;
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

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
public class RoleControllerTest {

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
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository repository;
    /**
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository permissionRepository;

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
     * Path to endpoints to the {@link BPermission} resource.
     */
    private static final String PERMISSIONS_PATH = ResourcePath.PERMISSION + "s";
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
    private static final String REQ_STRING = ResourcePath.ROLE;

    /**
     * List of permission's ids.
     */
    private List<Long> permissions;
    /**
     * An instance of {@link BPermission}.
     */
    private BPermission p1;
    /**
     * An instance of {@link BPermission}.
     */
    private BPermission p2;
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
    public void addPermissions() throws Exception {
        createSamplePermissions();
        BRole r = repository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r);

        ConstrainedFields fields = new ConstrainedFields(List.class);
        mvc.perform(post(apiPrefix + "/" + REQ_STRING + "/{id}/" + PERMISSIONS_PATH, r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to "
                                        + "be added to the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void removePermissions() throws Exception {
        createSamplePermissions();
        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p1, p2);
        repository.save(r);

        assertNotNull(r);
        assertTrue("The permission " + p1.getName() + " was not added properly", r.getPermissions().contains(p1));
        assertTrue("The permission " + p2.getName() + " was not added properly", r.getPermissions().contains(p2));

        ConstrainedFields fields = new ConstrainedFields(List.class);
        mvc.perform(delete(apiPrefix + "/" + REQ_STRING + "/{id}/" + PERMISSIONS_PATH, r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to "
                                        + "be removed from the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void updatePermissions() throws Exception {
        createSamplePermissions();
        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p1, p2);
        repository.save(r);

        assertNotNull(r);
        assertTrue("The permission " + p1.getName() + " was not added properly", r.getPermissions().contains(p1));
        assertTrue("The permission " + p2.getName() + " was not added properly", r.getPermissions().contains(p2));

        //create de new ones
        createSamplePermissions();

        ConstrainedFields fields = new ConstrainedFields(List.class);
        mvc.perform(put(apiPrefix + "/" + REQ_STRING + "/{id}/" + PERMISSIONS_PATH, r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to "
                                        + "be set for the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPermissionsByRole() throws Exception {
        BPermission permission1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        BPermission permission2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(permission1, permission2);
        repository.save(r);
        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}/" + ResourcePath.PERMISSION + "s", r.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept("application/hal+json")
                .param(dc.getPageSizeParam(), dc.getPageSize()))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].name")
                                                .description(BPermissionMeta.NAME_INFO),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].label")
                                                .description(BPermissionMeta.LABEL_INFO),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO)
                                )
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(dc)
                )));
    }

    private void createSamplePermissions() {
        p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        permissions = new LinkedList<>();
        permissions.add(p1.getId());
        permissions.add(p2.getId());
    }

}
