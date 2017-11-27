package com.gmsboilerplatesbng.util.configuration;

public enum ConfigKey {
    /**
     * App which handles multiple entities (enterprises, businesses,etc)
     */
    IS_MULTI_ENTITY_APP,

    /**
     * New users are can be register using a "register" form
     */
    IS_USER_REGISTRATION_ALLOWED,

    /**
     * Last language used by a user
     */
    LANGUAGE,

    /**
     * Specifies the id of the last accessed entity by a user
     */
    LAST_ACCESSED_ENTITY
}
