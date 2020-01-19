package com.gms.repository.security.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
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
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BPermissionRepositoryTest {

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
     * Instance of {@link DefaultConst}.
     */
    @Autowired
    private DefaultConst dc;
    /**
     * Instance of {@link SecurityConst}.
     */
    @Autowired
    private SecurityConst sc;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link BPermissionRepository}.
     */
    @Autowired
    private BPermissionRepository repository;

    /**
     * Instance of {@link MockMvc}.
     */
    private MockMvc mvc;
    /**
     * Instance of {@link MockMvc} with a specific configuration to not create documentation from the results from the
     * web requests.
     */
    private MockMvc mvcNonDocumentary;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    /**
     * Base path for the API.
     */
    private String apiPrefix;
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
     * Attributes to set the page size of the response.
     */
    private String pageSizeAttr;
    /**
     * Attributes to set the sorting strategy of the response.
     */
    private String pageSortAttr;
    /**
     * Page size value of the response.
     */
    private String pageSize;
    /**
     * Base path for requests in this test suite (after the base API path).
     */
    private static final String REQ_STRING = ResourcePath.PERMISSION;
    //endregion

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
        mvcNonDocumentary = GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        pageSizeAttr = dc.getPageSizeParam();
        pageSortAttr = dc.getPageSortParam();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void list() throws Exception {
        setUpPermissions();

        String embeddedReqString = LinkPath.EMBEDDED + REQ_STRING;
        String[] sortParams = {"name,desc", "label"};
        mvc.perform(get(apiPrefix + "/" + REQ_STRING)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(embeddedReqString + "[].name")
                                                .description(BPermissionMeta.NAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].label")
                                                .description(BPermissionMeta.LABEL_INFO),
                                        fieldWithPath(embeddedReqString + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get())
                                                .description(GmsEntityMeta.SELF_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("bPermission"))
                                                .ignored(),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("roles"))
                                                .ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO)
                                )
                        )
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(dc)
                )));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getE() throws Exception {
        BPermission p = createPermissionUsingRepository();

        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}", p.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("name").description(BPermissionMeta.NAME_INFO),
                                fieldWithPath("label").description(BPermissionMeta.LABEL_INFO),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("bPermission")).ignored(),
                                fieldWithPath(LinkPath.get("roles")).description(BPermissionMeta.ROLES_LINK_INFO)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BPermissionMeta.ID_INFO))
                ));

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchMulti() throws Exception {
        searchMulti(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchMultiLike() throws Exception {
        searchMulti(true);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchName() throws Exception {
        searchName(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchNameLike() throws Exception {
        searchName(true);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchLabel() throws Exception {
        searchLabel(false);
    }

    /**
     * Test to be executed by JUnit.
     */
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

    private void searchName(final boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameUpper = isLike ? e.getName().toUpperCase(Locale.ENGLISH) : e.getName();

        testSearchValueP(url, nameUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameLower = e.getName().toLowerCase(Locale.ENGLISH);
            String nameLShortened = nameLower.substring(1, nameLower.length() - 2);
            testSearchValueP(url, nameLower);
            testSearchValueP(url, nameLShortened);
        }
    }

    private void searchLabel(final boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.LABEL_LIKE : ResourcePath.LABEL;
        String labelUpper = isLike ? e.getLabel().toUpperCase(Locale.ENGLISH) : e.getLabel();

        testSearchValueP(url, labelUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String labelLower = e.getLabel().toLowerCase(Locale.ENGLISH);
            String labelLowerShortened = labelLower.substring(1, labelLower.length() - 2);
            testSearchValueP(url, labelLower);
            testSearchValueP(url, labelLowerShortened);
        }
    }

    private void testSearchValueP(final String url, final String name, final ResultMatcher status) throws Exception {
        final MvcResult mvcResult = mvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/search/" + url)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(ResourcePath.QUERY_VALUE, name)
        ).andExpect(status).andReturn();

        if (status == status().isOk()) {
            checkMvcResult(mvcResult);
        }
    }

    private void testSearchValueP(final String url, final String name) throws Exception {
        testSearchValueP(url, name, status().isOk());
    }

    private void searchMulti(final boolean isLike) throws Exception {
        BPermission e = repository.save(EntityUtil.getSamplePermission(random.nextString()));
        assertNotNull(e);
        MultiValueMap<String, String> paramMapBase = new LinkedMultiValueMap<>();
        paramMapBase.add(pageSizeAttr, pageSize);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String url;

        // region MULTI_LIKE - OK
        String notFound = random.nextString();
        url = isLike ? ResourcePath.MULTI_LIKE : ResourcePath.MULTI;
        String nameL = isLike ? e.getName().toLowerCase(Locale.ENGLISH) : e.getName();
        String labelL = isLike ? e.getLabel().toLowerCase(Locale.ENGLISH) : e.getLabel();

        // region name (lower case?) - label invalid
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
            String nameU = e.getName().toUpperCase(Locale.ENGLISH);
            String labelU = e.getLabel().toUpperCase(Locale.ENGLISH);

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

    private MvcResult testSearchMulti(
            final String url,
            final MultiValueMap<String, String> paramMap,
            final boolean useMvcNonDocumenter,
            final ResultMatcher status
    ) throws Exception {
        MockMvc mockMvc = useMvcNonDocumenter ? mvcNonDocumentary : mvc;
        return mockMvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/search/" + url)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .params(paramMap)
        ).andExpect(status).andReturn();
    }

    private void testSearchMulti(final String url, final MultiValueMap<String, String> paramMap) throws Exception {
        checkMvcResult(testSearchMulti(url, paramMap, false, status().isOk()));
    }

    private void checkMvcResult(final MvcResult mvcResult) throws JSONException, UnsupportedEncodingException {
        final String valueInJSON =
                GmsSecurityUtil.getValueInJSON(mvcResult.getResponse().getContentAsString(), "_embedded");
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.PERMISSION));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

}
