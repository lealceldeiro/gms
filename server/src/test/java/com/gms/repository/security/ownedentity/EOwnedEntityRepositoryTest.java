package com.gms.repository.security.ownedentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.service.AppService;
import com.gms.util.GMSRandom;
import com.gms.util.GmsSecurityUtil;
import com.gms.util.RestDoc;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.Resource;
import com.gms.util.constant.SecurityConst;
import com.gms.util.validation.ConstrainedFields;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EOwnedEntityRepositoryTest
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 08, 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EOwnedEntityRepositoryTest {

    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EOwnedEntityRepository repository;

    private MockMvc mvc;
    private RestDocumentationResultHandler restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private int pageSize;
    private static final String reqString = Resource.OWNED_ENTITY_PATH;
    private static final String name = "SampleName-";
    private static final String username = "SampleUsername-";
    private static final String description = "SampleDescription-";
    //endregion

    private final GMSRandom random = new GMSRandom();

    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed.", appService.isInitialLoadOK());

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
    public void createOwnedEntity() throws Exception {
        EOwnedEntity e = new EOwnedEntity(name + random.nextString(), username + random.nextString(),
                description + random.nextString());
        ConstrainedFields fields = new ConstrainedFields(EOwnedEntity.class);

        mvc.perform(
                post(apiPrefix + "/" + reqString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
                        .content(objectMapper.writeValueAsString(e))
        ).andExpect(status().isCreated())
                .andDo(
                        restDocResHandler.document(
                                requestFields(
                                        fields.withPath("name").description("Natural name which is used commonly for referring to the entity"),
                                        fields.withPath("username").description("A unique string representation of the {@link #name}. Useful when there are other entities with the same {@link #name}"),
                                        fields.withPath("description").description("A brief description of the entity")
                                )
                        )
                );
    }

    @Test
    public void listOwnedEntity() throws Exception {
        mvc.perform(
                get(apiPrefix + "/" + reqString + "?" + pageSizeAttr + "=" + pageSize)
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("_embedded." + reqString).description("Array of owned entities"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to owned entities"),
                                fieldWithPath("page").description("Options for paging the owned entities")
                        )
                )
        );
    }

    @Test
    public void getOwnedEntity() throws Exception {
        EOwnedEntity e = repository.save(new EOwnedEntity(name + random.nextString(), username + random.nextString(), description + random.nextString()));
        mvc.perform(
                get(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("name").description("Natural name which is used commonly for referring to the entity"),
                                fieldWithPath("username").description(" unique string representation of the {@link #name}. Useful when there are other entities with the same {@link #name}"),
                                fieldWithPath("description").description("A brief description of the entity"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned owned entity")
                        )
                )
        );
    }

    @Test
    public void updateOwnedEntity() throws Exception {
        EOwnedEntity e = repository.save(new EOwnedEntity(name + random.nextString(), username + random.nextString(), description + random.nextString()));
        EOwnedEntity e2 = new EOwnedEntity(name + "Updated-" + random, username + "Updated-" + random.nextString(),
                description + "Updated-" + random.nextString());
        mvc.perform(
                put(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e2))
        ).andExpect(status().isOk()).andDo(
                restDocResHandler.document(
                        responseFields(
                                fieldWithPath("name").description("Natural name which is used commonly for referring to the entity"),
                                fieldWithPath("username").description(" unique string representation of the {@link #name}. Useful when there are other entities with the same {@link #name}"),
                                fieldWithPath("description").description("A brief description of the entity"),
                                fieldWithPath("_links").description("Available links for requesting other webservices related to the returned owned entity")
                        )
                )
        );
    }

    @Test
    public void deleteOwnedEntity() throws Exception {
        EOwnedEntity e = repository.save(new EOwnedEntity(name + random.nextString(), username + random.nextString(), description + random.nextString()));
        mvc.perform(
                delete(apiPrefix + "/" + reqString + "/" + e.getId())
                        .header(authHeader, tokenType + " " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}