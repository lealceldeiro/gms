package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultConst {

    //region role
    @Value("${gms.role.default.label:ROLE_ADMIN}")
    public String ROLE_LABEL;

    @Value("${gms.role.default.description:Default role}")
    public String ROLE_DESCRIPTION;

    @Value("${gms.role.default.enabled:true}")
    public boolean ROLE_ENABLED;
    //endregion

    //region user
    @Value("${gms.user.default.name:Admin}")
    public String USER_USERNAME;

    @Value("${gms.user.default.lastName:Default}")
    public String USER_LAST_NAME;

    @Value("${gms.user.default.username:admin}")
    public String USER_NAME;

    @Value("${gms.user.default.password:admin}")
    public String USER_PASSWORD;

    @Value("${gms.user.default.email:admin@gms.com}")
    public String USER_EMAIL;
    //endregion

    //region entity
    @Value("${gms.entity.default.name:HOME}")
    public String ENTITY_NAME;

    @Value("${gms.entity.default.username:home}")
    public String ENTITY_USERNAME;

    @Value("${gms.entity.default.description:Default entity}")
    public String ENTITY_DESCRIPTION;
    //endregion

    //region config
    @Value("${gms.config.multi-entity:false}")
    public Boolean IS_MULTI_ENTITY;

    @Value("${gms.config.user-registration-allowed:true}")
    public Boolean IS_USER_REGISTRATION_ALLOWED;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath:/api}")
    public String API_BASE_PATH;

    @Value("${gms.config.api-docs.basePath:/apidocs}")
    public String API_DOC_PATH;
    //endregion

    //region vars
    @Value("${gms.config.page.size:1}")
    public int PAGE_SIZE;

    @Value("${gms.config.page.size_holder:size}")
    public String PAGE_SIZE_HOLDER;

    @Value("${gms.response.message:message}")
    public String RES_MESSAGE_HOLDER;
    //endregion

    //region lang
    @Value("${gms.i18n.lang_holder:lang}")
    public String LANGUAGE_HOLDER;

    @Value("${gms.i18n.default:en}")
    public String DEFAULT_LANGUAGE;

    public String DEFAULT_LANGUAGE_HEADER = "Accept-Language";
    //endregion
}
