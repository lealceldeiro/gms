package com.gms.domain.security;

/**
 * BAuthorizationMeta
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 12, 2018
 */
public class BAuthorizationMeta {
    private BAuthorizationMeta() {}

    public static final String userId = "User's identifier (id)";
    public static final String entityId = "Entity's identifier (id) over which the user will have (or not) the roles";
    public static final String roleId = "Role's identifier (id) the user will have (or not) over the entity";
}
