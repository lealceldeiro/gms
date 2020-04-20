package com.gms.repository.security.ownedentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.ownedentity.EOwnedEntityMeta;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.LinkPath;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConstant;
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

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
@Transactional
public class EOwnedEntityRepositoryTest {

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
     * Instance of {@link SecurityConstant}.
     */
    @Autowired
    private SecurityConstant securityConstant;
    /**
     * Instance of {@link DefaultConstant}.
     */
    @Autowired
    private DefaultConstant defaultConstant;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository repository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
    /**
     * Instance of {@link MockMvc} to perform web request with a specific configuration to no create documentation from
     * the results of the requests.
     */
    private MockMvc mvcNonDocumenter;
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
    private static final String REQ_STRING = ResourcePath.OWNED_ENTITY;
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
        mvcNonDocumenter = GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = defaultConstant.getApiBasePath();
        authHeader = securityConstant.getATokenHeader();
        tokenType = securityConstant.getATokenType();

        pageSizeAttr = defaultConstant.getPageSizeParam();
        pageSortAttr = defaultConstant.getPageSortParam();
        pageSize = defaultConstant.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                securityConstant,
                                                                mvc,
                                                                objectMapper,
                                                                false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void list() throws Exception {
        String[] sortParams = {"name", "username,asc"};
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
                                        fieldWithPath(embeddedReqString + "[].name")
                                                .description(EOwnedEntityMeta.NAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].username")
                                                .description(EOwnedEntityMeta.USERNAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].description")
                                                .description(EOwnedEntityMeta.DESCRIPTION_INFO),
                                        fieldWithPath(embeddedReqString + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get())
                                                .description(GmsEntityMeta.SELF_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("eOwnedEntity"))
                                                .ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO)
                                ))
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(defaultConstant)
                )));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getE() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("name").description(EOwnedEntityMeta.NAME_INFO),
                                fieldWithPath("username").description(EOwnedEntityMeta.USERNAME_INFO),
                                fieldWithPath("description").description(EOwnedEntityMeta.DESCRIPTION_INFO),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("eOwnedEntity")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void update() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        EOwnedEntity e2 = EntityUtil.getSampleEntity(random.nextString());
        mvc.perform(put(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("name").description(EOwnedEntityMeta.NAME_INFO),
                                fieldWithPath("username").description(EOwnedEntityMeta.USERNAME_INFO),
                                fieldWithPath("description").description(EOwnedEntityMeta.DESCRIPTION_INFO),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("eOwnedEntity")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void deleteE() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        mvc.perform(delete(apiPrefix + "/" + REQ_STRING + "/{id}", e.getId())
                            .header(authHeader, tokenType + " " + accessToken)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.ID_INFO))
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
    public void searchUsername() throws Exception {
        searchUsername(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchUsernameLike() throws Exception {
        searchUsername(true);
    }

    private void searchName(final boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameU = isLike ? e.getName().toUpperCase(Locale.ENGLISH) : e.getName();

        testSearchValueOE(url, nameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameL = e.getName().toLowerCase(Locale.ENGLISH);
            String nameLShortened = nameL.substring(1, nameL.length() - 2);
            testSearchValueOE(url, nameL);
            testSearchValueOE(url, nameLShortened);
        }
    }

    private void searchUsername(final boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USERNAME_LIKE : ResourcePath.USERNAME;
        String usernameU = isLike ? e.getUsername().toUpperCase(Locale.ENGLISH) : e.getUsername();

        testSearchValueOE(url, usernameU);

        if (isLike) { // check also for the same result with the username in lower case and a with substring of it
            String usernameL = e.getUsername().toLowerCase(Locale.ENGLISH);
            String usernameLShortened = usernameL.substring(1, usernameL.length() - 2);
            testSearchValueOE(url, usernameL);
            testSearchValueOE(url, usernameLShortened);
        }
    }

    private void testSearchValueOE(final String url, final String name, final ResultMatcher status) throws Exception {
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

    private void testSearchValueOE(final String url, final String name) throws Exception {
        testSearchValueOE(url, name, status().isOk());
    }

    private void searchMulti(final boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        MultiValueMap<String, String> paramMapBase = new LinkedMultiValueMap<>();
        paramMapBase.add(pageSizeAttr, pageSize);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String url;

        // region MULTI_LIKE - OK
        String notFound = random.nextString();
        url = isLike ? ResourcePath.MULTI_LIKE : ResourcePath.MULTI;
        String nameL = isLike ? e.getName().toLowerCase(Locale.ENGLISH) : e.getName();
        String usernameL = isLike ? e.getUsername().toLowerCase(Locale.ENGLISH) : e.getUsername();

        // region name (lower case?) - username invalid
        paramMap.add(ResourcePath.NAME, nameL);
        paramMap.add(ResourcePath.USERNAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username (lower case?)
        paramMap.clear();
        paramMap.add(ResourcePath.USERNAME, usernameL);
        paramMap.add(ResourcePath.NAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // endregion

        if (isLike) {
            // as it is like, test the other alternatives as well
            String nameU = e.getName().toUpperCase(Locale.ENGLISH);
            String usernameU = e.getUsername().toUpperCase(Locale.ENGLISH);

            // region name (upper case) - username invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU);
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name (upper case shortened) - username invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU.substring(1, nameU.length() - 2));
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username (upper case)
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, usernameU);
            paramMap.add(ResourcePath.NAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region MULTI_LIKE - KO

            // region name ok - username null
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name null - username ok
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, usernameL);
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
        MockMvc mockMvc = useMvcNonDocumenter ? mvcNonDocumenter : mvc;
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
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.OWNED_ENTITY));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

}
