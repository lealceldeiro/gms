package com.gms.repository.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.domain.security.user.EUserMeta;
import com.gms.service.AppService;
import com.gms.util.EntityUtil;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
import com.gms.util.constant.DefaultConst;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
    private int pageSize;
    private static final String reqString = ResourcePath.USER;
    private static final String username = "SampleUsername-";
    private static final String email = "sample@email.com-";
    private static final String name = "SampleName-";
    private static final String lastName = "SampleLastName-";
    private static final String password = "SamplePassword-";

    private EUser user;
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
    public void listUser() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString + "?" + pageSizeAttr + "=" + pageSize)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("_embedded." + reqString).description("Array of users"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to users"),
                                fieldWithPath("page").description("Options for paging the users")
                        )
                )
        );
    }

    @Test
    public void getUser() throws Exception {
        createSampleUser();
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + user.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("username").description(EUserMeta.username),
                                fieldWithPath("email").optional().description(EUserMeta.email),
                                fieldWithPath("name").optional().description(EUserMeta.name),
                                fieldWithPath("lastName").optional().description(EUserMeta.lastName),
                                fieldWithPath("password").optional().description(EUserMeta.passwordHashed),
                                fieldWithPath("enabled").optional().description(EUserMeta.enabled),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional().description(EUserMeta.emailVerified),
                                fieldWithPath("accountNonExpired").optional().description(EUserMeta.accountNonExpired),
                                fieldWithPath("accountNonLocked").optional().description(EUserMeta.accountNonLocked),
                                fieldWithPath("credentialsNonExpired").optional().description(EUserMeta.credentialsNonExpired),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned user")
                        )
                )
        );
    }

    @Test
    public void updateUser() throws Exception {
        createSampleUser();
        EUser e2 = EntityUtil.getSampleUser(random.nextString());
        e2.setEnabled(true);
        mvc.perform(
                put(apiPrefix + "/" + reqString + "/" + user.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e2))
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("username").description(EUserMeta.username),
                                fieldWithPath("email").description(EUserMeta.email),
                                fieldWithPath("name").description(EUserMeta.name),
                                fieldWithPath("lastName").description(EUserMeta.lastName),
                                fieldWithPath("password").description(EUserMeta.passwordHashed),
                                fieldWithPath("enabled").description(EUserMeta.enabled),
                                fieldWithPath("authorities").optional().ignored(),
                                fieldWithPath("emailVerified").optional().description(EUserMeta.emailVerified),
                                fieldWithPath("accountNonExpired").optional().description(EUserMeta.accountNonExpired),
                                fieldWithPath("accountNonLocked").optional().description(EUserMeta.accountNonLocked),
                                fieldWithPath("credentialsNonExpired").optional().description(EUserMeta.credentialsNonExpired),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned role")
                        )
                )
        );
    }

    @Test
    public void deleteUser() throws Exception {
        createSampleUser();
        mvc.perform(
                delete(apiPrefix + "/" + reqString + "/" + user.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    private void createSampleUser() {
        user = repository.save(EntityUtil.getSampleUser(random.nextString()));
    }
}