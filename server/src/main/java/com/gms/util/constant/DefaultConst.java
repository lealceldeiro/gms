package com.gms.util.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Component
@Getter
public class DefaultConst {

    //region role
    /**
     * {@code label} property for default role created at initial start up.
     *
     * @see com.gms.domain.security.role.BRole
     */
    @Value("${gms.role.default.label:ROLE_ADMIN}")
    private String roleAdminDefaultLabel;

    /**
     * {@code description} property for default role created at initial start up.
     *
     * @see com.gms.domain.security.role.BRole
     */
    @Value("${gms.role.default.description:Default role}")
    private String roleAdminDefaultDescription;

    /**
     * {@code enabled} property for default role created at initial start up.
     *
     * @see com.gms.domain.security.role.BRole
     */
    @Value("${gms.role.default.enabled:true}")
    private boolean roleAdminDefaultEnabled;
    //endregion

    //region user
    /**
     * {@code name} property for default user created at initial start up.
     *
     * @see com.gms.domain.security.user.EUser
     */
    @Value("${gms.user.default.name:Admin}")
    private String userAdminDefaultName;

    /**
     * {@code lastName} property for default user created at initial start up.
     *
     * @see com.gms.domain.security.user.EUser
     */
    @Value("${gms.user.default.lastName:Default}")
    private String userAdminDefaultLastName;

    /**
     * {@code username} property for default user created at initial start up.
     *
     * @see com.gms.domain.security.user.EUser
     */
    @Value("${gms.user.default.username:admin}")
    private String userAdminDefaultUsername;

    /**
     * {@code password} property for default user created at initial start up.
     *
     * @see com.gms.domain.security.user.EUser
     */
    @Value("${gms.user.default.password:admin}")
    private String userAdminDefaultPassword;

    /**
     * {@code email} property for default user created at initial start up.
     *
     * @see com.gms.domain.security.user.EUser
     */
    @Value("${gms.user.default.email:admin@gms.com}")
    private String userAdminDefaultEmail;
    //endregion

    //region entity
    /**
     * {@code name} property for default owned entity created at initial start up.
     *
     * @see com.gms.domain.security.ownedentity.EOwnedEntity
     */
    @Value("${gms.entity.default.name:HOME}")
    private String entityDefaultName;

    /**
     * {@code username} property for default owned entity created at initial start up.
     *
     * @see com.gms.domain.security.ownedentity.EOwnedEntity
     */
    @Value("${gms.entity.default.username:home}")
    private String entityDefaultUsername;

    /**
     * {@code description} property for default owned entity created at initial start up.
     *
     * @see com.gms.domain.security.ownedentity.EOwnedEntity
     */
    @Value("${gms.entity.default.description:Default entity}")
    private String entityDefaultDescription;
    //endregion

    //region config
    /**
     * Initial which indicates whether this is a multi entity application or not.
     */
    @Value("${gms.config.multi-entity:false}")
    private Boolean isMultiEntity;

    /**
     * Initial which indicates whether this application will handle user registration via sign-up or not.
     */
    @Value("${gms.config.user-registration-allowed:true}")
    private Boolean isUserRegistrationAllowed;
    //endregion

    //region system
    /**
     * API base path.
     */
    @Value("${spring.data.rest.basePath:/api}")
    private String apiBasePath;

    /**
     * API documentation base path.
     */
    public static final String API_DOC_PATH = "apidocs";
    //endregion

    //region vars
    /**
     * Default size of pages.
     */
    @Value("${spring.data.rest.default-page-size:10}")
    private String pageSize;

    /**
     * Name of the URL query string parameter that indicates how many results to return at
     * once.
     */
    @Value("${spring.data.rest.limit-param-name:size}")
    private String pageSizeParam;

    /**
     * Name of the URL query string parameter that indicates what page to return.
     */
    @Value("${spring.data.rest.page-param-name:page}")
    private String pagePageParam;

    /**
     * Name of the URL query string parameter that indicates what direction to sort
     * results.
     */
    @Value("${spring.data.rest.sort-param-name:sort}")
    private String pageSortParam;

    /**
     * Variable in which all message will be sent in the response body (when sent).
     */
    @Value("${gms.response.message:message}")
    private String resMessageHolder;
    //endregion

    //region lang
    /**
     * Request variable in which it is specified what should be the response language.
     *
     * @see DefaultConst#DEFAULT_LANGUAGE_HEADER
     */
    @Value("${gms.i18n.lang.holder:lang}")
    private String languageHolder;

    /**
     * Default language should be set in the response.
     */
    @Value("${gms.i18n.lang.default:en}")
    private String defaultLanguage;

    /**
     * Header in which it is specified what should be the response language.
     *
     * @see DefaultConst#languageHolder
     */
    public static final String DEFAULT_LANGUAGE_HEADER = "Accept-Language";
    //endregion
}
