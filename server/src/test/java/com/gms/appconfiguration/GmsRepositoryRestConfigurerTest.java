package com.gms.appconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class GmsRepositoryRestConfigurerTest {

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
    private EUserRepository repository;

    /**
     * Instance of {@link MockMvc} to performe web requests.
     */
    private MockMvc mvc;
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
     * Base path for requests in this test suite (after the base API path).
     */
    private static final String REQ_STRING = ResourcePath.USER;
    //endregion

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = defaultConstant.getApiBasePath();
        authHeader = securityConstant.getATokenHeader();
        tokenType = securityConstant.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(defaultConstant,
                                                                securityConstant,
                                                                mvc,
                                                                objectMapper,
                                                                false);
    }

    /**
     * Tests with only one domain class. The same behavior must be specified for all domain classes in method
     * {@link GmsRepositoryRestConfigurer#configureRepositoryRestConfiguration
     * (org.springframework.data.rest.core.config.RepositoryRestConfiguration)}
     */
    @Test
    public void exposeIdsForDomainClasses() throws Exception {
        EUser u = repository.save(EntityUtil.getSampleUser());
        String result = mvc.perform(
                get(apiPrefix + "/" + REQ_STRING + "/{userId}", u.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        final String id = GmsSecurityUtil.getValueInJSON(result, "id");
        assertTrue("Entity id not found", id != null && !"".equals(id));
        assertEquals(u.getId(), Long.valueOf(id));
    }

}
