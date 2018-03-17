package com.gms.repository.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.service.AppService;
import com.gms.util.*;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.LinkPath;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
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
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EUserRepositoryTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 12, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EUserRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EUserRepository repository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private String pageSize;
    private static final String reqString = ResourcePath.USER;

    private EUser user;
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

        pageSizeAttr = dc.getPageSizeHolder();
        pageSize = dc.getPageSize();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    @Test
    public void list() throws Exception {
        mvc.perform(get(apiPrefix + "/" + reqString)
                .header(authHeader, tokenType + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .param(pageSizeAttr, pageSize))
                .andExpect(status().isOk())
                .andDo(restDocResHandler.document(
                        responseFields(
                                RestDoc.getPagingfields(
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
                ));
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

    private void createSampleUser() {
        user = repository.save(EntityUtil.getSampleUser(random.nextString()));
    }
}