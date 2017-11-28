package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;

public class DefaultConst {

    //region role
    @Value("${default.gmsrole.label}")
    public static final String ROLE_LABEL = "ROLE_ADMIN";

    @Value("${default.gmsrole.description}")
    public static final String ROLE_DESCRIPTION = "Default role";

    @Value("${default.gmsrole.enabled}")
    public static final boolean ROLE_ENABLED = true;
    //endregion

    //region user
    @Value("${default.gmsuser.username}")
    public static final String USER_USERNAME = "admin";

    @Value("${default.gmsuser.email}")
    public static final String USER_EMAIL = "admin@example.com";
    //endregion

    //region entity
    @Value("${default.gmsentity.name}")
    public static final String ENTITY_NAME = "HOME";

    @Value("${default.gmsentity.username}")
    public static final String ENTITY_USERNAME = "home";

    @Value("${default.gmsentity.description}")
    public static final String ENTITY_DESCRIPTION = "Default entity";
    //endregion

    //region logic
    @Value("${default.entity.multi-entity}")
    public static final Boolean IS_MULTI_ENTITY = false;

    @Value("${default.user.registration.allowed}")
    public static final Boolean IS_USER_REGISTRATION_ALLOWED = true;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath}")
    public static final String API_BASE_PATH = "/api";

    public static final String API_DOC_PATH = "/apidocs";
    //endregion
}
