package com.gms.appconfiguration.locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConstant;
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

import javax.transaction.Transactional;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class GmsLocaleConfigurerTest {

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
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;

    /**
     * Instance of {@link MockMvc} to perform web requests.
     */
    private MockMvc mvc;

    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    /**
     * Instance of {@link EUser}.
     */
    private EUser user;
    /**
     * An instance of {@link EOwnedEntity}.
     */
    private EOwnedEntity entity;

    // region vars
    /**
     * API request URL.
     */
    private String url;
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
    // endregion

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, restDocumentation, restDocResHandler, springSecurityFilterChain);

        url = defaultConstant.getApiBasePath() + "/" + ResourcePath.USER + "/roles/{userId}/{entityId}";
        authHeader = securityConstant.getATokenHeader();
        tokenType = securityConstant.getATokenType();

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
    public void deleteRolesEN() throws Exception {
        user = userRepository.save(EntityUtil.getSampleUser());
        entity = entityRepository.save(EntityUtil.getSampleEntity());

        mvc.perform(delete(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .content(objectMapper.writeValueAsString(new int[]{-1}))
                .header(DefaultConstant.DEFAULT_LANGUAGE_HEADER, "en")
        ).andExpect(status().isNotFound());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void deleteRolesES() throws Exception {
        user = userRepository.save(EntityUtil.getSampleUser());
        entity = entityRepository.save(EntityUtil.getSampleEntity());

        mvc.perform(delete(url, user.getId(), entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(authHeader, tokenType + " " + accessToken)
                .param(defaultConstant.getLanguageHolder(), "es")
                .content(objectMapper.writeValueAsString(new int[]{-1}))
        ).andExpect(status().isNotFound());
    }

}
