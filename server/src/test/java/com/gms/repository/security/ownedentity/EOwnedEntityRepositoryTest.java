package com.gms.repository.security.ownedentity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.ownedentity.EOwnedEntityMeta;
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
    private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private String pageSizeAttr;
    private String pageSize;
    private static final String reqString = ResourcePath.OWNED_ENTITY;
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
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].name").description(EOwnedEntityMeta.name),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].username").description(EOwnedEntityMeta.username),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].description").description(EOwnedEntityMeta.description),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[].id").description(GmsEntityMeta.id),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get()).description(GmsEntityMeta.self),
                                        fieldWithPath(LinkPath.EMBEDDED + reqString + "[]." + LinkPath.get("eOwnedEntity")).ignored(),
                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                ))
                ));
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
}