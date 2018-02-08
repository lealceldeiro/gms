package com.gms.repository.security.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.permission.BPermission;
import com.gms.service.AppService;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.Resource;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BPermissionRepositoryTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private DefaultConst dc;
    @Autowired private SecurityConst sc;

    @Autowired private AppService appService;
    @Autowired private BPermissionRepository repository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler;

    //region vars
    private String apiPrefix;
    private String pageSizeAttr;
    private int pageSize;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private static final String RANDOM_NAME = "RandomName-";
    private static final String RANDOM_LABEL = "RandomLabel-";
    private static final String reqString = Resource.PERMISSION_PATH;
    //endregion

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() throws Exception {
        org.junit.Assert.assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocResHandler)
                .addFilter(springSecurityFilterChain)
                .alwaysExpect(forwardedUrl(null))
                .build();

        apiPrefix = dc.getApiBasePath();
        pageSizeAttr = dc.getPageSizeHolder();
        pageSize = dc.getPageSize();

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    //C
    @Test
    public void createPermission() throws Exception {
        Map<String, String> newPermission = new HashMap<>();
        newPermission.put("name", RANDOM_NAME + random.nextString());
        newPermission.put("label", RANDOM_LABEL + random.nextString());

        ConstrainedFields fields = new ConstrainedFields(BPermission.class);

        mvc.perform(
                post(apiPrefix + "/" + reqString).contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(newPermission))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("name").description("Name to be used for authenticating"),
                                        fields.withPath("label").description("Label to be shown to the final user")
                                )
                        )
                );

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
                                fieldWithPath("name").description("Name to be used for authenticating"),
                                fieldWithPath("label").description("Label to be shown to the final user"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned permission")
                        )
                )
        );

    }

    @Test
    public void getRolesPermission() throws Exception {
        BPermission p = createPermissionUsingRepository();

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

    //U (partial)
    @Test
    public void partialUpdatePermission() throws Exception {
        BPermission oldPermission = createPermissionUsingRepository();
        Map<String, String> newPermission = new HashMap<>();
        newPermission.put("name", RANDOM_NAME + random.nextString());
        newPermission.put("label", RANDOM_LABEL + random.nextString());

        ConstrainedFields fields = new ConstrainedFields(BPermission.class);

        mvc.perform(
                patch(apiPrefix + "/" + reqString + "/" + oldPermission.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPermission))
        ).andExpect(status().isNoContent())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("name").description("Name to be used for authenticating"),
                                        fields.withPath("label").description("Label to be shown to the final user")
                                )
                        )
                );
    }

    //D
    @Test
    public void deletePermission() throws Exception {
        BPermission p = createPermissionUsingRepository();

        mvc.perform(
                delete(apiPrefix + "/" + reqString + "/" + p.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

    }

    private void setUpPermissions() {
        createPermissionUsingRepository();
    }

    private BPermission createPermissionUsingRepository() {
        return createPermissionUsingRepository(RANDOM_NAME + random.nextString(), RANDOM_LABEL + random.nextString());
    }
    private BPermission createPermissionUsingRepository(String name, String label) {
        return repository.save(new BPermission(name, label));
    }

}