package com.gms.repository.security.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
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
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
public class BRoleRepositoryTest {

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
     * Instance of {@link SecurityConst}.
     */
    @Autowired
    private SecurityConst sc;
    /**
     * Instance of {@link DefaultConst}.
     */
    @Autowired
    private DefaultConst dc;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository repository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
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
    private static final String REQ_STRING = ResourcePath.ROLE;
    /**
     * "label" field value.
     */
    private static final String LABEL = "SampleLabel-";
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
    public void create() throws Exception {
        BRole e = EntityUtil.getSampleRole(random.nextString());
        ConstrainedFields fields = new ConstrainedFields(BRole.class);

        mvc.perform(post(apiPrefix + "/" + REQ_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("label")
                                                .description(BRoleMeta.LABEL_INFO),
                                        fields.withPath("description").optional()
                                                .description(BRoleMeta.DESCRIPTION_INFO),
                                        fields.withPath("permissions").optional().ignored()
                                                .description(BRoleMeta.PERMISSIONS_INFO),
                                        fields.withPath("enabled").optional()
                                                .description(BRoleMeta.ENABLED_INFO)
                                )
                        )
                );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void list() throws Exception {
        String[] sortParams = {"label"};
        repository.save(EntityUtil.getSampleRole(random.nextString()));

        String embeddedReqString = LinkPath.EMBEDDED + REQ_STRING;
        mvc.perform(get(apiPrefix + "/" + REQ_STRING)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(embeddedReqString + "[].label")
                                                .description(BRoleMeta.LABEL_INFO),
                                        fieldWithPath(embeddedReqString + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(embeddedReqString + "[].description")
                                                .description(BRoleMeta.DESCRIPTION_INFO),
                                        fieldWithPath(embeddedReqString + "[].enabled")
                                                .description(BRoleMeta.ENABLED_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get())
                                                .description(GmsEntityMeta.SELF_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("bRole")).ignored(),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("permissions"))
                                                .description(BRoleMeta.PERMISSION_LINK_INFO),
                                        fieldWithPath(LinkPath.get())
                                                .description(GmsEntityMeta.SELF_INFO)
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
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("label").description(BRoleMeta.LABEL_INFO),
                                fieldWithPath("description").optional().description(BRoleMeta.DESCRIPTION_INFO),
                                fieldWithPath("enabled").optional().description(BRoleMeta.ENABLED_INFO),
                                fieldWithPath(LinkPath.get("self")).description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("bRole")).ignored(),
                                fieldWithPath(LinkPath.get("permissions")).description(BRoleMeta.PERMISSION_LINK_INFO)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void update() throws Exception {
        BRole e = repository.save(new BRole(LABEL + random.nextString()));
        BRole e2 = EntityUtil.getSampleRole(random.nextString());

        mvc.perform(put(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("label").description(BRoleMeta.LABEL_INFO),
                                fieldWithPath("description").description(BRoleMeta.DESCRIPTION_INFO),
                                fieldWithPath("enabled").description(BRoleMeta.ENABLED_INFO),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("bRole")).ignored(),
                                fieldWithPath(LinkPath.get("permissions")).description(BRoleMeta.PERMISSION_LINK_INFO)
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void deleteE() throws Exception {
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        mvc.perform(delete(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(BRoleMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchLabel() throws Exception {
        searchLabelR(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchLabelLike() throws Exception {
        searchLabelR(true);
    }

    private void searchLabelR(final boolean isLike) throws Exception {
        BRole e = repository.save(EntityUtil.getSampleRole(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.LABEL_LIKE : ResourcePath.LABEL;
        String labelUpper = isLike ? e.getLabel().toUpperCase(Locale.ENGLISH) : e.getLabel();

        testSearchValueR(url, labelUpper);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String labelRLower = e.getLabel().toLowerCase(Locale.ENGLISH);
            String labelRLowerShortened = labelRLower.substring(1, labelRLower.length() - 2);
            testSearchValueR(url, labelRLower);
            testSearchValueR(url, labelRLowerShortened);
        }
    }

    private void testSearchValueR(final String url, final String name, final ResultMatcher status) throws Exception {
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

    private void testSearchValueR(final String url, final String name) throws Exception {
        testSearchValueR(url, name, status().isOk());
    }

    private void checkMvcResult(final MvcResult mvcResult) throws JSONException, UnsupportedEncodingException {
        final String valueInJSON =
                GmsSecurityUtil.getValueInJSON(mvcResult.getResponse().getContentAsString(), "_embedded");
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.ROLE));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

}
