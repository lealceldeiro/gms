package com.gms.util.constant;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DefaultConstTest {

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.role.default.label:ROLE_ADMIN}")
    private String roleLabelBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.role.default.description:Default role}")
    private String roleDescBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.role.default.enabled:true}")
    private boolean roleEnabledBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.user.default.name:Admin}")
    private String userNameBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.user.default.lastName:Default}")
    private String userLastNameBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.user.default.username:admin}")
    private String userUsernameBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.user.default.password:admin}")
    private String userPassBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.user.default.email:admin@gms.com}")
    private String userEmailBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.entity.default.name:HOME}")
    private String entityNameBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.entity.default.username:home}")
    private String entityUsernameBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.entity.default.description:Default entity}")
    private String entityDescBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.config.multi-entity:false}")
    private boolean isMultiEntityBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.config.user-registration-allowed:true}")
    private boolean isUserRegistrationAllowedBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${spring.data.rest.basePath:/api}")
    private String apiBasePathBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${spring.data.rest.default-page-size:10}")
    private String pageSizeBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${spring.data.rest.limit-param-name:size}")
    private String pageSizeParamBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${spring.data.rest.page-param-name:page}")
    private String pagePageParamBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${spring.data.rest.sort-param-name:sort}")
    private String pageSortParamBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.response.message:message}")
    private String resMessageHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.i18n.lang.holder:lang}")
    private String languageHolderBind;

    /**
     * Injected (by framework) config value.
     */
    @Value("${gms.i18n.lang.default:en}")
    private String defaultLanguageBind;

    /**
     * Injected (by framework) config value.
     */
    @Autowired
    private DefaultConst autowiredDc;

    /**
     * Instance of {@link DefaultConst}.
     */
    private DefaultConst entity0;

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void defaultValuesLoadedProperly() {
        assertEquals(roleLabelBind, autowiredDc.getRoleAdminDefaultLabel());
        assertEquals(roleDescBind, autowiredDc.getRoleAdminDefaultDescription());
        assertEquals(roleEnabledBind, autowiredDc.isRoleAdminDefaultEnabled());
        assertEquals(userNameBind, autowiredDc.getUserAdminDefaultName());
        assertEquals(userLastNameBind, autowiredDc.getUserAdminDefaultLastName());
        assertEquals(userUsernameBind, autowiredDc.getUserAdminDefaultUsername());
        assertEquals(userPassBind, autowiredDc.getUserAdminDefaultPassword());
        assertEquals(userEmailBind, autowiredDc.getUserAdminDefaultEmail());
        assertEquals(entityNameBind, autowiredDc.getEntityDefaultName());
        assertEquals(entityUsernameBind, autowiredDc.getEntityDefaultUsername());
        assertEquals(entityDescBind, autowiredDc.getEntityDefaultDescription());
        assertEquals(isMultiEntityBind, autowiredDc.getIsMultiEntity());
        assertEquals(isUserRegistrationAllowedBind, autowiredDc.getIsUserRegistrationAllowed());
        assertEquals(apiBasePathBind, autowiredDc.getApiBasePath());
        assertEquals(pageSizeBind, autowiredDc.getPageSize());
        assertEquals(pageSizeParamBind, autowiredDc.getPageSizeParam());
        assertEquals(pagePageParamBind, autowiredDc.getPagePageParam());
        assertEquals(pageSortParamBind, autowiredDc.getPageSortParam());
        assertEquals(resMessageHolderBind, autowiredDc.getResMessageHolder());
        assertEquals(languageHolderBind, autowiredDc.getLanguageHolder());
        assertEquals(defaultLanguageBind, autowiredDc.getDefaultLanguage());

        assertEquals("apidocs", ReflectionTestUtils.getField(autowiredDc, "API_DOC_PATH"));
        assertEquals("Accept-Language", ReflectionTestUtils.getField(autowiredDc, "DEFAULT_LANGUAGE_HEADER"));
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRoleAdminDefaultLabel() {
        cleanEntity();
        String roleLabel = "roleLabel";
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultLabel", roleLabel);
        assertEquals(roleLabel, entity0.getRoleAdminDefaultLabel());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getRoleAdminDefaultDescription() {
        cleanEntity();
        String roleDesc = "roleDesc";
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultDescription", roleDesc);
        assertEquals(roleDesc, entity0.getRoleAdminDefaultDescription());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void isRoleAdminDefaultEnabled() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultEnabled", true);
        assertTrue(entity0.isRoleAdminDefaultEnabled());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserAdminDefaultName() {
        cleanEntity();
        String userName = "userName";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultName", userName);
        assertEquals(userName, entity0.getUserAdminDefaultName());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserAdminDefaultLastName() {
        cleanEntity();
        String userLastName = "userLastName";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultLastName", userLastName);
        assertEquals(userLastName, entity0.getUserAdminDefaultLastName());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserAdminDefaultUsername() {
        cleanEntity();
        String userUsername = "userUsername";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultUsername", userUsername);
        assertEquals(userUsername, entity0.getUserAdminDefaultUsername());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserAdminDefaultPassword() {
        cleanEntity();
        String userPass = "userPass";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultPassword", userPass);
        assertEquals(userPass, entity0.getUserAdminDefaultPassword());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getUserAdminDefaultEmail() {
        cleanEntity();
        String userEmail = "user@email.com";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultEmail", userEmail);
        assertEquals(userEmail, entity0.getUserAdminDefaultEmail());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEntityDefaultName() {
        cleanEntity();
        String entityName = "enatityName";
        ReflectionTestUtils.setField(entity0, "entityDefaultName", entityName);
        assertEquals(entityName, entity0.getEntityDefaultName());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEntityDefaultUsername() {
        cleanEntity();
        String entityUsername = "enatityUsername";
        ReflectionTestUtils.setField(entity0, "entityDefaultUsername", entityUsername);
        assertEquals(entityUsername, entity0.getEntityDefaultUsername());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getEntityDefaultDescription() {
        cleanEntity();
        String entityDesc = "entityDesc";
        ReflectionTestUtils.setField(entity0, "entityDefaultDescription", entityDesc);
        assertEquals(entityDesc, entity0.getEntityDefaultDescription());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getIsMultiEntity() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "isMultiEntity", true);
        assertTrue(entity0.getIsMultiEntity());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getIsUserRegistrationAllowed() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "isUserRegistrationAllowed", false);
        assertFalse(entity0.getIsUserRegistrationAllowed());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getApiBasePath() {
        cleanEntity();
        String apiBasePath = "apiBasePath";
        ReflectionTestUtils.setField(entity0, "apiBasePath", apiBasePath);
        assertEquals(apiBasePath, entity0.getApiBasePath());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPageSize() {
        cleanEntity();
        String pageSize = "9999";
        ReflectionTestUtils.setField(entity0, "pageSize", pageSize);
        assertEquals(pageSize, entity0.getPageSize());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPageSizeParam() {
        cleanEntity();
        String pageSizeParam = "pageSizeParam";
        ReflectionTestUtils.setField(entity0, "pageSizeParam", pageSizeParam);
        assertEquals(pageSizeParam, entity0.getPageSizeParam());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPagePageParam() {
        cleanEntity();
        String pagePageParam = "pagePageParam";
        ReflectionTestUtils.setField(entity0, "pagePageParam", pagePageParam);
        assertEquals(pagePageParam, entity0.getPagePageParam());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getPageSortParam() {
        cleanEntity();
        String pageSortParam = "pageSortParam";
        ReflectionTestUtils.setField(entity0, "pageSortParam", pageSortParam);
        assertEquals(pageSortParam, entity0.getPageSortParam());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getResMessageHolder() {
        cleanEntity();
        String resMessageHolder = "resMessageHolder";
        ReflectionTestUtils.setField(entity0, "resMessageHolder", resMessageHolder);
        assertEquals(resMessageHolder, entity0.getResMessageHolder());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getLanguageHolder() {
        cleanEntity();
        String languageHolder = "languageHolder";
        ReflectionTestUtils.setField(entity0, "languageHolder", languageHolder);
        assertEquals(languageHolder, entity0.getLanguageHolder());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getDefaultLanguage() {
        cleanEntity();
        String defaultLanguage = "ch";
        ReflectionTestUtils.setField(entity0, "defaultLanguage", defaultLanguage);
        assertEquals(defaultLanguage, entity0.getDefaultLanguage());
    }

    private void cleanEntity() {
        entity0 = new DefaultConst();
        assertEntityValidity(entity0);
    }

    private void assertEntityValidity(final DefaultConst entity) {
        assertNull(ReflectionTestUtils.getField(entity, "roleAdminDefaultLabel"));
        assertNull(ReflectionTestUtils.getField(entity, "roleAdminDefaultDescription"));
        assertFalse(Boolean.parseBoolean(
                String.valueOf(ReflectionTestUtils.getField(entity, "roleAdminDefaultEnabled"))
        ));
        assertNull(ReflectionTestUtils.getField(entity, "userAdminDefaultName"));
        assertNull(ReflectionTestUtils.getField(entity, "userAdminDefaultLastName"));
        assertNull(ReflectionTestUtils.getField(entity, "userAdminDefaultUsername"));
        assertNull(ReflectionTestUtils.getField(entity, "userAdminDefaultPassword"));
        assertNull(ReflectionTestUtils.getField(entity, "userAdminDefaultEmail"));
        assertNull(ReflectionTestUtils.getField(entity, "entityDefaultName"));
        assertNull(ReflectionTestUtils.getField(entity, "entityDefaultUsername"));
        assertNull(ReflectionTestUtils.getField(entity, "entityDefaultDescription"));
        Object ime = ReflectionTestUtils.getField(entity, "isMultiEntity");
        assertFalse(Boolean.parseBoolean(ime != null ? ime.toString() : null));
        Object userRegistrationAllowed = ReflectionTestUtils.getField(entity, "isUserRegistrationAllowed");
        assertFalse(Boolean.parseBoolean(userRegistrationAllowed != null ? userRegistrationAllowed.toString() : null));
        assertNull(ReflectionTestUtils.getField(entity, "apiBasePath"));
        assertNull(ReflectionTestUtils.getField(entity, "pageSize"));
        assertNull(ReflectionTestUtils.getField(entity, "pageSizeParam"));
        assertNull(ReflectionTestUtils.getField(entity, "pagePageParam"));
        assertNull(ReflectionTestUtils.getField(entity, "pageSortParam"));
        assertNull(ReflectionTestUtils.getField(entity, "resMessageHolder"));
        assertNull(ReflectionTestUtils.getField(entity, "languageHolder"));
        assertNull(ReflectionTestUtils.getField(entity, "defaultLanguage"));
    }

}
