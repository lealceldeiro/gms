package com.gms.controller.security.permission;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.GmsEntityMeta;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.permission.BPermissionMeta;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.role.BRoleMeta;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.repository.security.role.BRoleRepository;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PermissionControllerTest {

        @Rule
        public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

        @Autowired
        private WebApplicationContext context;
        private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

        @Autowired
        private FilterChainProxy springSecurityFilterChain;

        @Autowired
        private SecurityConst sc;
        @Autowired
        private DefaultConst dc;

        @Autowired
        private AppService appService;
        @Autowired
        private BRoleRepository roleRepository;
        @Autowired
        private BPermissionRepository repository;

        private MockMvc mvc;
        private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

        // region vars
        private String apiPrefix;
        private String authHeader;
        private String tokenType;
        private String accessToken;
        private static final String reqString = ResourcePath.PERMISSION;
        // endregion

        private final GMSRandom random = new GMSRandom();

        @Before
        public void setUp() throws Exception {
                assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

                mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

                apiPrefix = dc.getApiBasePath();
                authHeader = sc.getATokenHeader();
                tokenType = sc.getATokenType();

                accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
        }

        @Test
        public void getRoles() throws Exception {
                BPermission p = repository.save(EntityUtil.getSamplePermission(random.nextString()));
                BRole r = EntityUtil.getSampleRole(random.nextString());
                BRole r2 = EntityUtil.getSampleRole(random.nextString());
                r.addPermission(p);
                r2.addPermission(p);
                roleRepository.save(r);
                roleRepository.save(r2);

                mvc.perform(get(apiPrefix + "/" + reqString + "/{id}/" + ResourcePath.ROLE + "s", p.getId())
                                .header(authHeader, tokenType + " " + accessToken).accept("application/hal+json"))
                                .andExpect(status().isOk())
                                .andDo(restDocResHandler.document(
                                        responseFields(
                                                RestDoc.getPagingFields(
                                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].label").description(BRoleMeta.label),
                                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].id").description(GmsEntityMeta.id),
                                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].description").description(BRoleMeta.description),
                                                        fieldWithPath(LinkPath.EMBEDDED + ResourcePath.ROLE + "[].enabled").description(BRoleMeta.enabled),
                                                        fieldWithPath(LinkPath.get()).description(GmsEntityMeta.self)
                                                )
                                        )
                                ))
                                .andDo(restDocResHandler.document(
                                        pathParameters(parameterWithName("id").description(BPermissionMeta.id))
                                ))
                                .andDo(restDocResHandler.document(relaxedRequestParameters(
                                        RestDoc.getRelaxedPagingParameters(dc)
                                )));

        }

}