package com.gmsboilerplatesbng.util.constant;

import org.springframework.beans.factory.annotation.Value;

public class DefaultConst {

    //region role
    @Value("${default.gmsrole.label}")
    public static String ROLE_LABEL = "ROLE_ADMIN";

    @Value("${default.gmsrole.description}")
    public static String ROLE_DESCRIPTION = "Default role";

    @Value("${default.gmsrole.enabled}")
    public static boolean ROLE_ENABLED = true;
    //endregion

    //region user
    @Value("${default.gmsuser.username}")
    public static String USER_USERNAME = "admin";

    @Value("${default.gmsuser.email}")
    public static String USER_EMAIL = "admin@example.com";
    //endregion

    //region entity
    @Value("${default.gmsentity.name}")
    public static String ENTITY_NAME = "HOME";

    @Value("${default.gmsentity.username}")
    public static String ENTITY_USERNAME = "home";

    @Value("${default.gmsentity.description}")
    public static String ENTITY_DESCRIPTION = "Default entity";
    //endregion

    //region logic
    @Value("${default.entity.multi-entity}")
    public static Boolean IS_MULTI_ENTITY = false;

    @Value("${default.user.registration.allowed}")
    public static Boolean IS_USER_REGISTRATION_ALLOWED = true;
    //endregion

    //region system
    @Value("${spring.data.rest.basePath}")
    public static String API_BASE_PATH;

    public static String API_DOC_PATH = "apidocs";
    //endregion
}
