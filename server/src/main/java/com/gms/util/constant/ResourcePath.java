package com.gms.util.constant;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class ResourcePath {

    // region configuration

    /**
     * Relative url for {@link com.gms.domain.configuration.BConfiguration} resources.
     */
    public static final String CONFIGURATION = "configuration";

    // endregion

    // region permission

    /**
     *  Relative url for {@link com.gms.domain.security.permission.BPermission} resources.
     */
    public static final String PERMISSION = "permission";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by name or
     *  label (like search).
     */
    public static final String PERMISSION_SEARCH_NAME_LABEL_LIKE = "name-label-like";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by name (like search).
     */
    public static final String PERMISSION_SEARCH_NAME_LIKE = "name-like";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by label (like search).
     */
    public static final String PERMISSION_SEARCH_LABEL_LIKE = "label-like";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by name.
     */
    public static final String PERMISSION_SEARCH_NAME = "name";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by label.
     */
    public static final String PERMISSION_SEARCH_LABEL = "label";

    // endregion

    // region entity

    /**
     * Relative url for {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources.
     */
    public static final String OWNED_ENTITY = "entity";

    /**
     * Relative url for searching {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources by name or
     * username (like search).
     */
    public static final String OWNED_ENTITY_SEARCH_NAME_USERNAME_LIKE = "name-username-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources by name (like search).
     */
    public static final String OWNED_ENTITY_SEARCH_NAME_LIKE = "name-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources by username (like search).
     */
    public static final String OWNED_ENTITY_SEARCH_USERNAME_LIKE = "username-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources by name.
     */
    public static final String OWNED_ENTITY_SEARCH_NAME = "name";

    /**
     * Relative url for searching {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources by username.
     */
    public static final String OWNED_ENTITY_SEARCH_USERNAME = "username";

    // endregion

    // region role

    /**
     * Relative url for {@link com.gms.domain.security.role.BRole} resources.
     */
    public static final String ROLE = "role";

    /**
     * Relative url for searching {@link com.gms.domain.security.role.BRole} resources by label (like search).
     */
    public static final String ROLE_SEARCH_LABEL_LIKE = "label-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.role.BRole} resources by label.
     */
    public static final String ROLE_SEARCH_LABEL = "label";

    // endregion

    // region user

    /**
     * Relative url for {@link com.gms.domain.security.user.EUser} resources.
     */
    public static final String USER = "user";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by username.
     */
    public static final String USER_SEARCH_USERNAME = "username";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by email.
     */
    public static final String USER_SEARCH_EMAIL = "email";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by name.
     */
    public static final String USER_SEARCH_NAME = "name";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by last name.
     */
    public static final String USER_SEARCH_LASTNAME = "lastname";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by username (like search).
     */
    public static final String USER_SEARCH_USERNAME_LIKE = "username-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by email (like search).
     */
    public static final String USER_SEARCH_EMAIL_LIKE = "email-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by name (like search).
     */
    public static final String USER_SEARCH_NAME_LIKE = "name-like";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by last name (like search).
     */
    public static final String USER_SEARCH_LASTNAME_LIKE = "lastname-like";

    // endregion

    // region authorization

    /**
     * Relative url for {@link com.gms.domain.security.BAuthorization} resources.
     */
    public static final String AUTHORIZATION = "authorization";

    // endregion

    // region query

    /**
     * Query param "value"
     */
    public static final String QUERY_VALUE = "value";

    // endregion

    private ResourcePath() {}
}
