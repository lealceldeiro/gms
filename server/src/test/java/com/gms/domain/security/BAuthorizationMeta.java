package com.gms.domain.security;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BAuthorizationMeta {
    private BAuthorizationMeta() {}

    public static final String userId = "User's identifier (id)";
    public static final String entityIdAdd = "Entity's identifier (id) over which the user will have the roles";
    public static final String entityIdRemove = "Entity's identifier (id) over which the user will not have the roles";
    public static final String roleIdAdd = "Role's identifier (id) the user will have over the entity";
    public static final String roleIdRemove = "Role's identifier (id) the user will not have over the entity";
}
