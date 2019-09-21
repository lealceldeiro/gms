package com.gms.util.constant;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DefaultConstTest {

    @Value("${gms.role.default.label:ROLE_ADMIN}")
    private String roleLabelBind;

    @Value("${gms.role.default.description:Default role}")
    private String roleDescBind;

    @Value("${gms.role.default.enabled:true}")
    private boolean roleEnabledBind;

    @Value("${gms.user.default.name:Admin}")
    private String userNameBind;

    @Value("${gms.user.default.lastName:Default}")
    private String userLastNameBind;

    @Value("${gms.user.default.username:admin}")
    private String userUsernameBind;

    @Value("${gms.user.default.password:admin}")
    private String userPassBind;

    @Value("${gms.user.default.email:admin@gms.com}")
    private String userEmailBind;

    @Value("${gms.entity.default.name:HOME}")
    private String entityNameBind;

    @Value("${gms.entity.default.username:home}")
    private String entityUsernameBind;

    @Value("${gms.entity.default.description:Default entity}")
    private String entityDescBind;

    @Value("${gms.config.multi-entity:false}")
    private boolean isMultiEntityBind;

    @Value("${gms.config.user-registration-allowed:true}")
    private boolean isUserRegistrationAllowedBind;

    @Value("${spring.data.rest.basePath:/api}")
    private String apiBasePathBind;

    @Value("${spring.data.rest.default-page-size:10}")
    private String pageSizeBind;

    @Value("${spring.data.rest.limit-param-name:size}")
    private String pageSizeParamBind;

    @Value("${spring.data.rest.page-param-name:page}")
    private String pagePageParamBind;

    @Value("${spring.data.rest.sort-param-name:sort}")
    private String pageSortParamBind;

    @Value("${gms.response.message:message}")
    private String resMessageHolderBind;

    @Value("${gms.i18n.lang.holder:lang}")
    private String languageHolderBind;

    @Value("${gms.i18n.lang.default:en}")
    private String defaultLanguageBind;

    @Autowired private DefaultConst autowiredDc;
    private DefaultConst entity0;

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

    @Test
    public void getRoleAdminDefaultLabel() {
        cleanEntity();
        String roleLabel = "roleLabel";
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultLabel", roleLabel);
        assertEquals(roleLabel, entity0.getRoleAdminDefaultLabel());
    }

    @Test
    public void getRoleAdminDefaultDescription() {
        cleanEntity();
        String roleDesc = "roleDesc";
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultDescription", roleDesc);
        assertEquals(roleDesc, entity0.getRoleAdminDefaultDescription());
    }

    @Test
    public void isRoleAdminDefaultEnabled() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "roleAdminDefaultEnabled", true);
        assertTrue(entity0.isRoleAdminDefaultEnabled());
    }

    @Test
    public void getUserAdminDefaultName() {
        cleanEntity();
        String userName = "userName";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultName", userName);
        assertEquals(userName, entity0.getUserAdminDefaultName());
    }

    @Test
    public void getUserAdminDefaultLastName() {
        cleanEntity();
        String userLastName = "userLastName";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultLastName", userLastName);
        assertEquals(userLastName, entity0.getUserAdminDefaultLastName());
    }

    @Test
    public void getUserAdminDefaultUsername() {
        cleanEntity();
        String userUsername = "userUsername";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultUsername", userUsername);
        assertEquals(userUsername, entity0.getUserAdminDefaultUsername());
    }

    @Test
    public void getUserAdminDefaultPassword() {
        cleanEntity();
        String userPass = "userPass";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultPassword", userPass);
        assertEquals(userPass, entity0.getUserAdminDefaultPassword());
    }

    @Test
    public void getUserAdminDefaultEmail() {
        cleanEntity();
        String userEmail = "user@email.com";
        ReflectionTestUtils.setField(entity0, "userAdminDefaultEmail", userEmail);
        assertEquals(userEmail, entity0.getUserAdminDefaultEmail());
    }

    @Test
    public void getEntityDefaultName() {
        cleanEntity();
        String entityName = "enatityName";
        ReflectionTestUtils.setField(entity0, "entityDefaultName", entityName);
        assertEquals(entityName, entity0.getEntityDefaultName());
    }

    @Test
    public void getEntityDefaultUsername() {
        cleanEntity();
        String entityUsername = "enatityUsername";
        ReflectionTestUtils.setField(entity0, "entityDefaultUsername", entityUsername);
        assertEquals(entityUsername, entity0.getEntityDefaultUsername());
    }

    @Test
    public void getEntityDefaultDescription() {
        cleanEntity();
        String entityDesc = "entityDesc";
        ReflectionTestUtils.setField(entity0, "entityDefaultDescription", entityDesc);
        assertEquals(entityDesc, entity0.getEntityDefaultDescription());
    }

    @Test
    public void getIsMultiEntity() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "isMultiEntity", true);
        assertTrue(entity0.getIsMultiEntity());
    }

    @Test
    public void getIsUserRegistrationAllowed() {
        cleanEntity();
        ReflectionTestUtils.setField(entity0, "isUserRegistrationAllowed", false);
        assertFalse(entity0.getIsUserRegistrationAllowed());
    }

    @Test
    public void getApiBasePath() {
        cleanEntity();
        String apiBasePath = "apiBasePath";
        ReflectionTestUtils.setField(entity0, "apiBasePath", apiBasePath);
        assertEquals(apiBasePath, entity0.getApiBasePath());
    }

    @Test
    public void getPageSize() {
        cleanEntity();
        String pageSize = "9999";
        ReflectionTestUtils.setField(entity0, "pageSize", pageSize);
        assertEquals(pageSize, entity0.getPageSize());
    }

    @Test
    public void getPageSizeParam() {
        cleanEntity();
        String pageSizeParam = "pageSizeParam";
        ReflectionTestUtils.setField(entity0, "pageSizeParam", pageSizeParam);
        assertEquals(pageSizeParam, entity0.getPageSizeParam());
    }

    @Test
    public void getPagePageParam() {
        cleanEntity();
        String pagePageParam = "pagePageParam";
        ReflectionTestUtils.setField(entity0, "pagePageParam", pagePageParam);
        assertEquals(pagePageParam, entity0.getPagePageParam());
    }

    @Test
    public void getPageSortParam() {
        cleanEntity();
        String pageSortParam = "pageSortParam";
        ReflectionTestUtils.setField(entity0, "pageSortParam", pageSortParam);
        assertEquals(pageSortParam, entity0.getPageSortParam());
    }

    @Test
    public void getResMessageHolder() {
        cleanEntity();
        String resMessageHolder = "resMessageHolder";
        ReflectionTestUtils.setField(entity0, "resMessageHolder", resMessageHolder);
        assertEquals(resMessageHolder, entity0.getResMessageHolder());
    }

    @Test
    public void getLanguageHolder() {
        cleanEntity();
        String languageHolder = "languageHolder";
        ReflectionTestUtils.setField(entity0, "languageHolder", languageHolder);
        assertEquals(languageHolder, entity0.getLanguageHolder());
    }

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

    private void assertEntityValidity(DefaultConst entity) {
        assertNull(ReflectionTestUtils.getField(entity, "roleAdminDefaultLabel"));
        assertNull(ReflectionTestUtils.getField(entity, "roleAdminDefaultDescription"));
        assertFalse(Boolean.parseBoolean(ReflectionTestUtils.getField(entity, "roleAdminDefaultEnabled").toString()));
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
        Object iura = ReflectionTestUtils.getField(entity, "isUserRegistrationAllowed");
        assertFalse(Boolean.parseBoolean(iura != null ? iura.toString() : null));
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