package com.gms.repository.security.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.service.AppService;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BRoleRepositoryTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 09, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BRoleRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private BRoleRepository repository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private int pageSize;
    private static final String reqString = Resource.ROLE_PATH;
    private static final String label = "SampleLabel-";
    private static final String description = "SampleDescription-";
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

    @Test
    public void createRole() throws Exception {
        BRole e = new BRole(label + random.nextString());
        e.setDescription(description + random.nextString());
        e.setEnabled(true);

        ConstrainedFields fields = new ConstrainedFields(BRole.class);

        mvc.perform(
                post(apiPrefix + "/" + reqString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(e))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("label").description(BRoleMeta.label),
                                        fields.withPath("description").optional().description(BRoleMeta.description),
                                        fields.withPath("permissions").optional().ignored().description(BRoleMeta.permissions),
                                        fields.withPath("enabled").optional().description(BRoleMeta.enabled)
                                )
                        )
                );
    }

    @Test
    public void listRole() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString + "?" + pageSizeAttr + "=" + pageSize)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("_embedded." + reqString).description("Array of roles"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to roles"),
                                fieldWithPath("page").description("Options for paging the roles")
                        )
                )
        );
    }

    @Test
    public void getRole() throws Exception {
        BRole e = repository.save(new BRole(label + random.nextString()));
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("label").description(BRoleMeta.label),
                                fieldWithPath("description").optional().description(BRoleMeta.description),
                                fieldWithPath("enabled").optional().description(BRoleMeta.enabled),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned role")
                        )
                )
        );
    }

    @Test
    public void updateRole() throws Exception {
        BRole e = repository.save(new BRole(label + random.nextString()));
        BRole e2 = new BRole(label + random.nextString());
        e2.setEnabled(true);
        e2.setDescription(description + random.nextString());
        mvc.perform(
                put(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e2))
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("label").description(BRoleMeta.label),
                                fieldWithPath("description").description(BRoleMeta.description),
                                fieldWithPath("enabled").description(BRoleMeta.enabled),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned role")
                        )
                )
        );
    }

    @Test
    public void deleteRole() throws Exception {
        BRole e = repository.save(new BRole(label + random.nextString()));
        mvc.perform(
                delete(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

}