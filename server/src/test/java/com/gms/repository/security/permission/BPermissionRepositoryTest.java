package com.gms.repository.security.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
import com.gms.domain.security.role.BRole;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.service.AppService;
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.Resource;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
    private int pageSize;
    private static final String reqString = Resource.PERMISSION_PATH;
    private static final String name = "SampleName-";
    private static final String label = "SampleLabel-";
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

        pageSizeAttr = dc.getPageSizeHolder();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    //R
    @Test
    public void listPermissions() throws Exception {
        setUpPermissions();

        mvc.perform(
                get(apiPrefix + "/" + reqString + "?" + pageSizeAttr + "=" + pageSize)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("_embedded." + reqString).description("Array of permissions"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to permissions"),
                                fieldWithPath("page").description("Options for paging the permissions")
                        )
                )
        );
    }

    @Test
    public void getPermission() throws Exception {
        BPermission p = createPermissionUsingRepository();

        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + p.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("name").description(BPermissionMeta.name),
                                fieldWithPath("label").description(BPermissionMeta.label),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned permission")
                        )
                )
        );

    }

    @Test
    public void getRolesPermission() throws Exception {
        BPermission p = createPermissionUsingRepository();
        BRole r = EntityUtil.getSampleRole(random.nextString());
        BRole r2 = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p);
        r2.addPermission(p);
        roleRepository.save(r);
        roleRepository.save(r2);

        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + p.getId() + "/roles")
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(
                        restDocResHandler.document(
                                responseFields(
                                        fieldWithPath("_embedded.role").description("Array of roles in which the permission is included"),
                                        fieldWithPath("_links").description("Available links for requesting other webservices related to the returned permission's roles")
                                )
                        )
                );

    }

    private void setUpPermissions() {
        createPermissionUsingRepository();
    }

    private BPermission createPermissionUsingRepository() {
        return repository.save(EntityUtil.getSamplePermission(random.nextString()));
    }

}