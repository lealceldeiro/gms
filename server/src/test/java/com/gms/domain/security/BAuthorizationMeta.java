package com.gms.domain.security;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class BAuthorizationMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private BAuthorizationMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String USER_ID_INFO = "User's identifier (id)";
    /**
     * Meta documentation.
     */
    public static final String ENTITY_ID_ADD_INFO = "Entity's identifier (id) over which the user will have the roles";
    /**
     * Meta documentation.
     */
    public static final String ENTITY_ID_REMOVE_INFO = "Entity's identifier (id) over which the user will not have "
            + "the roles";
    /**
     * Meta documentation.
     */
    public static final String ROLE_ID_ADD_INFO = "Role's identifier (id) the user will have over the entity";
    /**
     * Meta documentation.
     */
    public static final String ROLE_ID_REMOVE_INFO = "Role's identifier (id) the user will not have over the entity";

}
