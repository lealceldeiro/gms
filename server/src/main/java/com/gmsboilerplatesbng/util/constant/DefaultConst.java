package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * DefaultConst
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Component
public class DefaultConst {

    //region role
    @Value("${gms.role.default.label:ROLE_ADMIN}")
    public String roleAdminDefaultLabel;

    @Value("${gms.role.default.description:Default role}")
    public String roleAdminDefaultDescription;

    @Value("${gms.role.default.enabled:true}")
    public boolean roleAdminDefaultEnabled;
    //endregion

    //region user
    @Value("${gms.user.default.name:Admin}")
    public String userAdminDefaultName;

    @Value("${gms.user.default.lastName:Default}")
    public String userAdminDefaultLastName;

    @Value("${gms.user.default.username:admin}")
    public String userAdminDefaultUsername;

    @Value("${gms.user.default.password:admin}")
    public String userAdminDefaultPassword;

    @Value("${gms.user.default.email:admin@gms.com}")
    public String userAdminDefaultEmail;
    //endregion

    //region entity
    @Value("${gms.entity.default.name:HOME}")
    public String entityDefaultName;

    @Value("${gms.entity.default.username:home}")
    public String entityDefaultUsername;

    @Value("${gms.entity.default.description:Default entity}")
    public String entityDefaultDescription;
    //endregion

    //region config
    @Value("${gms.config.multi-entity:false}")
    public Boolean isMultiEntity;

    @Value("${gms.config.user-registration-allowed:true}")
    public Boolean isUserRegistrationAllowed;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath:/api}")
    public String apiBasePath;

    @Value("${gms.config.api-docs.basePath:/apidocs}")
    public String apiDocPath;
    //endregion

    //region vars
    @Value("${gms.config.page.size:1}")
    public int pageSize;

    @Value("${gms.config.page.size_holder:size}")
    public String pageSizeHolder;

    @Value("${gms.response.message:message}")
    public String resMessageHolder;
    //endregion

    //region lang
    @Value("${gms.i18n.lang_holder:lang}")
    public String languageHolder;

    @Value("${gms.i18n.default:en}")
    public String defaultLanguage;

    public static final String DEFAULT_LANGUAGE_HEADER = "Accept-Language";
    //endregion
}
