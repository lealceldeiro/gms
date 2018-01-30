package com.gms.controller.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.request.mapping.user.RolesForUserOverEntity;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired private ObjectMapper objectMapper;

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
    private RestDocumentationResultHandler restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));

    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String apiPrefix;

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilter(springSecurityFilterChain)
                .alwaysExpect(forwardedUrl(null))
                .build();
        apiPrefix = dc.getApiBasePath();

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void addRolesToUserOK() throws Exception {
        //region var-initialisation
        BPermission p = new BPermission("pn" + random.nextString(), "pl" + random.nextString());
        p = permissionRepository.save(p);
        p = permissionRepository.findOne(p.getId());
        assertNotNull("Test permission could not be saved", p);

        BRole r = new BRole("rl" + random.nextString());
        r = roleRepository.save(r);
        r = roleRepository.findOne(r.getId());
        assertNotNull("Test role could not be saved", r);

        BRole r2 = new BRole("r2l" + random.nextString());
        r2 = roleRepository.save(r2);
        r2 = roleRepository.findOne(r2.getId());
        assertNotNull("Test role 2 could not be saved", r2);

        EOwnedEntity e = new EOwnedEntity("en" + random.nextString(), "eu" + random.nextString(), "Test description");
        e = entityRepository.save(e);
        e = entityRepository.findOne(e.getId());
        assertNotNull("Test entity could not be saved", e);

        EUser u = new EUser("uu" + random.nextString(), "test" + random.nextString() + "@test.com", "name" + random.nextString(),
                "lastname" + random.nextString(), "pass");
        u = userRepository.save(u);
        u = userRepository.findOne(u.getId());
        assertNotNull("Test user could not be saved", u);

        ArrayList<Long> rolesId = new ArrayList<>(1);
        rolesId.add(r.getId());
        rolesId.add(r2.getId());
        //endregion

        RolesForUserOverEntity payload = new RolesForUserOverEntity();
        payload.setEntityId(e.getId());
        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);

        ConstrainedFields fields = new ConstrainedFields(RolesForUserOverEntity.class);

        mvc.perform(post(apiPrefix + "/user/roles/add").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isOk())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("userId").description("User's identifier (id)"),
                                        fields.withPath("entityId").description("Entity's identifier (id) over which the user will have the roles"),
                                        fields.withPath("rolesId").description("List of identifiers (id) of every role the user will have over the entity")
                                )
                        )
                )
                .andDo(
                        restDocResHandler.document(
                                responseFields(fieldWithPath("[]").description("List of roles added to the user over the entity"))
                        )
                );
    }

    @Test
    public void addRolesToUser404() throws Exception {
        //region var-initialisation
        final long INVALID_ID = -999999999L;
        BPermission p = new BPermission("pn" + random.nextString(), "pl" + random.nextString());
        p = permissionRepository.save(p);
        p = permissionRepository.findOne(p.getId());
        assertNotNull("Test permission could not be saved", p);

        BRole r = new BRole("rl" + random.nextString());
        r = roleRepository.save(r);
        r = roleRepository.findOne(r.getId());
        assertNotNull("Test role could not be saved", r);

        BRole r2 = new BRole("r2l" + random.nextString());
        r2 = roleRepository.save(r2);
        r2 = roleRepository.findOne(r2.getId());
        assertNotNull("Test role 2 could not be saved", r2);

        EOwnedEntity e = new EOwnedEntity("en" + random.nextString(), "eu" + random.nextString(), "Test description");
        e = entityRepository.save(e);
        e = entityRepository.findOne(e.getId());
        assertNotNull("Test entity could not be saved", e);

        EUser u = new EUser("uu" + random.nextString(), "test" + random.nextString() + "@test.com", "name" + random.nextString(),
                "lastname" + random.nextString(), "pass");
        u = userRepository.save(u);
        u = userRepository.findOne(u.getId());
        assertNotNull("Test user could not be saved", u);

        ArrayList<Long> rolesId = new ArrayList<>(1);
        rolesId.add(r.getId());
        rolesId.add(r2.getId());
        //endregion

        RolesForUserOverEntity payload = new RolesForUserOverEntity();
        payload.setEntityId(INVALID_ID);
        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);

        //entity not found
        mvc.perform(post(apiPrefix + "/user/roles/add").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());

        //user not found
        payload.setEntityId(e.getId());
        payload.setUserId(INVALID_ID);
        mvc.perform(post(apiPrefix + "/user/roles/add").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        rolesId.clear();
        rolesId.add(INVALID_ID);

        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);
        mvc.perform(post(apiPrefix + "/user/roles/add").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void removeRolesFromUserUserOK() throws Exception {
        //region var-initialisation
        BPermission p = new BPermission("pn" + random.nextString(), "pl" + random.nextString());
        p = permissionRepository.save(p);
        p = permissionRepository.findOne(p.getId());
        assertNotNull("Test permission could not be saved", p);

        BRole r = new BRole("rl" + random.nextString());
        r = roleRepository.save(r);
        r = roleRepository.findOne(r.getId());
        assertNotNull("Test role could not be saved", r);

        BRole r2 = new BRole("r2l" + random.nextString());
        r2 = roleRepository.save(r2);
        r2 = roleRepository.findOne(r2.getId());
        assertNotNull("Test role 2 could not be saved", r2);

        EOwnedEntity e = new EOwnedEntity("en" + random.nextString(), "eu" + random.nextString(), "Test description");
        e = entityRepository.save(e);
        e = entityRepository.findOne(e.getId());
        assertNotNull("Test entity could not be saved", e);

        EUser u = new EUser("uu" + random.nextString(), "test" + random.nextString() + "@test.com", "name" + random.nextString(),
                "lastname" + random.nextString(), "pass");
        u = userRepository.save(u);
        u = userRepository.findOne(u.getId());
        assertNotNull("Test user could not be saved", u);

        ArrayList<Long> rolesId = new ArrayList<>(1);
        rolesId.add(r.getId());
        rolesId.add(r2.getId());
        //endregion

        RolesForUserOverEntity payload = new RolesForUserOverEntity();
        payload.setEntityId(e.getId());
        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);

        final MvcResult mvcResult = mvc.perform(post(apiPrefix + "/user/roles/add").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andReturn();

        assertTrue("Tes relationship among user, entity and role(s) could not be saved",
                mvcResult.getResponse().getStatus() == HttpStatus.OK.value());

        ConstrainedFields fields = new ConstrainedFields(RolesForUserOverEntity.class);

        mvc.perform(delete(apiPrefix + "/user/roles/remove").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isOk())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("userId").description("User's identifier (id)"),
                                        fields.withPath("entityId").description("Entity's identifier (id) over which the user will have the roles removed from"),
                                        fields.withPath("rolesId").description("List of identifiers (id) of every role the user will have removed from the entity")
                                )
                        )
                )
                .andDo(
                        restDocResHandler.document(
                                responseFields(fieldWithPath("[]").description("List of roles removed to the user from the entity"))
                        )
                );
    }

    @Test
    public void removeRolesFromUser404() throws Exception {
        //region var-initialisation
        final long INVALID_ID = -999999999L;
        BPermission p = new BPermission("pn" + random.nextString(), "pl" + random.nextString());
        p = permissionRepository.save(p);
        p = permissionRepository.findOne(p.getId());
        assertNotNull("Test permission could not be saved", p);

        BRole r = new BRole("rl" + random.nextString());
        r = roleRepository.save(r);
        r = roleRepository.findOne(r.getId());
        assertNotNull("Test role could not be saved", r);

        BRole r2 = new BRole("r2l" + random.nextString());
        r2 = roleRepository.save(r2);
        r2 = roleRepository.findOne(r2.getId());
        assertNotNull("Test role 2 could not be saved", r2);

        EOwnedEntity e = new EOwnedEntity("en" + random.nextString(), "eu" + random.nextString(), "Test description");
        e = entityRepository.save(e);
        e = entityRepository.findOne(e.getId());
        assertNotNull("Test entity could not be saved", e);

        EUser u = new EUser("uu" + random.nextString(), "test" + random.nextString() + "@test.com", "name" + random.nextString(),
                "lastname" + random.nextString(), "pass");
        u = userRepository.save(u);
        u = userRepository.findOne(u.getId());
        assertNotNull("Test user could not be saved", u);

        ArrayList<Long> rolesId = new ArrayList<>(1);
        rolesId.add(r.getId());
        rolesId.add(r2.getId());
        //endregion

        RolesForUserOverEntity payload = new RolesForUserOverEntity();
        payload.setEntityId(INVALID_ID);
        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);

        //entity not found
        mvc.perform(delete(apiPrefix + "/user/roles/remove").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());

        //user not found
        payload.setEntityId(e.getId());
        payload.setUserId(INVALID_ID);
        mvc.perform(delete(apiPrefix + "/user/roles/remove").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());

        //none of the roles was found
        rolesId.clear();
        rolesId.add(INVALID_ID);

        payload.setUserId(u.getId());
        payload.setRolesId(rolesId);
        mvc.perform(delete(apiPrefix + "/user/roles/remove").contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isNotFound());
    }

}