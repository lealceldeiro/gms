package com.gms.util.permission;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public enum BPermissionConst {

    /*USER*/
    /**
     * Allows to manage users. Super-permission over all other "user" permissions.
     */
    MANAGE_USER,
    /**
     * Allows to create new users.
     */
    CREATE_USER,
    /**
     * Allows to read a user information.
     */
    READ_USER,
    /**
     * Allows to read all users information. For example a list of them.
     */
    READ_ALL_USER,
    /**
     * Allows to read all users associated to an owned entity. i.e.: given the ids of several entities, read all users
     * associated to any of those entities.
     */
    READ_ASSOCIATED_USER,
    /**
     * Allows to update a user information.
     */
    UPDATE_USER,
    /**
     * Allows to delete a user information.
     */
    DELETE_USER,

    /*ROLE*/
    /**
     * Allows to manage roles. Super-permission over all other "role" permissions.
     */
    MANAGE_ROLE,
    /**
     * Allows to create new roles.
     */
    CREATE_ROLE,
    /**
     * Allows to read a role information.
     */
    READ_ROLE,
    /**
     * Allows to read all roles information. For example a list of them.
     */
    READ_ALL_ROLE,
    /**
     * Allows to update a role information.
     */
    UPDATE_ROLE,
    /**
     * Allows to delete a role information.
     */
    DELETE_ROLE,

    /*PERMISSION*/
    /**
     * Allows to manage permissions. Super-permission over all other "permission" permissions.
     */
    MANAGE_PERMISSION,
    /**
     * Allows to create new permissions.
     */
    CREATE_PERMISSION,
    /**
     * Allows to read a permission information.
     */
    READ_PERMISSION,
    /**
     * Allows to update a permission information.
     */
    UPDATE_PERMISSION,
    /**
     * Allows to delete a permission information.
     */
    DELETE_PERMISSION,

    /*OWNED_ENTITY*/
    /**
     * Allows to manage an owned entity. Super-permission over all other "owned entity" permissions.
     */
    MANAGE_OWNED_ENTITY,
    /**
     * Allows to create new owned entities.
     */
    CREATE_OWNED_ENTITY,
    /**
     * Allows to read an owned entity information.
     */
    READ_OWNED_ENTITY,
    /**
     * Allows to read all owned entities information. For example a list of them.
     */
    READ_ALL_OWNED_ENTITY,
    /**
     * Allows to update an owned entity information.
     */
    UPDATE_OWNED_ENTITY,
    /**
     * Allows to delete an owned entity information.
     */
    DELETE_OWNED_ENTITY,

    /*PROFILE*/
    /**
     * Allows to manage profile. Super-permission over all other "profile" permissions.
     */
    MANAGE_PROFILE,
    /**
     * Allows to read the profile information.
     */
    READ_PROFILE,
    /**
     * Allows to update a profile information.
     */
    UPDATE_PROFILE,

    /**
     * Allows to manage configuration. Super-permission over all other "configuration" permissions.
     */
    MANAGE_CONFIGURATION

}
