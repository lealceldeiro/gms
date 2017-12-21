package com.gmsboilerplatesbng.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.Application;
import com.gmsboilerplatesbng.component.security.authentication.IAuthenticationFacade;
import com.gmsboilerplatesbng.component.security.token.JWTService;
import com.gmsboilerplatesbng.service.AppService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.GmsSecurityUtil;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.constant.SecurityConst;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SecurityConfigTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired private WebApplicationContext context;

    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired private FilterChainProxy springSecurityFilterChain;

    @Autowired private SecurityConst sc;
    @Autowired private DefaultConst dc;

    @Autowired AppService appService;
    @Autowired UserService userService;
    @Autowired BCryptPasswordEncoder encoder;
    @Autowired JWTService jwtService;
    @Autowired IAuthenticationFacade authFacade;

    private MockMvc mvc;

    private RestDocumentationResultHandler restDocResHandler;

    private String authHeader;
    private String tokenType;
    private String accessToken;

    private static final String METHOD_GET_FREE_POST = "getFreePost";
    private static final String METHOD_GET_FREE_GET = "getFreeGet";
    private static final String METHOD_GET_FREE_ANY = "getFreeAny";
    private static final String METHOD_GET_ADDITIONAL_FREE_POST = "getAdditionalFreePostUrls";
    private static final String METHOD_GET_ADDITIONAL_FREE_GET = "getAdditionalFreeGetUrls";
    private static final String METHOD_GET_ADDITIONAL_FREE_ANY = "getAdditionalFreeAnyUrls";

    @Before
    public void setUp() throws Exception {
        org.junit.Assert.assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

        restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.restDocResHandler)
                .addFilter(this.springSecurityFilterChain)
                .build();

        authHeader = sc.getATokenHeader();
        tokenType = sc.getATokenType();

        accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper);
    }

    @Test
    public void baseUrlPermitAll() throws Exception {
        mvc.perform( get("/")).andExpect(status().isOk());
    }

    @Test
    public void apiUrlsAreProtected() throws Exception {
        mvc.perform( get(dc.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
    }

    @Test
    public void failOnAccessTokenIntegrityCompromised() throws Exception {
        mvc.perform(
                get(dc.getApiBasePath() + "/permissions")
                        .header(sc.getATokenHeader(), "sampleOfWrongToken")
        ).andExpect(status().isUnauthorized());
        mvc.perform(
                get(dc.getApiBasePath() + "/permissions")
                        .header(sc.getATokenHeader(), accessToken + "manipulated")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void successOnCorrectAccessTokenProvided() throws Exception {
        mvc.perform(
                get(dc.getApiBasePath() + "/permission")
                        .header(authHeader, tokenType + " " + accessToken)
        ).andExpect(status().isOk());
    }

    @Test
    public void checkGetFreePost() {
        checkGetFreeX(METHOD_GET_FREE_POST, METHOD_GET_ADDITIONAL_FREE_POST);
    }

    @Test
    public void checkGetFreeGet() {
        checkGetFreeX(METHOD_GET_FREE_GET, METHOD_GET_ADDITIONAL_FREE_GET);
    }

    @Test
    public void checkGetFreeAny() {
        checkGetFreeX(METHOD_GET_FREE_ANY, METHOD_GET_ADDITIONAL_FREE_ANY);
    }

    private void checkGetFreeX(String method, String methodAdditionalUrl) {
        assertNotNull(method);

        //collect valid urls
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
        }

        final ArrayList<String> realFreeUrl = new ArrayList<>();
        for (String s : aux) {
            if (s != null && !s.equals("")) {
                realFreeUrl.add(s);
            }
        }

        SecurityConfig securityConfig = new SecurityConfig(sc, dc, userService, encoder, objectMapper, jwtService, authFacade);

        //add additional urls statically defined in SecurityConfig class
        final Object additionalUrl = ReflectionTestUtils.invokeMethod(securityConfig, methodAdditionalUrl);
        assertTrue("The " + methodAdditionalUrl + " method should return an array", additionalUrl instanceof String[]);
        realFreeUrl.addAll(Arrays.asList((String[])additionalUrl));

        final Object actualFreeUrl = ReflectionTestUtils.invokeMethod(securityConfig, method);
        assertTrue("The " + method + " method should return an array", actualFreeUrl instanceof String[]);
        assertTrue("The size of the array returned by the " + method + " method does not math the size of the free post url specified by the SecurityConst component",
                realFreeUrl.size() == ((String[]) actualFreeUrl).length);
    }

}