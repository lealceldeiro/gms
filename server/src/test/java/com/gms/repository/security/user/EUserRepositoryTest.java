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
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
public class EUserRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EUserRepository repository;

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
    private static final String reqString = ResourcePath.USER;

    private EUser user;
    //endregion

    private final GMSRandom random = new GMSRandom();

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
        String[] sortParams = {"username,asc", "name,desc"};
        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].username").description(EUserMeta.username),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].email").description(EUserMeta.email),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].name").description(EUserMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].lastName").description(EUserMeta.lastName),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].password").ignored(),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].enabled").description(EUserMeta.enabled),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].emailVerified").description(EUserMeta.emailVerified),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].accountNonExpired").description(EUserMeta.accountNonExpired),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].accountNonLocked").description(EUserMeta.accountNonLocked),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].credentialsNonExpired").description(EUserMeta.credentialsNonExpired),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("eUser")).ignored(),
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
        createSampleUser();
        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}", user.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("username").description(EUserMeta.username),
                                fieldWithPath("email").optional().description(EUserMeta.email),
                                fieldWithPath("name").optional().description(EUserMeta.name),
                                fieldWithPath("lastName").optional().description(EUserMeta.lastName),
                                fieldWithPath("password").ignored(),
                                fieldWithPath("enabled").optional().description(EUserMeta.enabled),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional().description(EUserMeta.emailVerified),
                                fieldWithPath("accountNonExpired").optional().description(EUserMeta.accountNonExpired),
                                fieldWithPath("accountNonLocked").optional().description(EUserMeta.accountNonLocked),
                                fieldWithPath("credentialsNonExpired").optional().description(EUserMeta.credentialsNonExpired),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("eUser")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.id))
                ));
    }

    @Test
    public void update() throws Exception {
        createSampleUser();
        EUser e2 = EntityUtil.getSampleUser(random.nextString());
        e2.setEnabled(true);
        mvc.perform(
                put(apiPrefix + "/" + reqString + "/{id}", user.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("username").description(EUserMeta.username),
                                fieldWithPath("email").description(EUserMeta.email),
                                fieldWithPath("name").description(EUserMeta.name),
                                fieldWithPath("lastName").description(EUserMeta.lastName),
                                fieldWithPath("password").ignored(),
                                fieldWithPath("enabled").description(EUserMeta.enabled),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional().description(EUserMeta.emailVerified),
                                fieldWithPath("accountNonExpired").optional().description(EUserMeta.accountNonExpired),
                                fieldWithPath("accountNonLocked").optional().description(EUserMeta.accountNonLocked),
                                fieldWithPath("credentialsNonExpired").optional().description(EUserMeta.credentialsNonExpired),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("eUser")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.id))
                ));
    }

    @Test
    public void deleteE() throws Exception {
        createSampleUser();
        mvc.perform(delete(apiPrefix + "/" + reqString + "/{id}", user.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EUserMeta.id))
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
    public void searchUsername() throws Exception {
        searchUsername(false);
    }

    @Test
    public void searchUsernameLike() throws Exception {
        searchUsername(true);
    }

    @Test
    public void searchEmail() throws Exception {
        searchEmail(false);
    }

    @Test
    public void searchEmailLike() throws Exception {
        searchEmail(true);
    }

    @Test
    public void searchUserNameEmail() throws Exception {
        EUser u = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(u);
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/search/" + ResourcePath.USERNAME_EMAIL)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .param(ResourcePath.USERNAME, u.getUsername())
                        .param(ResourcePath.EMAIL, u.getEmail())
        )
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        relaxedRequestParameters(
                                parameterWithName("username").description(EUserMeta.username).optional(),
                                parameterWithName("email").description(EUserMeta.email).optional()
                        ))
                );
    }

    @Test
    public void searchLastName() throws Exception {
        searchLastName(false);
    }

    @Test
    public void searchLastNameLike() throws Exception {
        searchLastName(true);
    }

    private void createSampleUser() {
        user = repository.save(EntityUtil.getSampleUser(random.nextString()));
    }

    private void searchName(boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameU = isLike ? e.getName().toUpperCase() : e.getName();

        testSearchValueU(url, nameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameL = e.getName().toLowerCase();
            String nameLShortened = nameL.substring(1, nameL.length() - 2);
            testSearchValueU(url, nameL);
            testSearchValueU(url, nameLShortened);
        }
    }

    private void searchLastName(boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USER_SEARCH_LASTNAME_LIKE : ResourcePath.LASTNAME;
        String lastnameU = isLike ? e.getLastName().toUpperCase() : e.getLastName();

        testSearchValueU(url, lastnameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String lastnameL = e.getLastName().toLowerCase();
            String lastnameLShortened = lastnameL.substring(1, lastnameL.length() - 2);
            testSearchValueU(url, lastnameL);
            testSearchValueU(url, lastnameLShortened);
        }
    }

    private void searchUsername(boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USERNAME_LIKE : ResourcePath.USERNAME;
        String usernameU = isLike ? e.getUsername().toUpperCase() : e.getUsername();

        testSearchValueU(url, usernameU);

        if (isLike) { // check also for the same result with the username in lower case and a with substring of it
            String usernameL = e.getUsername().toLowerCase();
            String usernameLShortened = usernameL.substring(1, usernameL.length() - 2);
            testSearchValueU(url, usernameL);
            testSearchValueU(url, usernameLShortened);
        }
    }

    private void searchEmail(boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USER_SEARCH_EMAIL_LIKE : ResourcePath.EMAIL;
        String emailU = isLike ? e.getEmail().toUpperCase() : e.getName();

        testSearchValueU(url, emailU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String emailL = e.getEmail().toLowerCase();
            String emailLShortened = emailL.substring(1, emailL.length() - 2);
            testSearchValueU(url, emailL);
            testSearchValueU(url, emailLShortened);
        }
    }

    private void testSearchValueU(String url, String name, ResultMatcher status) throws Exception {
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

    private void testSearchValueU(String url, String name) throws Exception {
        testSearchValueU(url, name, status().isOk());
    }

    private void searchMulti(boolean isLike) throws Exception {
        EUser e = repository.save(EntityUtil.getSampleUser(random.nextString()));
        assertNotNull(e);
        MultiValueMap<String, String> paramMapBase = new LinkedMultiValueMap<>();
        paramMapBase.add(pageSizeAttr, pageSize);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String url;

        // region MULTI_LIKE - OK
        String notFound = random.nextString();
        url = isLike ? ResourcePath.MULTI_LIKE : ResourcePath.MULTI;
        String nameL = isLike ? e.getName().toLowerCase() : e.getName();
        String usernameL = isLike ? e.getUsername().toLowerCase() : e.getUsername();
        String emailL = isLike ? e.getEmail().toLowerCase() : e.getEmail();
        String lastNameL = isLike ? e.getLastName().toLowerCase() : e.getLastName();

        // region name (lower case?) - username invalid - email invalid - lastName invalid
        paramMap.clear();
        paramMap.add(ResourcePath.NAME, nameL);
        paramMap.add(ResourcePath.USERNAME, notFound);
        paramMap.add(ResourcePath.EMAIL, notFound);
        paramMap.add(ResourcePath.LASTNAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username (lower case?) - email invalid - lastName invalid
        paramMap.clear();
        paramMap.add(ResourcePath.USERNAME, usernameL);
        paramMap.add(ResourcePath.NAME, notFound);
        paramMap.add(ResourcePath.EMAIL, notFound);
        paramMap.add(ResourcePath.LASTNAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username invalid - email (lower case?) - lastName invalid
        paramMap.clear();
        paramMap.add(ResourcePath.USERNAME, notFound);
        paramMap.add(ResourcePath.NAME, notFound);
        paramMap.add(ResourcePath.EMAIL, emailL);
        paramMap.add(ResourcePath.LASTNAME, notFound);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // region name invalid - username invalid - email invalid - lastName (lower case?)
        paramMap.clear();
        paramMap.add(ResourcePath.USERNAME, notFound);
        paramMap.add(ResourcePath.NAME, notFound);
        paramMap.add(ResourcePath.EMAIL, notFound);
        paramMap.add(ResourcePath.LASTNAME, lastNameL);
        paramMap.addAll(paramMapBase);

        testSearchMulti(url, paramMap);
        //endregion

        // endregion

        if (isLike) {
            // as it is like, test the other alternatives as well
            String nameU = e.getName().toUpperCase();
            String usernameU = e.getUsername().toUpperCase();
            String emailU = e.getEmail().toUpperCase();
            String lastNameU = e.getLastName().toUpperCase();

            // region name (upper case) - username invalid - email invalid, lastName invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU);
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.add(ResourcePath.EMAIL, notFound);
            paramMap.add(ResourcePath.LASTNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name (upper case shortened) - username invalid - email invalid, lastName invalid
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameU.substring(1, nameU.length() - 2));
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.add(ResourcePath.EMAIL, notFound);
            paramMap.add(ResourcePath.LASTNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username (upper case) - email invalid, lastName invalid
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, usernameU);
            paramMap.add(ResourcePath.NAME, notFound);
            paramMap.add(ResourcePath.EMAIL, notFound);
            paramMap.add(ResourcePath.LASTNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username invalid - email (upper case), lastName invalid
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.add(ResourcePath.NAME, notFound);
            paramMap.add(ResourcePath.EMAIL, emailU);
            paramMap.add(ResourcePath.LASTNAME, notFound);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region name invalid - username invalid - email invalid, lastName (upper case)
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, notFound);
            paramMap.add(ResourcePath.NAME, notFound);
            paramMap.add(ResourcePath.EMAIL, notFound);
            paramMap.add(ResourcePath.LASTNAME, lastNameU);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap);
            //endregion

            // region MULTI_LIKE - KO

            // region name ok - username ok - email ok - lastName null
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameL);
            paramMap.add(ResourcePath.USERNAME, usernameL);
            paramMap.add(ResourcePath.EMAIL, emailL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name ok - username ok - email null - lastName ok
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameL);
            paramMap.add(ResourcePath.USERNAME, usernameL);
            paramMap.add(ResourcePath.LASTNAME, lastNameL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name ok - username null - email ok - lastName ok
            paramMap.clear();
            paramMap.add(ResourcePath.NAME, nameL);
            paramMap.add(ResourcePath.EMAIL, emailL);
            paramMap.add(ResourcePath.LASTNAME, lastNameL);
            paramMap.addAll(paramMapBase);

            testSearchMulti(url, paramMap, true, status().isBadRequest());
            //endregion

            // region name null - username ok - email ok - lastName ok
            paramMap.clear();
            paramMap.add(ResourcePath.USERNAME, usernameL);
            paramMap.add(ResourcePath.EMAIL, emailL);
            paramMap.add(ResourcePath.LASTNAME, lastNameL);
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
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.USER));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }

    @Test
    public void saveAll() {
        List<EUser> list = new LinkedList<>();
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