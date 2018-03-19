package com.gms.controller.security.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RoleControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private BRoleRepository repository;
    @Autowired private BPermissionRepository permissionRepository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private static final String permissionsPath = ResourcePath.PERMISSION + "s";
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private static final String reqString = ResourcePath.ROLE;

    private List<Long> permissions;
    private BPermission p1;
    private BPermission p2;
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
    public void addPermissions() throws Exception {
        createSamplePermissions();
        BRole r = repository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(r);

        ConstrainedFields fields = new ConstrainedFields(List.class);
        mvc.perform(post(apiPrefix + "/" + reqString + "/{id}/" + permissionsPath, r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to be added to the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

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
        mvc.perform(delete(apiPrefix + "/" + reqString + "/{id}/" + permissionsPath, r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to be removed from the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

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
        mvc.perform(put(apiPrefix + "/" + reqString + "/{id}/" + permissionsPath , r.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(permissions)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        requestFields(
                                fields.withPath("[]").description("Identifiers of permissions which are intended to be set for the role")
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

    private void createSamplePermissions() {
        p1 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        permissions = new LinkedList<>();
        permissions.add(p1.getId());
        permissions.add(p2.getId());
    }
}