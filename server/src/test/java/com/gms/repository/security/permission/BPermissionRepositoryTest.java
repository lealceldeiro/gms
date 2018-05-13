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
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
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
    private MockMvc mvcNonDocumenter;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private String pageSortAttr;
    private String pageSize;
    private static final String reqString = ResourcePath.PERMISSION;
    //endregion

    private final GMSRandom random = new GMSRandom();

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc =  GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);
        mvcNonDocumenter =  GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        pageSizeAttr = dc.getPageSizeParam();
        pageSortAttr = dc.getPageSortParam();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void list() throws Exception {
        setUpPermissions();


        String[] sortParams = {"name,desc", "label"};
        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].name").description(BPermissionMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].label").description(BPermissionMeta.label),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("bPermission")).ignored(),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("roles")).ignored(),
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

    @Test
    public void searchMulti() throws Exception {
        searchMulti(false);
    }

    @Test
    public void searchMultiLike() throws Exception {
        searchMulti(true);
    }

    @Test
    public void searchName() throws Exception {
        searchName(false);
    }

    @Test
    public void searchNameLike() throws Exception {
        searchName(true);
    }

    @Test
    public void searchLabel() throws Exception {
        searchLabel(false);
    }

    @Test
    public void searchLabelLike() throws Exception {
        searchLabel(true);
    }

    private void setUpPermissions() {
        createPermissionUsingRepository();
    }

    private BPermission createPermissionUsingRepository() {
        return repository.save(EntityUtil.getSamplePermission(random.nextString()));
    }

    private void searchName(boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameUpper = isLike ? e.getName().toUpperCase() : e.getName();

        testSearchValueP(url, nameUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameLower = e.getName().toLowerCase();
            String nameLShortened = nameLower.substring(1, nameLower.length() - 2);
            testSearchValueP(url, nameLower);
            testSearchValueP(url, nameLShortened);
        }
    }

    private void searchLabel(boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.LABEL_LIKE : ResourcePath.LABEL;
        String labelUpper = isLike ? e.getLabel().toUpperCase() : e.getLabel();

        testSearchValueP(url, labelUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String labelLower = e.getLabel().toLowerCase();
            String labelLowerShortened = labelLower.substring(1, labelLower.length() - 2);
            testSearchValueP(url, labelLower);
            testSearchValueP(url, labelLowerShortened);
        }
    }

    private void testSearchValueP(String url, String name, ResultMatcher status) throws Exception {
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

    private void testSearchValueP(String url, String name) throws Exception {
        testSearchValueP(url, name, status().isOk());
    }

    private void searchMulti(boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        MultiValueMap<String, String> paramMapBase = new LinkedMultiValueMap<>();
        paramMapBase.add(pageSizeAttr, pageSize);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String url;

        // region MULTI_LIKE - OK
        String notFound = random.nextString();
        url = isLike ? ResourcePath.MULTI_LIKE : ResourcePath.MULTI;
        String nameL = isLike ? e.getName().toLowerCase() : e.getName();
        String labelL = isLike ? e.getLabel().toLowerCase() : e.getLabel();

        // region name (lower case?) - label invalid
        paramMap.clear();
        paramMap.add(ResourcePath.NAME, nameL);
        paramMap.add(ResourcePath.LABEL, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - label (lower case?)
        paramMap.clear();
        paramMap.add(ResourcePath.LABEL, labelL);
        paramMap.add(ResourcePath.NAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // endregion

        if (isLike) {
            // as it is like, test the other alternatives as well
            String nameU = e.getName().toUpperCase();
            String labelU = e.getLabel().toUpperCase();

            // region name (upper case) - label invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU);
            paramMap.add(ResourcePath.LABEL, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name (upper case shortened) - label invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU.substring(1, nameU.length() - 2));
            paramMap.add(ResourcePath.LABEL, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - label (upper case)
            paramMap.clear();
            paramMap.add(ResourcePath.LABEL, labelU);
            paramMap.add(ResourcePath.NAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region MULTI_LIKE - KO

            // region name ok - label null
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name null - label ok
            paramMap.clear();
            paramMap.add(ResourcePath.LABEL, labelL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // endregion
        }
    }

    private MvcResult testSearchMulti(String url, MultiValueMap<String, String> paramMap, boolean useMvcNonDocumenter, ResultMatcher status) throws Exception {
        MockMvc mockMvc = useMvcNonDocumenter ? mvcNonDocumenter : mvc;
        return mockMvc.perform(
                get(apiPrefix + "/" + reqString + "/search/" + url)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .params(paramMap)
        ).andExpect(status).andReturn();
    }

    private void testSearchMulti(String url, MultiValueMap<String, String> paramMap) throws Exception {
        checkMvcResult(testSearchMulti(url, paramMap, false, status().isOk()));
    }

    private void checkMvcResult(MvcResult mvcResult) throws JSONException, UnsupportedEncodingException {
        final String valueInJSON = GmsSecurityUtil.getValueInJSON(mvcResult.getResponse().getContentAsString(), "_embedded");
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.PERMISSION));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

}