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
import com.gms.util.*;
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
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private BPermissionRepository permissionRepository;
    @Autowired private BRoleRepository roleRepository;
    @Autowired private EOwnedEntityRepository entityRepository;
    @Autowired private EUserRepository userRepository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private EOwnedEntity entity;
    private EUser user;
    private ArrayList<Long> rIds;

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
    public void addRoles() throws Exception {
        initializeVars();

        ConstrainedFields fields = new ConstrainedFields(ArrayList.class);

        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("List of " + BAuthorizationMeta.roleIdAdd)
                        )
                ))
                .andDo(restDocResHandler.document(
                        responseFields(fieldWithPath("[]").description("List of roles added to the user over the entity"))
                ))
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.id),
                        parameterWithName("entityId").description(EOwnedEntityMeta.id)
                )));
    }

    @Test
    public void addRolesToUser404() throws Exception {
        //region var-initialisation
        final long INVALID_ID = -999999999L;
        initializeVars();
        //endregion

        //entity not found
        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());

        //user not found
        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", INVALID_ID, entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        rIds.clear();
        rIds.add(INVALID_ID);

        mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void removeRoles() throws Exception {
        initializeVars();

        ConstrainedFields fields = new ConstrainedFields(ArrayList.class);

        mvc.perform(delete(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(requestFields(
                        fields.withPath("[]").description("List of " + BAuthorizationMeta.roleIdRemove)
                )))
                .andDo(restDocResHandler.document(
                        responseFields(fieldWithPath("[]").description("List of roles removed to the user from the entity"))
                ))
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.id),
                        parameterWithName("entityId").description(EOwnedEntityMeta.id)
                )));
    }

    @Test
    public void removeRolesFromUser404() throws Exception {
        //region var-initialisation
        final long INVALID_ID = -999999999L;
        initializeVars();
        //endregion

        //entity not found
        mvc.perform(delete(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());

        //user not found
        mvc.perform(delete(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", INVALID_ID, entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        rIds.clear();
        rIds.add(INVALID_ID);

        mvc.perform(delete(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getRoles() throws Exception {
        initializeVars();
        assignRolesForUserOverEntity();

        mvc.perform(get(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(pathParameters(
                        parameterWithName("userId").description(EUserMeta.id)
                )));
    }

    @Test
    public void getRolesByEntity() throws Exception {
        initializeVars();
        assignRolesForUserOverEntity();

        mvc.perform(get(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk())
        .andDo(restDocResHandler.document(pathParameters(
                parameterWithName("userId").description(EUserMeta.id),
                parameterWithName("entityId").description(EUserMeta.id)
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

        ArrayList<Long> rolesId = new ArrayList<>(1);
        rolesId.add(ro.getId());
        rolesId.add(ro2.getId());

        entity = e;
        user = u;
        rIds = rolesId;
    }

    private void assignRolesForUserOverEntity() throws Exception {
        final MvcResult mvcResult = mvc.perform(post(apiPrefix + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}", user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(rIds))
        ).andReturn();

        assertTrue("The relationship among user, entity and role(s) could not be saved",
                mvcResult.getResponse().getStatus() == HttpStatus.OK.value());
    }

}