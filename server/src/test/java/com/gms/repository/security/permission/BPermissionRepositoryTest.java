package com.gms.repository.security.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.service.AppService;
import com.gms.util.*;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BPermissionRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private DefaultConst dc;
    @Autowired private SecurityConst sc;

    @Autowired private AppService appService;
    @Autowired private BPermissionRepository repository;
    @Autowired private BRoleRepository roleRepository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private String pageSize;
    private static final String reqString = ResourcePath.PERMISSION;
    //endregion

    private final GMSRandom random = new GMSRandom();

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc =  GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        pageSizeAttr = dc.getPageSizeHolder();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void list() throws Exception {
        setUpPermissions();

        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingfields(
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].name").description(BPermissionMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].label").description(BPermissionMeta.label),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("bPermission")).ignored(),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("roles")).ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                )
                        )
                ));
    }

    @Test
    public void getE() throws Exception {
        BPermission p = createPermissionUsingRepository();

        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}", p.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("name").description(BPermissionMeta.name),
                                fieldWithPath("label").description(BPermissionMeta.label),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("bPermission")).ignored(),
                                fieldWithPath(LinkPath.get("roles")).description(BPermissionMeta.rolesLink)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BPermissionMeta.id))
                ));

    }

    @Test
    public void getRoles() throws Exception {
        BPermission p = createPermissionUsingRepository();
        BRole r = EntityUtil.getSampleRole(random.nextString());
        BRole r2 = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p);
        r2.addPermission(p);
        roleRepository.save(r);
        roleRepository.save(r2);

        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}/roles", p.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].label").description(BRoleMeta.label),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[].id").description(GmsEntityMeta.id),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[].description").description(BRoleMeta.description),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[].enabled").description(BRoleMeta.enabled),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[]." + LinkPath.get("bRole")).ignored(),
                                fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE  + "[]." + LinkPath.get("permissions")).description(BRoleMeta.permissionsLink),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BPermissionMeta.id))
                ));

    }

    private void setUpPermissions() {
        createPermissionUsingRepository();
    }

    private BPermission createPermissionUsingRepository() {
        return repository.save(EntityUtil.getSamplePermission(random.nextString()));
    }

}