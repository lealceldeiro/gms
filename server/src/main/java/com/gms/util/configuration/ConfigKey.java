package com.gms.util.configuration;

/**
 * ConfigKey
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
public enum ConfigKey {
    /**
     * App which handles multiple entities (enterprises, businesses,etc)
     */
    IS_MULTI_ENTITY_APP_IN_SERVER,

    /**
     * New users are can be register using a "register" form
     */
    IS_USER_REGISTRATION_ALLOWED_IN_SERVER,

    /**
     * Last language used by a user
     */
    LANGUAGE,

    /**
     * Specifies the id of the last accessed entity by a user
     */
    LAST_ACCESSED_ENTITY
}
