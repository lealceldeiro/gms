package com.gmsboilerplatesbng.util.constant;

import lombok.Getter;
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
@Getter
public class DefaultConst {

    //region role
    @Value("${gms.role.default.label:ROLE_ADMIN}")
    private String roleAdminDefaultLabel;

    @Value("${gms.role.default.description:Default role}")
    private String roleAdminDefaultDescription;

    @Value("${gms.role.default.enabled:true}")
    private boolean roleAdminDefaultEnabled;
    //endregion

    //region user
    @Value("${gms.user.default.name:Admin}")
    private String userAdminDefaultName;

    @Value("${gms.user.default.lastName:Default}")
    private String userAdminDefaultLastName;

    @Value("${gms.user.default.username:admin}")
    private String userAdminDefaultUsername;

    @Value("${gms.user.default.password:admin}")
    private String userAdminDefaultPassword;

    @Value("${gms.user.default.email:admin@gms.com}")
    private String userAdminDefaultEmail;
    //endregion

    //region entity
    @Value("${gms.entity.default.name:HOME}")
    private String entityDefaultName;

    @Value("${gms.entity.default.username:home}")
    private String entityDefaultUsername;

    @Value("${gms.entity.default.description:Default entity}")
    private String entityDefaultDescription;
    //endregion

    //region config
    @Value("${gms.config.multi-entity:false}")
    private Boolean isMultiEntity;

    @Value("${gms.config.user-registration-allowed:true}")
    private Boolean isUserRegistrationAllowed;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath:/api}")
    private String apiBasePath;

    public static final String API_DOC_PATH = "apidocs";
    //endregion

    //region vars
    @Value("${gms.config.page.size:1}")
    private int pageSize;

    @Value("${gms.config.page.size_holder:size}")
    private String pageSizeHolder;

    @Value("${gms.response.message:message}")
    private String resMessageHolder;
    //endregion

    //region lang
    @Value("${gms.i18n.lang_holder:lang}")
    private String languageHolder;

    @Value("${gms.i18n.default:en}")
    private String defaultLanguage;

    public static final String DEFAULT_LANGUAGE_HEADER = "Accept-Language";
    //endregion
}
