package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultConst {

    //region role
    @Value("${gms.role.default.label}")
    public String ROLE_LABEL = "ROLE_ADMIN";

    @Value("${gms.role.default.description}")
    public String ROLE_DESCRIPTION = "Default role";

    @Value("${gms.role.default.enabled}")
    public boolean ROLE_ENABLED = true;
    //endregion

    //region user
    @Value("${gms.user.default.name}")
    public String USER_USERNAME = "Admin";

    @Value("${gms.user.default.lastName}")
    public String USER_LAST_NAME = "Default";

    @Value("${gms.user.default.username}")
    public String USER_NAME = "admin";

    @Value("${gms.user.default.password}")
    public String USER_PASSWORD = "admin";

    @Value("${gms.user.default.email}")
    public String USER_EMAIL = "admin@gms.com";
    //endregion

    //region entity
    @Value("${gms.entity.default.name}")
    public String ENTITY_NAME = "HOME";

    @Value("${gms.entity.default.username}")
    public String ENTITY_USERNAME = "home";

    @Value("${gms.entity.default.description}")
    public String ENTITY_DESCRIPTION = "Default entity";
    //endregion

    //region config
    @Value("${gms.config.multi-entity}")
    public Boolean IS_MULTI_ENTITY = false;

    @Value("${gms.config.user-registration-allowed}")
    public Boolean IS_USER_REGISTRATION_ALLOWED = true;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath}")
    public String API_BASE_PATH = "/api";

    @Value("${gms.config.api-docs.basePath}")
    public String API_DOC_PATH = "/apidocs";
    //endregion

    //region vars
    @Value("${gms.config.page.size}")
    public int PAGE_SIZE = 1;

    @Value("${gms.config.page.size_holder}")
    public String PAGE_SIZE_HOLDER = "size";

    @Value("${gms.response.message}")
    public String RES_MESSAGE_HOLDER = "message";
    //endregion

    //region lang
    @Value("${gms.i18n.lang_holder}")
    public String LANGUAGE_HOLDER = "lang";

    @Value("${gms.i18n.default}")
    public String DEFAULT_LANGUAGE = "en";
    //endregion
}
