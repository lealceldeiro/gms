package com.gms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.Application;
import com.gms.component.security.authentication.AuthenticationFacade;
import com.gms.component.security.token.JWTService;
import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.repository.security.role.BRoleRepository;
import com.gms.repository.security.user.EUserRepository;
import com.gms.service.AppService;
import com.gms.service.security.user.UserService;
import com.gms.testutil.EntityUtil;
import com.gms.testutil.GmsMockUtil;
import com.gms.testutil.GmsSecurityUtil;
import com.gms.testutil.RestDoc;
import com.gms.testutil.StringUtil;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecurityConfigTest {

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
     * Instance of {@link SecurityConst}.
     */
    @Autowired
    private SecurityConst sc;
    /**
     * Instance of {@link DefaultConst}.
     */
    @Autowired
    private DefaultConst dc;

    /**
     * Instance of {@link AppService}.
     */
    @Autowired
    private AppService appService;
    /**
     * Instance of {@link UserService}.
     */
    @Autowired
    private UserService userService;
    /**
     * Instance of {@link BCryptPasswordEncoder}.
     */
    @Autowired
    private BCryptPasswordEncoder encoder;
    /**
     * Instance of {@link JWTService}.
     */
    @Autowired
    private JWTService jwtService;
    /**
     * Instance of {@link AuthenticationFacade}.
     */
    @Autowired
    private AuthenticationFacade authFacade;
    /**
     * Instance of {@link MessageResolver}.
     */
    @Autowired
    private MessageResolver msg;

    /**
     * Instance of {@link BRoleRepository}.
     */
    @Autowired
    private BRoleRepository roleRepository;
    /**
     * Instance of {@link EOwnedEntityRepository}.
     */
    @Autowired
    private EOwnedEntityRepository entityRepository;
    /**
     * Instance of {@link EUserRepository}.
     */
    @Autowired
    private EUserRepository userRepository;

    /**
     * Instance of {@link MockMvc} to be used to perform web request.
     */
    private MockMvc mvc;
    /**
     * An instance of {@link RestDocumentationResultHandler} for the documenting RESTful APIs endpoints.
     */
    private final RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

    /**
     * Header to be set with the authentication token.
     */
    private String authHeader;
    /**
     * Token type.
     */
    private String tokenType;
    /**
     * The access token.
     */
    private String accessToken;

    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_FREE_POST = "getFreePost";
    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_FREE_GET = "getFreeGet";
    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_FREE_ANY = "getFreeAny";
    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_ADDITIONAL_FREE_POST = "getAdditionalFreePostUrls";
    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_ADDITIONAL_FREE_GET = "getAdditionalFreeGetUrls";
    /**
     * Name of a method to be tested.
     */
    private static final String METHOD_GET_ADDITIONAL_FREE_ANY = "getAdditionalFreeAnyUrls";

    /**
     * Sets up the tests resources.
     */
    @Before
    public void setUp() throws Exception {
        assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        mvc = GmsMockUtil.getMvcMockForwardUrlNotNull(context, restDocumentation, restDocResHandler,
                springSecurityFilterChain);

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, restDocResHandler, false);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void login() throws Exception { // for documentation purposes
        assertNotEquals("",
                GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, restDocResHandler, false));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void loginKO() throws Exception {
        assertEquals("", GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, true));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void loginOKNoAuthorities() throws Exception {
        // region role
        BRole roleWithoutPermissions = roleRepository.save(EntityUtil.getSampleRole());
        assertNotNull(roleWithoutPermissions);
        // endregion

        // region entity
        EOwnedEntity entity = entityRepository.save(EntityUtil.getSampleEntity());
        assertNotNull(entity);
        // endregion

        // region user
        String r = new GMSRandom().nextString();
        String password = StringUtil.EXAMPLE_PASSWORD;
        EUser user = new EUser(StringUtil.EXAMPLE_USERNAME + r, "sc" + r + "@test.com", StringUtil.EXAMPLE_NAME + r,
                StringUtil.EXAMPLE_LAST_NAME + r, password);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setAccountNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        assertNotNull(userRepository.save(user));
        // endregion

        Collection<Long> ids = new LinkedList<>();
        ids.add(roleWithoutPermissions.getId());

        try {
            assertEquals("Role could not be added to user",
                    userService.addRolesToUser(user.getId(), entity.getId(), ids).size(), ids.size());

            Map<String, String> loginData = new HashMap<>();
            loginData.put(sc.getReqUsernameHolder(), user.getUsername());
            loginData.put(sc.getReqPasswordHolder(), password);

            mvc.perform(post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginData))).andExpect(status().isUnauthorized());
        } catch (NotFoundEntityException e) {
            LoggerFactory.getLogger(SecurityConfigTest.class).error(e.getLocalizedMessage());
            fail("Role could not be added to user");
        }

    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void baseUrlPermitAll() throws Exception {
        int s = mvc.perform(get("/")).andReturn().getResponse().getStatus();
        assertTrue(s != HttpStatus.FORBIDDEN.value());
        assertTrue(s != HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void apiUrlsAreProtected() throws Exception {
        mvc.perform(get(dc.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnAccessTokenIntegrityCompromised() throws Exception {
        // region completely wrong token
        mvc.perform(get(dc.getApiBasePath() + "/permissions").header(sc.getATokenHeader(), "sampleOfWrongToken"))
                .andExpect(status().isUnauthorized());
        // endregion

        // region token manipulated (string appended)
        mvc.perform(get(dc.getApiBasePath() + "/permissions").header(sc.getATokenHeader(), accessToken + "manipulated"))
                .andExpect(status().isUnauthorized());
        // endregion
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnAccessTokenDoesNotStartProperly() throws Exception {
        mvc.perform(get(dc.getApiBasePath() + "/permissions").header(sc.getATokenHeader(),
                "AnotherTypeDifferentFromBearer " + accessToken)).andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void failOnNotAccessTokenHeaderProvided() throws Exception {
        mvc.perform(get(dc.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void successOnCorrectAccessTokenProvided() throws Exception {
        mvc.perform(get(dc.getApiBasePath() + "/permission").header(authHeader, tokenType + " " + accessToken))
                .andExpect(status().isOk());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkGetFreePost() {
        checkGetFreeX(METHOD_GET_FREE_POST, METHOD_GET_ADDITIONAL_FREE_POST);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkGetFreeGet() {
        checkGetFreeX(METHOD_GET_FREE_GET, METHOD_GET_ADDITIONAL_FREE_GET);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkGetFreeAny() {
        checkGetFreeX(METHOD_GET_FREE_ANY, METHOD_GET_ADDITIONAL_FREE_ANY);
    }

    private void checkGetFreeX(final String method, final String methodAdditionalUrl) {
        assertNotNull(method);

        // collect valid urls
        String[] aux = new String[0];
        switch (methodAdditionalUrl) {
            case METHOD_GET_ADDITIONAL_FREE_POST:
                aux = sc.getFreeURLsPostRequest();
                break;
            case METHOD_GET_ADDITIONAL_FREE_GET:
                aux = sc.getFreeURLsGetRequest();
                break;
            case METHOD_GET_ADDITIONAL_FREE_ANY:
                aux = sc.getFreeURLsAnyRequest();
                break;
            default:
                // no op
        }

        final Collection<String> realFreeUrl = new ArrayList<>();
        for (String s : aux) {
            if (s != null && !s.isEmpty()) {
                realFreeUrl.add(s);
            }
        }

        SecurityConfig securityConfig = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
                authFacade);

        // add additional urls statically defined in SecurityConfig class
        final Object additionalUrl = ReflectionTestUtils.invokeMethod(securityConfig, methodAdditionalUrl);
        assertTrue("The " + methodAdditionalUrl + " method should return an array", additionalUrl instanceof String[]);
        realFreeUrl.addAll(Arrays.asList((String[]) additionalUrl));

        final Object actualFreeUrl = ReflectionTestUtils.invokeMethod(securityConfig, method);
        assertTrue("The " + method + " method should return an array", actualFreeUrl instanceof String[]);
        assertEquals("The size of the array returned by the " + method + " method does not math the size of "
                        + "the free post url specified by the SecurityConst component",
                realFreeUrl.size(), ((String[]) actualFreeUrl).length);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkAccessToFreeGetUrls() throws Exception {
        SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
                authFacade);
        checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_GET, null, null);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void checkAccessToFreeAnyUrls() throws Exception {
        SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
                authFacade);
        checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_ANY, null, null);
    }

    /**
     * Test to be executed by JUnit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void checkAccessToFreePostUrls() throws Exception {
        SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
                authFacade);
        Object args = ReflectionTestUtils.invokeGetterMethod(sConf, "getListOfParametersForFreePostUrl");
        assertTrue("The list of parameters for free post url must be an array of HashMap", args instanceof HashMap[]);
        checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_POST, HttpMethod.POST, (HashMap<String, String>[]) args);
    }

    private void checkAccessToFreeXUrls(final SecurityConfig securityConfig, final String methodName,
                                        final HttpMethod method, final HashMap<String, String>[] listOfArguments)
            throws Exception {
        final Object freeUrl = ReflectionTestUtils.invokeMethod(securityConfig, methodName);
        assertNotNull(freeUrl);
        assertTrue(freeUrl instanceof String[]);
        if (listOfArguments != null) {
            assertEquals("The list of argument provided must has the length as the list of free URLs is "
                    + "being testes", listOfArguments.length, ((String[]) freeUrl).length);
        }
        MvcResult mvcResult;
        MockHttpServletRequestBuilder request;
        String lastTestedUrl;
        for (int i = 0; i < ((String[]) freeUrl).length; i++) {
            lastTestedUrl = ((String[]) freeUrl)[i];
            if (method == null) {
                mvc.perform(get(lastTestedUrl)).andExpect(status().isOk());
            } else {
                if (method == HttpMethod.POST) {
                    request = post(lastTestedUrl);
                } else {
                    throw new RuntimeException("Case not implemented yet");
                }
                if (request != null) {
                    if (listOfArguments != null) {
                        request.contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listOfArguments[i]));
                    }
                    mvcResult = mvc.perform(request).andReturn();
                    assertTrue(
                            lastTestedUrl + " did not finish as expected. Response status is "
                                    + mvcResult.getResponse().getStatus(),
                            mvcResult.getResponse().getStatus() != HttpStatus.UNAUTHORIZED.value());
                    assertTrue(
                            lastTestedUrl + " did not finish as expected. Response status is "
                                    + mvcResult.getResponse().getStatus(),
                            mvcResult.getResponse().getStatus() != HttpStatus.FORBIDDEN.value());
                }
            }
        }
    }

}
