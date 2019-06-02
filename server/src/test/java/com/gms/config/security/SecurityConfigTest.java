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
import com.gms.testutil.*;
import com.gms.util.GMSRandom;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.*;

import static org.junit.Assert.*;
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

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(RestDoc.APIDOC_LOCATION);

	@Autowired
	private WebApplicationContext context;
	private ObjectMapper objectMapper = GmsSecurityUtil.getObjectMapper();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private SecurityConst sc;
	@Autowired
	private DefaultConst dc;

	@Autowired
	private AppService appService;
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private AuthenticationFacade authFacade;
	@Autowired
	private MessageResolver msg;

	@Autowired
	private BRoleRepository roleRepository;
	@Autowired
	private EOwnedEntityRepository entityRepository;
	@Autowired
	private EUserRepository userRepository;

	private MockMvc mvc;
	private RestDocumentationResultHandler restDocResHandler = RestDoc.getRestDocumentationResultHandler();

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
		assertTrue("Application initial configuration failed", appService.isInitialLoadOK());

		mvc = GmsMockUtil.getMvcMockForwardUrlNotNull(context, restDocumentation, restDocResHandler,
				springSecurityFilterChain);

		authHeader = sc.getATokenHeader();
		tokenType = sc.getATokenType();

		accessToken = GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, restDocResHandler, false);
	}

	@Test
	public void login() throws Exception { // for documentation purposes
		GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, restDocResHandler, false);
	}

	@Test
	public void loginKO() throws Exception {
		GmsSecurityUtil.createSuperAdminAuthToken(dc, sc, mvc, objectMapper, true);
	}

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

		List<Long> ids = new LinkedList<>();
		ids.add(roleWithoutPermissions.getId());

		try {
			assertTrue("Role could not be added to user",
					userService.addRolesToUser(user.getId(), entity.getId(), ids).size() == ids.size());

			Map<String, String> loginData = new HashMap<>();
			loginData.put(sc.getReqUsernameHolder(), user.getUsername());
			loginData.put(sc.getReqPasswordHolder(), password);

			mvc.perform(post(dc.getApiBasePath() + sc.getSignInUrl()).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginData))).andExpect(status().isUnauthorized());
		} catch (NotFoundEntityException e) {
			e.printStackTrace();
			fail("Role could not be added to user");
		}

	}

	@Test
	public void baseUrlPermitAll() throws Exception {
		int s = mvc.perform(get("/")).andReturn().getResponse().getStatus();
		assertTrue(s != HttpStatus.FORBIDDEN.value());
		assertTrue(s != HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void apiUrlsAreProtected() throws Exception {
		mvc.perform(get(dc.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
	}

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

	@Test
	public void failOnAccessTokenDoesNotStartProperly() throws Exception {
		mvc.perform(get(dc.getApiBasePath() + "/permissions").header(sc.getATokenHeader(),
				"AnotherTypeDifferentFromBearer " + accessToken)).andExpect(status().isUnauthorized());
	}

	@Test
	public void failOnNotAccessTokenHeaderProvided() throws Exception {
		mvc.perform(get(dc.getApiBasePath() + "/permissions")).andExpect(status().isUnauthorized());
	}

	@Test
	public void successOnCorrectAccessTokenProvided() throws Exception {
		mvc.perform(get(dc.getApiBasePath() + "/permission").header(authHeader, tokenType + " " + accessToken))
				.andExpect(status().isOk());
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
		}

		final ArrayList<String> realFreeUrl = new ArrayList<>();
		for (String s : aux) {
			if (s != null && !s.equals("")) {
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
		assertTrue("The size of the array returned by the " + method
				+ " method does not math the size of the free post url specified by the SecurityConst component",
				realFreeUrl.size() == ((String[]) actualFreeUrl).length);
	}

	@Test
	public void checkAccessToFreeGetUrls() throws Exception {
		SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
				authFacade);
		checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_GET, null, null);
	}

	@Test
	public void checkAccessToFreeAnyUrls() throws Exception {
		SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
				authFacade);
		checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_ANY, null, null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void checkAccessToFreePostUrls() throws Exception {
		SecurityConfig sConf = new SecurityConfig(sc, dc, msg, userService, encoder, objectMapper, jwtService,
				authFacade);
		Object args = ReflectionTestUtils.invokeGetterMethod(sConf, "getListOfParametersForFreePostUrl");
		assertTrue("The list of parameters for free post url must be an array of HashMap", args instanceof HashMap[]);
		checkAccessToFreeXUrls(sConf, METHOD_GET_FREE_POST, HttpMethod.POST, (HashMap[]) args);
	}

	private void checkAccessToFreeXUrls(SecurityConfig securityConfig, String methodName, HttpMethod method,
			HashMap<String, String>[] listOfArguments) throws Exception {
		final Object freeUrl = ReflectionTestUtils.invokeMethod(securityConfig, methodName);
		assertNotNull(freeUrl);
		assertTrue(freeUrl instanceof String[]);
		if (listOfArguments != null) {
			assertTrue("The list of argument provided must has the length as the list of free URLs is being testes",
					listOfArguments.length == ((String[]) freeUrl).length);
		}
		MvcResult r;
		MockHttpServletRequestBuilder request;
		String lastTestedUrl;
		for (int i = 0; i < ((String[]) freeUrl).length; i++) {
			lastTestedUrl = ((String[]) freeUrl)[i];
			if (method == null) {
				mvc.perform(get(lastTestedUrl)).andExpect(status().isOk());
			} else {
				switch (method) {
				case POST:
					request = post(lastTestedUrl);
					break;
				default:
					throw new RuntimeException("Case not implemented yet");

				}
				if (request != null) {
					if (listOfArguments != null) {
						request.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(listOfArguments[i]));
					}
					r = mvc.perform(request).andReturn();
					assertTrue(
							lastTestedUrl + " did not finish as expected. Response status is "
									+ r.getResponse().getStatus(),
							r.getResponse().getStatus() != HttpStatus.UNAUTHORIZED.value());
					assertTrue(
							lastTestedUrl + " did not finish as expected. Response status is "
									+ r.getResponse().getStatus(),
							r.getResponse().getStatus() != HttpStatus.FORBIDDEN.value());
				}
			}
		}
	}

}