package com.gms.repository.security.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.repository.security.permission.BPermissionRepository;
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
import org.json.JSONArray;
import org.json.JSONException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
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
    @Autowired private BPermissionRepository permissionRepository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private String pageSortAttr;
    private String pageSize;
    private static final String reqString = ResourcePath.ROLE;
    private static final String label = "SampleLabel-";
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

        pageSizeAttr = dc.getPageSizeParam();
        pageSortAttr = dc.getPageSortParam();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void create() throws Exception {
        BRole e = EntityUtil.getSampleRole(random.nextString());
        ConstrainedFields fields = new ConstrainedFields(BRole.class);

        mvc.perform(post(apiPrefix + "/" + reqString)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
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
    public void list() throws Exception {
        String[] sortParams = {"label"};
        repository.save(EntityUtil.getSampleRole(random.nextString()));

        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].label").description(BRoleMeta.label),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].description").description(BRoleMeta.description),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].enabled").description(BRoleMeta.enabled),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("bRole")).ignored(),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("permissions")).description(BRoleMeta.permissionsLink),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                )
                        )
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(dc)
                )));
    }

    @Test
    public void getE() throws Exception {
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("label").description(BRoleMeta.label),
                                fieldWithPath("description").optional().description(BRoleMeta.description),
                                fieldWithPath("enabled").optional().description(BRoleMeta.enabled),
                                fieldWithPath(LinkPath.get("self")).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("bRole")).ignored(),
                                fieldWithPath(LinkPath.get("permissions")).description(BRoleMeta.permissionsLink)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

    @Test
    public void update() throws Exception {
        BRole e = repository.save(new BRole(label + random.nextString()));
        BRole e2 = EntityUtil.getSampleRole(random.nextString());

        mvc.perform(put(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("label").description(BRoleMeta.label),
                                fieldWithPath("description").description(BRoleMeta.description),
                                fieldWithPath("enabled").description(BRoleMeta.enabled),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("bRole")).ignored(),
                                fieldWithPath(LinkPath.get("permissions")).description(BRoleMeta.permissionsLink)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

    @Test
    public void deleteE() throws Exception {
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        mvc.perform(delete(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ));
    }

    @Test
    public void getPermissions() throws Exception {
        BPermission p = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        BPermission p2 = permissionRepository.save(EntityUtil.getSamplePermission(random.nextString()));
        BRole r = EntityUtil.getSampleRole(random.nextString());
        r.addPermission(p, p2);
        repository.save(r);
        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}/" + ResourcePath.PERMISSION + "s", r.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].name").description(BPermissionMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].label").description(BPermissionMeta.label),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[]." + LinkPath.get("bPermission")).ignored(),
                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.PERMISSION + "[]." + LinkPath.get("roles")).ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                )
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.id))
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(dc)
                )));
    }

    @Test
    public void searchLabel() throws Exception {
        searchLabelR(false);
    }

    @Test
    public void searchLabelLike() throws Exception {
        searchLabelR(true);
    }

    private void searchLabelR(boolean isLike) throws Exception {
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.LABEL_LIKE : ResourcePath.LABEL;
        String labelUpper = isLike ? e.getLabel().toUpperCase() : e.getLabel();

        testSearchValueR(url, labelUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String labelRLower = e.getLabel().toLowerCase();
            String labelRLowerShortened = labelRLower.substring(1, labelRLower.length() - 2);
            testSearchValueR(url, labelRLower);
            testSearchValueR(url, labelRLowerShortened);
        }
    }

    private void testSearchValueR(String url, String name, ResultMatcher status) throws Exception {
        final MvcResult mvcResult = mvc.perform(
                get(apiPrefix + "/" + reqString + "/search/" + url)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(ResourcePath.QUERY_VALUE, name)
        ).andExpect(status).andReturn();

        if (status == status().isOk()) {
            checkMvcResult(mvcResult);
        }
    }

    private void testSearchValueR(String url, String name) throws Exception {
        testSearchValueR(url, name, status().isOk());
    }

    private void checkMvcResult(MvcResult mvcResult) throws JSONException, UnsupportedEncodingException {
        final String valueInJSON = GmsSecurityUtil.getValueInJSON(mvcResult.getResponse().getContentAsString(), "_embedded");
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.ROLE));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

}