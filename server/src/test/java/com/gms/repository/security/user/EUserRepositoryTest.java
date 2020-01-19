package com.gms.repository.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
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
import java.util.Collection;
import java.util.LinkedList;
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
public class EUserRepositoryTest {

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
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository repository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;
    /**
     * Instance of {@link MockMvc} with a specific configuration to not document the results from the web requests.
     */
    private MockMvc mvpNonDocumentary;
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
    private static final String REQ_STRING = ResourcePath.USER;

    /**
     * An instance of {@link EUser}.
     */
    private EUser user;
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
        mvpNonDocumentary = GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

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
        String[] sortParams = {"username,asc", "name,desc"};
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
                                        fieldWithPath(embeddedReqString + "[].username")
                                                .description(EUserMeta.USERNAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].email")
                                                .description(EUserMeta.EMAIL_INFO),
                                        fieldWithPath(embeddedReqString + "[].name")
                                                .description(EUserMeta.NAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].lastName")
                                                .description(EUserMeta.LAST_NAME_INFO),
                                        fieldWithPath(embeddedReqString + "[].password").ignored(),
                                        fieldWithPath(embeddedReqString + "[].enabled")
                                                .description(EUserMeta.ENABLED_INFO),
                                        fieldWithPath(embeddedReqString + "[].emailVerified")
                                                .description(EUserMeta.EMAIL_VERIFIED_INFO),
                                        fieldWithPath(embeddedReqString + "[].accountNonExpired")
                                                .description(EUserMeta.ACCOUNT_NON_EXPIRED_INFO),
                                        fieldWithPath(embeddedReqString + "[].accountNonLocked")
                                                .description(EUserMeta.ACCOUNT_NON_LOCKED_INFO),
                                        fieldWithPath(embeddedReqString + "[].credentialsNonExpired")
                                                .description(EUserMeta.CREDENTIALS_NON_EXPIRED_INFO),
                                        fieldWithPath(embeddedReqString + "[].id")
                                                .description(GmsEntityMeta.ID_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get())
                                                .description(GmsEntityMeta.SELF_INFO),
                                        fieldWithPath(embeddedReqString + "[]." + LinkPath.get("eUser")).ignored(),
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
        createSampleUser();
        mvc.perform(get(apiPrefix + "/" + REQ_STRING + "/{id}", user.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id")
                                        .description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("username")
                                        .description(EUserMeta.USERNAME_INFO),
                                fieldWithPath("email").optional()
                                        .description(EUserMeta.EMAIL_INFO),
                                fieldWithPath("name").optional()
                                        .description(EUserMeta.NAME_INFO),
                                fieldWithPath("lastName").optional()
                                        .description(EUserMeta.LAST_NAME_INFO),
                                fieldWithPath("password").ignored(),
                                fieldWithPath("enabled").optional()
                                        .description(EUserMeta.ENABLED_INFO),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional()
                                        .description(EUserMeta.EMAIL_VERIFIED_INFO),
                                fieldWithPath("accountNonExpired").optional()
                                        .description(EUserMeta.ACCOUNT_NON_EXPIRED_INFO),
                                fieldWithPath("accountNonLocked").optional()
                                        .description(EUserMeta.ACCOUNT_NON_LOCKED_INFO),
                                fieldWithPath("credentialsNonExpired").optional()
                                        .description(EUserMeta.CREDENTIALS_NON_EXPIRED_INFO),
                                fieldWithPath(LinkPath.get())
                                        .description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("eUser")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void update() throws Exception {
        createSampleUser();
        EUser e2 = EntityUtil.getSampleUser(random.nextString());
        e2.setEnabled(true);
        mvc.perform(
                put(apiPrefix + "/" + REQ_STRING + "/{id}", user.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id")
                                        .description(GmsEntityMeta.ID_INFO),
                                fieldWithPath("username")
                                        .description(EUserMeta.USERNAME_INFO),
                                fieldWithPath("email")
                                        .description(EUserMeta.EMAIL_INFO),
                                fieldWithPath("name")
                                        .description(EUserMeta.NAME_INFO),
                                fieldWithPath("lastName")
                                        .description(EUserMeta.LAST_NAME_INFO),
                                fieldWithPath("password").ignored(),
                                fieldWithPath("enabled")
                                        .description(EUserMeta.ENABLED_INFO),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional()
                                        .description(EUserMeta.EMAIL_VERIFIED_INFO),
                                fieldWithPath("accountNonExpired").optional()
                                        .description(EUserMeta.ACCOUNT_NON_EXPIRED_INFO),
                                fieldWithPath("accountNonLocked").optional()
                                        .description(EUserMeta.ACCOUNT_NON_LOCKED_INFO),
                                fieldWithPath("credentialsNonExpired").optional()
                                        .description(EUserMeta.CREDENTIALS_NON_EXPIRED_INFO),
                                fieldWithPath(LinkPath.get())
                                        .description(GmsEntityMeta.SELF_INFO),
                                fieldWithPath(LinkPath.get("eUser")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.ID_INFO))
                ));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void deleteE() throws Exception {
        createSampleUser();
        mvc.perform(delete(apiPrefix + "/" + REQ_STRING + "/{id}", user.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.ID_INFO))
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

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchEmail() throws Exception {
        searchEmail(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchEmailLike() throws Exception {
        searchEmail(true);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchUserNameEmail() throws Exception {
        EUser u = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        mvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/search/" + ResourcePath.USERNAME_EMAIL)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(ResourcePath.USERNAME, u.getUsername())
                        .param(ResourcePath.EMAIL, u.getEmail())
        )
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(
                                parameterWithName("username").description(EUserMeta.USERNAME_INFO).optional(),
                                parameterWithName("email").description(EUserMeta.EMAIL_INFO).optional()
                        ))
                );
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchLastName() throws Exception {
        searchLastName(false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void searchLastNameLike() throws Exception {
        searchLastName(true);
    }

    private void createSampleUser() {
        user = repository.save(EntityUtil.getSampleUser(random.nextString()));
    }

    private void searchName(final boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameU = isLike ? e.getName().toUpperCase(Locale.ENGLISH) : e.getName();

        testSearchValueU(url, nameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameL = e.getName().toLowerCase(Locale.ENGLISH);
            String nameLShortened = nameL.substring(1, nameL.length() - 2);
            testSearchValueU(url, nameL);
            testSearchValueU(url, nameLShortened);
        }
    }

    private void searchLastName(final boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USER_SEARCH_LASTNAME_LIKE : ResourcePath.LASTNAME;
        String lastnameU = isLike ? e.getLastName().toUpperCase(Locale.ENGLISH) : e.getLastName();

        testSearchValueU(url, lastnameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String lastnameL = e.getLastName().toLowerCase(Locale.ENGLISH);
            String lastnameLShortened = lastnameL.substring(1, lastnameL.length() - 2);
            testSearchValueU(url, lastnameL);
            testSearchValueU(url, lastnameLShortened);
        }
    }

    private void searchUsername(final boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USERNAME_LIKE : ResourcePath.USERNAME;
        String usernameU = isLike ? e.getUsername().toUpperCase(Locale.ENGLISH) : e.getUsername();

        testSearchValueU(url, usernameU);

        if (isLike) { // check also for the same result with the username in lower case and a with substring of it
            String usernameL = e.getUsername().toLowerCase(Locale.ENGLISH);
            String usernameLShortened = usernameL.substring(1, usernameL.length() - 2);
            testSearchValueU(url, usernameL);
            testSearchValueU(url, usernameLShortened);
        }
    }

    private void searchEmail(final boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USER_SEARCH_EMAIL_LIKE : ResourcePath.EMAIL;
        String emailU = isLike ? e.getEmail().toUpperCase(Locale.ENGLISH) : e.getName();

        testSearchValueU(url, emailU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String emailL = e.getEmail().toLowerCase(Locale.ENGLISH);
            String emailLShortened = emailL.substring(1, emailL.length() - 2);
            testSearchValueU(url, emailL);
            testSearchValueU(url, emailLShortened);
        }
    }

    private void testSearchValueU(final String url, final String name, final ResultMatcher status) throws Exception {
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

    private void testSearchValueU(final String url, final String name) throws Exception {
        testSearchValueU(url, name, status().isOk());
    }

    private void searchMulti(final boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
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
        String emailL = isLike ? e.getEmail().toLowerCase(Locale.ENGLISH) : e.getEmail();
        String lastNameL = isLike ? e.getLastName().toLowerCase(Locale.ENGLISH) : e.getLastName();

        // region name (lower case?) - username invalid - email invalid - lastName invalid
        prepareMapForSearchMultiTest(paramMap, nameL, notFound, notFound, notFound, paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username (lower case?) - email invalid - lastName invalid
        prepareMapForSearchMultiTest(paramMap, notFound, usernameL, notFound, notFound, paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username invalid - email (lower case?) - lastName invalid
        prepareMapForSearchMultiTest(paramMap, notFound, notFound, emailL, notFound, paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username invalid - email invalid - lastName (lower case?)
        prepareMapForSearchMultiTest(paramMap, notFound, notFound, notFound, lastNameL, paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // endregion

        if (isLike) {
            // as it is like, test the other alternatives as well
            String nameU = e.getName().toUpperCase(Locale.ENGLISH);
            String usernameU = e.getUsername().toUpperCase(Locale.ENGLISH);
            String emailU = e.getEmail().toUpperCase(Locale.ENGLISH);
            String lastNameU = e.getLastName().toUpperCase(Locale.ENGLISH);

            // region name (upper case) - username invalid - email invalid, lastName invalid
            prepareMapForSearchMultiTest(paramMap, nameU, notFound, notFound, notFound, paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name (upper case shortened) - username invalid - email invalid, lastName invalid
            prepareMapForSearchMultiTest(paramMap, nameU.substring(1, nameU.length() - 2), notFound, notFound,
                    notFound, paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username (upper case) - email invalid, lastName invalid
            prepareMapForSearchMultiTest(paramMap, notFound, usernameU, notFound, notFound, paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username invalid - email (upper case), lastName invalid
            prepareMapForSearchMultiTest(paramMap, notFound, notFound, emailU, notFound, paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username invalid - email invalid, lastName (upper case)
            prepareMapForSearchMultiTest(paramMap, notFound, notFound, notFound, lastNameU, paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region MULTI_LIKE - KO

            // region name ok - username ok - email ok - lastName null
            prepareMapForSearchMultiTest(paramMap, nameL, usernameL, emailL, null, paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name ok - username ok - email null - lastName ok
            prepareMapForSearchMultiTest(paramMap, nameL, usernameL, null, lastNameL, paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name ok - username null - email ok - lastName ok
            prepareMapForSearchMultiTest(paramMap, nameL, null, emailL, lastNameL, paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name null - username ok - email ok - lastName ok
            prepareMapForSearchMultiTest(paramMap, null, usernameL, emailL, lastNameL, paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // endregion
        }
    }

    private void prepareMapForSearchMultiTest(final MultiValueMap<String, String> paramMap, final String nameArg,
                                              final String usernameArg, final String emailArg, final String lastNameArg,
                                              final MultiValueMap<String, String> paramMapBase) {
        paramMap.clear();

        paramMap.add(ResourcePath.NAME, nameArg);
        paramMap.add(ResourcePath.USERNAME, usernameArg);
        paramMap.add(ResourcePath.EMAIL, emailArg);
        paramMap.add(ResourcePath.LASTNAME, lastNameArg);
        paramMap.addAll(paramMapBase);
    }

    private MvcResult testSearchMulti(final String url, final MultiValueMap<String, String> paramMap,
                                      final boolean useMvcNonDocumentary, final ResultMatcher status)
            throws Exception {
        MockMvc mockMvc = useMvcNonDocumentary ? mvpNonDocumentary : mvc;
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
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.USER));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void saveAll() {
        Collection<EUser> list = new LinkedList<>();
        EUser u;
        for (int i = 0; i < 2; i++) {
            u = EntityUtil.getSampleUser(random.nextString());
            u.setEnabled(true);
            list.add(u);
        }
        for (EUser userN : repository.saveAll(list)) {
            assertTrue(list.contains(userN));
        }
    }

}
