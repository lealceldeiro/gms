package com.gms.controller.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.BAuthorizationMeta;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.ownedentity.EOwnedEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

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
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository permissionRepository;
    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

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
     * Base path for the API.
     */
    private String apiPrefix;

    /**
     * An instance of {@link EOwnedEntity}.
     */
    private EOwnedEntity entity;
    /**
     * Instance of {@link EUser}.
     */
    private EUser user;
    /**
     * Role identifiers list.
     */
    private List<Long> roleIds;

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
    public void addRoles() throws Exception {
        initializeVars();

        ConstrainedFields fields = new ConstrainedFields(ArrayList.class);

        String url = apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}";
        mvc.perform(post(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("List of " + BAuthorizationMeta.ROLE_ID_ADD_INFO)
                        )
                ))
                .andDo(restDocResHandler.document(
                        responseFields(fieldWithPath("[]")
                                .description("List of roles added to the user over the entity"))
                ))
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.ID_INFO),
                        parameterWithName("entityId").description(EOwnedEntityMeta.ID_INFO)
                )));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void addRolesToUser404() throws Exception {
        //region var-initialisation
        final long invalidId = -999999999L;
        initializeVars();
        //endregion

        String url = apiPrefix + "/" + ResourcePath.USER + "/" + ResourcePath.ROLE + "s/{userId}/{entityId}";
        //entity not found
        mvc.perform(post(url, user.getId(), invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());

        //user not found
        mvc.perform(post(url, invalidId, entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        roleIds.clear();
        roleIds.add(invalidId);

        mvc.perform(post(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void removeRoles() throws Exception {
        initializeVars();

        ConstrainedFields fields = new ConstrainedFields(ArrayList.class);

        String url = apiPrefix + "/" + ResourcePath.USER + "/" + ResourcePath.ROLE + "s/{userId}/{entityId}";
        mvc.perform(delete(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(requestFields(
                        fields.withPath("[]").description("List of " + BAuthorizationMeta.ROLE_ID_REMOVE_INFO)
                )))
                .andDo(restDocResHandler.document(
                        responseFields(fieldWithPath("[]")
                                .description("List of roles removed to the user from the entity"))
                ))
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.ID_INFO),
                        parameterWithName("entityId").description(EOwnedEntityMeta.ID_INFO)
                )));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void removeRolesFromUser404() throws Exception {
        //region var-initialisation
        final long invalidId = -999999999L;
        initializeVars();
        //endregion

        String url = apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}";
        //entity not found
        mvc.perform(delete(url, user.getId(), invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());

        //user not found
        mvc.perform(delete(url, invalidId, entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        roleIds.clear();
        roleIds.add(invalidId);

        mvc.perform(delete(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(roleIds))
        ).andExpect(status().isNotFound());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRoles() throws Exception {
        initializeVars();
        assignRolesForUserOverEntity();

        mvc.perform(get(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.ID_INFO)
                )));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRolesByEntity() throws Exception {
        initializeVars();
        assignRolesForUserOverEntity();

        mvc.perform(get(
                apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}",
                user.getId(),
                entity.getId()
        )
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.ID_INFO),
                        parameterWithName("entityId").description(EUserMeta.ID_INFO)
                )));
    }

    private void initializeVars() {
        String r = random.nextString();
        BPermission p = permissionRepository.save(EntityUtil.getSamplePermission(r));
        Optional<BPermission> op = permissionRepository.findById(p.getId());
        assertTrue("Test permission could not be saved", op.isPresent());

        BRole ro = roleRepository.save(EntityUtil.getSampleRole(r));
        Optional<BRole> oro = roleRepository.findById(ro.getId());
        assertTrue("Test role could not be saved", oro.isPresent());

        BRole ro2 = roleRepository.save(EntityUtil.getSampleRole(random.nextString())); // get another random string
        Optional<BRole> oro2 = roleRepository.findById(ro2.getId());
        assertTrue("Test role 2 could not be saved", oro2.isPresent());

        EOwnedEntity e = entityRepository.save(EntityUtil.getSampleEntity(r));
        Optional<EOwnedEntity> oe = entityRepository.findById(e.getId());
        assertTrue("Test entity could not be saved", oe.isPresent());

        EUser u = userRepository.save(EntityUtil.getSampleUser(r));
        Optional<EUser> ou = userRepository.findById(u.getId());
        assertTrue("Test user could not be saved", ou.isPresent());

        List<Long> rolesId = new ArrayList<>(1);
        rolesId.add(ro.getId());
        rolesId.add(ro2.getId());

        entity = e;
        user = u;
        roleIds = rolesId;
    }

    private void assignRolesForUserOverEntity() throws Exception {
        final MvcResult mvcResult =
                mvc.perform(post(
                        apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}",
                        user.getId(),
                        entity.getId()
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(authHeader, tokenType + " " + accessToken)
                                .content(objectMapper.writeValueAsString(roleIds))
                ).andReturn();

        assertEquals("The relationship among user, entity and role(s) could not be saved",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

}
