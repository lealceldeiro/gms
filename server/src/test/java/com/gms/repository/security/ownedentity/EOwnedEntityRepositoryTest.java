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
public class EOwnedEntityRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EOwnedEntityRepository repository;

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
    private static final String reqString = ResourcePath.OWNED_ENTITY;
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
        String[] sortParams = {"name", "username,asc"};
        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize)
                .param(pageSortAttr, sortParams))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingFields(
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].name").description(EOwnedEntityMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].username").description(EOwnedEntityMeta.username),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].description").description(EOwnedEntityMeta.description),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("eOwnedEntity")).ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                ))
                ))
                .andDo(restDocResHandler.document(relaxedRequestParameters(
                        RestDoc.getRelaxedPagingParameters(dc)
                )));
    }

    @Test
    public void getE() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        mvc.perform(get(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("name").description(EOwnedEntityMeta.name),
                                fieldWithPath("username").description(EOwnedEntityMeta.username),
                                fieldWithPath("description").description(EOwnedEntityMeta.description),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("eOwnedEntity")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.id))
                ));
    }

    @Test
    public void update() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        EOwnedEntity e2 = EntityUtil.getSampleEntity(random.nextString());
        mvc.perform(put(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                fieldWithPath("id").description(GmsEntityMeta.id),
                                fieldWithPath("name").description(EOwnedEntityMeta.name),
                                fieldWithPath("username").description(EOwnedEntityMeta.username),
                                fieldWithPath("description").description(EOwnedEntityMeta.description),
                                fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self),
                                fieldWithPath(LinkPath.get("eOwnedEntity")).ignored()
                        )
                ))
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.id))
                ));
    }

    @Test
    public void deleteE() throws Exception {
        String r = random.nextString();
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(r));
        mvc.perform(delete(apiPrefix + "/" + reqString + "/{id}", e.getId())
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocResHandler.document(
                        pathParameters(parameterWithName("id").description(EOwnedEntityMeta.id))
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

    private void searchName(boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.NAME_LIKE : ResourcePath.NAME;
        String nameU = isLike ? e.getName().toUpperCase() : e.getName();

        testSearchValueOE(url, nameU);

        if (isLike) { // check also for the same result with the name in lower case and a with substring of it
            String nameL = e.getName().toLowerCase();
            String nameLShortened = nameL.substring(1, nameL.length() - 2);
            testSearchValueOE(url, nameL);
            testSearchValueOE(url, nameLShortened);
        }
    }

    private void searchUsername(boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
        assertNotNull(e);
        String url = isLike ? ResourcePath.USERNAME_LIKE : ResourcePath.USERNAME;
        String usernameU = isLike ? e.getUsername().toUpperCase() : e.getUsername();

        testSearchValueOE(url, usernameU);

        if (isLike) { // check also for the same result with the username in lower case and a with substring of it
            String usernameL = e.getUsername().toLowerCase();
            String usernameLShortened = usernameL.substring(1, usernameL.length() - 2);
            testSearchValueOE(url, usernameL);
            testSearchValueOE(url, usernameLShortened);
        }
    }

    private void testSearchValueOE(String url, String name, ResultMatcher status) throws Exception {
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

    private void testSearchValueOE(String url, String name) throws Exception {
        testSearchValueOE(url, name, status().isOk());
    }

    private void searchMulti(boolean isLike) throws Exception {
        EOwnedEntity e = repository.save(EntityUtil.getSampleEntity(random.nextString()));
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

        // region name (lower case?) - username invalid
        paramMap.clear();
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
            String nameU = e.getName().toUpperCase();
            String usernameU = e.getUsername().toUpperCase();

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
        JSONArray entities = new JSONArray(GmsSecurityUtil.getValueInJSON(valueInJSON, ResourcePath.OWNED_ENTITY));
        assertNotNull(entities);
        assertTrue(entities.length() > 0);
    }
}