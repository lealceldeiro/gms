package com.gms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.constant.SecurityConst;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
public class RepositoryConfigTest {

    @Autowired private WebApplicationContext context;
    private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired private AppService appService;
    @Autowired private EUserRepository repository;

    private MockMvc mvc;
    //region vars
    private String apiPrefix;
    private String authHeader;
    private String tokenType;
    private String accessToken;
    private static final String reqString = ResourcePath.USER;
    //endregion

    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc =  GmsMockUtil.getMvcMock(context, springSecurityFilterChain);

        apiPrefix = dc.getApiBasePath();
        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, false);
    }

    /**
     * Tests with only one domain class. The same behavior must be specified for all domain classes in method
     * {@link RepositoryConfig#configureRepositoryRestConfiguration(RepositoryRestConfiguration)}
     */
    @Test
    public void exposeIdsForDomainClasses() throws Exception{
        EUser u = repository.save(EntityUtil.getSampleUser());
        String result = mvc.perform(
                get(apiPrefix + "/" + reqString + "/{userId}", u.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(authHeader, tokenType + " " + accessToken)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        final String id = GmsSecurityUtil.getValueInJSON(result, "id");
        assertTrue("Entity id not found", id != null && !id.equals(""));
        assertEquals(u.getId(), Long.valueOf(id));
    }

}