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
     *  Relative url for searching any resource by any of its properties (like search).
     */
    public static final String MULTI_LIKE = "multi-like";

    /**
     *  Relative url for searching any resource by any of its properties.
     */
    public static final String MULTI = "multi";

    /**
     *  Relative url for searching any resource by name (the ones which have it) (like search).
     */
    public static final String NAME_LIKE = "name-like";

    /**
     *  Relative url for searching {@link com.gms.domain.security.permission.BPermission} resources by label (like search).
     */
    public static final String LABEL_LIKE = "label-like";

    /**
     *  Relative url for searching any resource by name (the ones which have it).
     */
    public static final String NAME = "name";

    /**
     *  Relative url for searching any resource by label (the ones which have it).
     */
    public static final String LABEL = "label";

    // endregion

    // region entity

    /**
     * Relative url for {@link com.gms.domain.security.ownedentity.EOwnedEntity} resources.
     */
    public static final String OWNED_ENTITY = "entity";

    /**
     * Relative url for searching any resource by username (the ones which have it) (like search).
     */
    public static final String USERNAME_LIKE = "username-like";

    /**
     * Relative url for searching any resource by username (the ones which have it).
     */
    public static final String USERNAME = "username";

    // endregion

    // region role

    /**
     * Relative url for {@link com.gms.domain.security.role.BRole} resources.
     */
    public static final String ROLE = "role";

    // endregion

    // region user

    /**
     * Relative url for {@link com.gms.domain.security.user.EUser} resources.
     */
    public static final String USER = "user";

    /**
     * Relative url for searching any resource by email (the ones which have it).
     */
    public static final String EMAIL = "email";

    /**
     * Relative url for searching any resource by last name (the ones which have it).
     */
    public static final String LASTNAME = "lastname";

    /**
     * Relative url for searching {@link com.gms.domain.security.user.EUser} resources by email (like search).
     */
    public static final String USER_SEARCH_EMAIL_LIKE = "email-like";

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

    // region query params

    /**
     * Query param "value"
     */
    public static final String QUERY_VALUE = "value";

    // endregion

    private ResourcePath() {}

    public static ResourcePath getInstance() {
        return new ResourcePath();
    }
}
